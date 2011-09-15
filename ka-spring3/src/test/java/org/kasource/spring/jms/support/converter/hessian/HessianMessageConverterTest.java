package org.kasource.spring.jms.support.converter.hessian;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

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

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HessianMessageConverterTest {

    @Mock
    private BytesMessage bytesMessage;
    
    
    private byte[] messageAsByteArray = {112,2,0,67,48,84,111,114,103,46,107,97,115,111,117,114,99,101,46,115,112,114,105,110,103,46,106,109,115,46,115,117,112,112,111,114,116,46,99,111,110,118,101,114,116,101,114,46,104,101,115,115,105,97,110,46,72,101,115,115,105,97,110,77,101,115,115,97,103,101,67,111,110,118,101,114,116,101,114,84,101,115,116,36,80,101,114,115,111,110,-110,4,110,97,109,101,3,97,103,101,96,6,82,105,107,97,114,100,-73,122};
    
    @Mock
    private Session session;
    
    @TestedObject
    private HessianMessageConverter converter;
    
    @Test
    public void toMessageTest() throws JMSException {
        Capture<byte[]> capturedBytes = new Capture<byte[]>();
        Person me = new Person("Rikard", 39);
        expect(session.createBytesMessage()).andReturn(bytesMessage);
        bytesMessage.writeBytes(capture(capturedBytes));
        expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(bytesMessage, converter.toMessage(me, session));
        
    }
    
    @Test
    public void fromMessageTest() throws MessageConversionException, JMSException {
        expect(bytesMessage.getBodyLength()).andReturn((long) messageAsByteArray.length);
        expect(bytesMessage.readBytes(replaceBytes(messageAsByteArray))).andReturn(messageAsByteArray.length);
        EasyMockUnitils.replay();
        Object object = converter.fromMessage(bytesMessage);
        assertTrue(object instanceof Person);
        Person person = (Person) object;
        assertEquals("Rikard", person.getName());
        assertEquals(39, person.getAge());
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
    
    @SuppressWarnings("serial")
    private static class Person implements Serializable {
        private String name;
        private int age;
       
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        @SuppressWarnings("unused")
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the age
         */
        public int getAge() {
            return age;
        }
        /**
         * @param age the age to set
         */
        @SuppressWarnings("unused")
        public void setAge(int age) {
            this.age = age;
        }
        
        
    }
}
