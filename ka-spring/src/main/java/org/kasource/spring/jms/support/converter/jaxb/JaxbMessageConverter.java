package org.kasource.spring.jms.support.converter.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;

public class JaxbMessageConverter implements MessageConverter {

    private static final String JMS_JAXB_PACKAGE_PROPERTY = "jaxbPackage";
    private static final String JMS_USE_FASTINFOSET_PROPERTY = "useFastInfoset";
    private boolean useFastInfoset;

    
    public JaxbMessageConverter() { }
    
    public JaxbMessageConverter(boolean useFastInfoset) { 
        this.useFastInfoset = useFastInfoset;
    }
    
    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        
        boolean isFastInfoset = message.getBooleanProperty(JMS_USE_FASTINFOSET_PROPERTY);
       
        String jaxbPackage = getJaxbPackageName(message);

        try {
            JAXBContext context = JAXBContext.newInstance(jaxbPackage);
            if(isFastInfoset) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] jaxbMessage = new byte[(int) bytesMessage.getBodyLength()];
               
                bytesMessage.readBytes(jaxbMessage);
               
                return getObjectFromFastInfoset(context, jaxbMessage);
            } else {
                String jaxbMessage = ((TextMessage) message).getText();
                return context.createUnmarshaller().unmarshal(
                            new ByteArrayInputStream(jaxbMessage.getBytes("UTF-8")));
            }
            
        } catch (Exception e) {
            throw new MessageConversionException(e.getMessage(), e);
        }
    }

    private Object getObjectFromFastInfoset(JAXBContext context, byte[] data) throws UnsupportedEncodingException, JAXBException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        XMLStreamReader streamReader = new StAXDocumentParser(bis);
        return context.createUnmarshaller().unmarshal(streamReader);

    }

    private String getJaxbPackageName(Message message) throws JMSException {
        String jaxbPackage = message.getStringProperty(JMS_JAXB_PACKAGE_PROPERTY);
        if (jaxbPackage == null) {
            throw new MessageConversionException("Text property " + JMS_JAXB_PACKAGE_PROPERTY + " is missing");
        }
        return jaxbPackage;
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        try {
            Message message = null;

            String packageName = object.getClass().getPackage().getName();
            JAXBContext context = JAXBContext.newInstance(packageName);
            if(useFastInfoset) {
                message = session.createBytesMessage();
                ((BytesMessage)message).writeBytes(getMessageContentAsFastInfoSet(object, context));
            } else {
                message = session.createTextMessage();
                ((TextMessage) message).setText(getMessageContentAsString(object, context));
            }
            message.setStringProperty(JMS_JAXB_PACKAGE_PROPERTY, packageName);
            message.setBooleanProperty(JMS_USE_FASTINFOSET_PROPERTY, useFastInfoset);
            return message;
        } catch (Exception e) {
            throw new MessageConversionException(e.getMessage(), e);
        }
    }

    private String getMessageContentAsString(Object object, JAXBContext context) throws JAXBException {
        StringWriter messageContent = new StringWriter();
        context.createMarshaller().marshal(object, messageContent);
        return messageContent.toString();
    }

    private byte[] getMessageContentAsFastInfoSet(Object object, JAXBContext context) throws JAXBException, UnsupportedEncodingException {      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StAXDocumentSerializer staxDocumentSerializer = new StAXDocumentSerializer();
        staxDocumentSerializer.setOutputStream(bos);
        XMLStreamWriter streamWriter = staxDocumentSerializer;
        context.createMarshaller().marshal(object, streamWriter);
        try {
            streamWriter.close();
        } catch (XMLStreamException e) {
        }
        return bos.toByteArray();
    }

    /**
     * @return the useFastInfoset
     */
    public boolean isUseFastInfoset() {
        return useFastInfoset;
    }

    /**
     * @param useFastInfoset the useFastInfoset to set
     */
    public void setUseFastInfoset(boolean useFastInfoset) {
        this.useFastInfoset = useFastInfoset;
    }

    

}
