package org.kasource.spring.jms.support.converter.javabeans;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.easymock.Capture;
import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jms.support.converter.MessageConversionException;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class JavaBeansXmlMessageConverterTest {

    @Mock
    private Session session;
    
    @Mock
    private TextMessage textMessage;
    
    @Mock
    private BytesMessage bytesMessage;
    
    private String messageContent = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
     + "<java version=\"1.6.0_26\" class=\"java.beans.XMLDecoder\">" 
     + "<object class=\"org.kasource.spring.jms.support.converter.javabeans.Tea\">" 
     + "<void property=\"brand\">" 
     + "<string>Twinings</string>" 
     + "</void>" 
     + "<void property=\"taste\">" 
     + "<string>Earl Grey</string>" 
     + "</void>" 
     + "</object>" 
     + "</java>"; 
    
    private byte[] fastInfosetContent = {-32,0,0,1,0,124,3,106,97,118,97,120,6,118,101,114,115,105,111,110,71,49,46,54,46,48,95,50,54,120,4,99,108,97,115,115,72,12,106,97,118,97,46,98,101,97,110,115,46,88,77,76,68,101,99,111,100,101,114,-16,-112,32,-111,10,32,124,5,111,98,106,101,99,116,1,8,46,111,114,103,46,107,97,115,111,117,114,99,101,46,115,112,114,105,110,103,46,106,109,115,46,115,117,112,112,111,114,116,46,99,111,110,118,101,114,116,101,114,46,106,97,118,97,98,101,97,110,115,46,84,101,97,-16,-96,-110,0,10,32,32,124,3,118,111,105,100,120,7,112,114,111,112,101,114,116,121,68,98,114,97,110,100,-16,-96,-110,1,10,32,32,32,60,5,115,116,114,105,110,103,-110,5,84,119,105,110,105,110,103,115,-16,-96,-94,-16,-96,-94,66,2,68,116,97,115,116,101,-16,-96,-93,3,-110,6,69,97,114,108,32,71,114,101,121,-16,-96,-94,-16,-96,-95,-16,-96,-112,10,-1};
    
    @TestedObject
    private JavaBeansXmlMessageConverter converter;
    
    
    @Test
    public void toMessageTest() throws JMSException {
        Capture<String> xmlData = new Capture<String>();
        Tea earlGrey = new Tea("Twinings", "Earl Grey");
        expect(session.createTextMessage()).andReturn(textMessage);
        textMessage.setText(capture(xmlData));
        expectLastCall();
        textMessage.setBooleanProperty("useFastInfoset", false);
        expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(textMessage, converter.toMessage(earlGrey, session));
        
        assertTrue(xmlData.getValue().contains("class=\""+ Tea.class.getName()+"\""));
        assertTrue(xmlData.getValue().contains("property=\"brand\""));
        assertTrue(xmlData.getValue().contains("<string>Twinings</string>"));
        assertTrue(xmlData.getValue().contains("property=\"taste\""));
        assertTrue(xmlData.getValue().contains("<string>Earl Grey</string>"));
    }
    
    
    @Test
    public void fromMessageTest() throws MessageConversionException, JMSException {
        expect(textMessage.getBooleanProperty("useFastInfoset")).andReturn(false);
        expect(textMessage.getText()).andReturn(messageContent);
        
        EasyMockUnitils.replay();
        Tea tea = (Tea) converter.fromMessage(textMessage);
        assertEquals("Twinings", tea.getBrand());
        assertEquals("Earl Grey", tea.getTaste());
    }
    
    
    @Test
    public void toMessageFastInfosetTest() throws JMSException {
        Capture<byte[]> fastInfosetData = new Capture<byte[]>();
        InjectionUtils.injectInto(true, converter, "useFastInfoset");
        Tea earlGrey = new Tea("Twinings", "Earl Grey");
        expect(session.createBytesMessage()).andReturn(bytesMessage);
        bytesMessage.writeBytes(capture(fastInfosetData));
        expectLastCall();
        bytesMessage.setBooleanProperty("useFastInfoset", true);
        expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(bytesMessage, converter.toMessage(earlGrey, session));
        
        
    }
  
    
    @Test
    public void fromMessageFastInfosetTest() throws MessageConversionException, JMSException {
        expect(bytesMessage.getBooleanProperty("useFastInfoset")).andReturn(true);
        expect(bytesMessage.getBodyLength()).andReturn((long)fastInfosetContent.length);
        expect(bytesMessage.readBytes(replaceBytes(fastInfosetContent))).andReturn(fastInfosetContent.length);
      
        EasyMockUnitils.replay();
        Tea tea = (Tea) converter.fromMessage(bytesMessage);
        assertEquals("Twinings", tea.getBrand());
        assertEquals("Earl Grey", tea.getTaste());
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
