package org.kasource.spring.jms.support.converter.json;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.Capture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jms.support.converter.MessageConversionException;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class JsonMessageConverterTest {

    @Mock
    private Session session;
    
    @Mock
    private TextMessage textMessage;
    
    private String jsonMessageContent = "{\"version\":\"9.2.0.1\",\"vendor\":\"Oracle\"}";
    
    @TestedObject
    private JsonMessageConverter converter;
    
    @Test
    public void toMessageTest() throws MessageConversionException, JMSException {
        Capture<String> jsonData = new Capture<String>();
        Database oracle = new Database("Oracle", "9.2.0.1");
        expect(session.createTextMessage(capture(jsonData))).andReturn(textMessage);
        textMessage.setStringProperty("JsonRootClass", Database.class.getName());
        expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(textMessage, converter.toMessage(oracle, session));
        
        assertTrue(jsonData.getValue().contains("\"version\":\"9.2.0.1\""));
        assertTrue(jsonData.getValue().contains("\"vendor\":\"Oracle\""));
    }
 
    
    @Test
    public void fromMessageTest() throws MessageConversionException, JMSException {
        expect(textMessage.getText()).andReturn(jsonMessageContent);
        expect(textMessage.getStringProperty("JsonRootClass")).andReturn(Database.class.getName());
        EasyMockUnitils.replay();
        Database database = (Database) converter.fromMessage(textMessage);
        assertEquals("Oracle", database.getVendor());
        assertEquals("9.2.0.1", database.getVersion());
    }
    
    
    private static class Database {
        private String vendor;
        private String version;
        
        @SuppressWarnings("unused")
        public Database() {
            
        }
       
        public Database(String vendor, String version) {
            this.vendor = vendor;
            this.version = version;
        }
        /**
         * @return the vendor
         */
        public String getVendor() {
            return vendor;
        }
        /**
         * @param vendor the vendor to set
         */
        @SuppressWarnings("unused")
        public void setVendor(String vendor) {
            this.vendor = vendor;
        }
        /**
         * @return the version
         */
        public String getVersion() {
            return version;
        }
        /**
         * @param version the version to set
         */
        @SuppressWarnings("unused")
        public void setVersion(String version) {
            this.version = version;
        }
        
        
    }
}
