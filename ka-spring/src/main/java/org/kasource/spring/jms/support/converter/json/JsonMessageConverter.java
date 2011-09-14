package org.kasource.spring.jms.support.converter.json;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

public class JsonMessageConverter implements MessageConverter {
    private static Map<String, Class<?>> loadedCLasses = new HashMap<String, Class<?>>();
    
    private static final String JMS_JACKSON_ROOT_CLASS = "JsonRootClass";
    private ObjectMapper objectMapper = new ObjectMapper();
    
    
    
    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        try {
            TextMessage textMessage = (TextMessage)  message;
            String className = textMessage.getStringProperty(JMS_JACKSON_ROOT_CLASS);
            Class<?> clazz = loadClass(className);
            return objectMapper.readValue(textMessage.getText(), clazz);
        } catch (Exception e) {
            throw new MessageConversionException(e.getMessage(), e);
        }
       
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        try {
            TextMessage message = session.createTextMessage(objectMapper.writeValueAsString(object));
            message.setStringProperty(JMS_JACKSON_ROOT_CLASS, object.getClass().getName());
            return message;
        } catch(Exception e) {
            throw new MessageConversionException(e.getMessage(), e);
        }
    }

    
    private Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz = loadedCLasses.get(className);
        if(clazz == null) {
            clazz = Class.forName(className);
            loadedCLasses.put(className, clazz);
        }
        return clazz;
    }
    
}
