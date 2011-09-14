package org.kasource.spring.jms.support.converter.jaxb;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.easymock.Capture;
import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.jms.support.converter.MessageConversionException;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class JaxbMessageConverterTest {

    @Mock
    private Session session;
    
    @Mock
    private TextMessage textMessage;
    
    @Mock
    private BytesMessage bytesMessage;
    
    private String messageText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><car color=\"Blue\" doors=\"4\" xmlns=\"http://\"/>";
    
    @TestedObject
    private JaxbMessageConverter converter;
    
    private byte[] infoSetData = {-32,0,0,1,0,120,-51,6,104,116,116,112,58,47,47,-16,61,-127,2,99,97,114,120,4,99,111,108,111,114,67,66,108,117,101,120,4,100,111,111,114,115,64,52,-1,-16};
    
    
    @Test
    public void toMessageTest() throws JMSException {
        Car car = new Car();
        car.setColor("Blue");
        car.setDoors(4);
        
        Capture<String> xml = new Capture<String>();
        
        EasyMock.expect(session.createTextMessage()).andReturn(textMessage);
        textMessage.setText(EasyMock.capture(xml));
        EasyMock.expectLastCall();
        textMessage.setStringProperty("jaxbPackage", car.getClass().getPackage().getName());
        EasyMock.expectLastCall();
        textMessage.setBooleanProperty("useFastInfoset", false);
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(textMessage, converter.toMessage(car, session));
        String xmlContent = xml.getValue();
       
        assertTrue(xmlContent.contains("car"));
        assertTrue(xmlContent.contains("color=\"Blue\""));
        assertTrue(xmlContent.contains("doors=\"4\""));
    }
    
    

    @Test
    public void toFastInfosetMessageTest() throws JMSException {
        InjectionUtils.injectInto(true, converter, "useFastInfoset");
        
        Car car = new Car();
        car.setColor("Blue");
        car.setDoors(4);
        
        
        Capture<byte[]> xml = new Capture<byte[]>();
        
        EasyMock.expect(session.createBytesMessage()).andReturn(bytesMessage);
        bytesMessage.writeBytes(EasyMock.capture(xml));
        EasyMock.expectLastCall();
        bytesMessage.setStringProperty("jaxbPackage", car.getClass().getPackage().getName());
        EasyMock.expectLastCall();
        bytesMessage.setBooleanProperty("useFastInfoset", false);
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(bytesMessage, converter.toMessage(car, session));
       
    }
    
    
    @Test
    public void fromMessageTest() throws MessageConversionException, JMSException {
        EasyMock.expect(textMessage.getBooleanProperty("useFastInfoset")).andReturn(false);
        EasyMock.expect(textMessage.getText()).andReturn(messageText);
        EasyMock.expect(textMessage.getStringProperty("jaxbPackage")).andReturn(Car.class.getPackage().getName());
        
        EasyMockUnitils.replay();
        Car car = (Car) converter.fromMessage(textMessage);
        assertEquals("Blue", car.getColor());
        assertEquals(4, car.getDoors());
    }
    
    @Test
    public void fromFastInfoSetMessageTest() throws MessageConversionException, JMSException {
        EasyMock.expect(bytesMessage.getBooleanProperty("useFastInfoset")).andReturn(true);
        EasyMock.expect(bytesMessage.getBodyLength()).andReturn((long) infoSetData.length);
        EasyMock.expect(bytesMessage.readBytes(replaceBytes(infoSetData))).andReturn(infoSetData.length);
      
        EasyMock.expect(bytesMessage.getStringProperty("jaxbPackage")).andReturn(Car.class.getPackage().getName());
        
        EasyMockUnitils.replay();
        Car car = (Car) converter.fromMessage(bytesMessage);
        assertEquals("Blue", car.getColor());
        assertEquals(4, car.getDoors());
    }
    
    private static byte[] replaceBytes(byte[] data) {
        EasyMock.reportMatcher(new BytesParameterReplacer(data));
        return null;
    }
    
    private static class BytesParameterReplacer implements IArgumentMatcher{
        
        private byte[] replacement;
        
        public BytesParameterReplacer(byte[] replacement) {
            this.replacement = replacement;
        }

        @Override
        public boolean matches(Object argument) {
            if(argument != null && argument instanceof byte[]) {
                 byte[] data = (byte[]) argument;
                 System.arraycopy(replacement, 0, data, 0, data.length);
  
                return true;
            }
            return false;
        }

        @Override
        public void appendTo(StringBuffer buffer) {
           buffer.append("replace bytes");
            
        }
        
    }
}
