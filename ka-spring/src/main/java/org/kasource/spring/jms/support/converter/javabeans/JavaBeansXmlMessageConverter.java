package org.kasource.spring.jms.support.converter.javabeans;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.jvnet.fastinfoset.FastInfosetSource;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.xml.sax.SAXException;

import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;

/**
 * Converts JMS Message to Object by serializing to XML, based on JavaBeans.
 * 
 * Any <i>object graph</i> which consists of JavaBeans can be serialized.
 * 
 * Objects should have a default constructor and attributes according to JavaBeans.
 * <p>
 * If a default constructor is missing, construction information needs to be provided in order to serialize the object.
 * 
 * Either supply an  serializationConstructorInfo map for the expected objects in the object graph or 
 * annotate the classes with @SerializationConstructor. 
 * <p>
 * Note: The names of the constructor parameters must match JavaBeans attributes of the
 * object.
 * <p>
 * For more complex constructor scenarios an persistenceDelegateInfo Map can be
 * provided.
 * 
 * @author rikardwi
 **/
public class JavaBeansXmlMessageConverter implements MessageConverter {
    private static final String JMS_USE_FASTINFOSET_PROPERTY = "useFastInfoset";
    private Map<Class<?>, String[]> serializationConstructorInfo;
    private Map<Class<?>, PersistenceDelegate> persistenceDelegateInfo;
    private boolean useFastInfoset;

    public JavaBeansXmlMessageConverter() {
    }
    
    public JavaBeansXmlMessageConverter(boolean useFastInfoset) {
        this.useFastInfoset = useFastInfoset;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        ByteArrayInputStream in = null;
        boolean isFastInfoset = message.getBooleanProperty(JMS_USE_FASTINFOSET_PROPERTY);
        
        
        try {
            if(isFastInfoset) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] messageXmlContent = new byte[(int) bytesMessage.getBodyLength()];          
                bytesMessage.readBytes(messageXmlContent);
                in = parseFastInfoset(messageXmlContent);
            } else {
                String messageXmlContent = ((TextMessage) message).getText();              
                in = new ByteArrayInputStream(messageXmlContent.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            throw new MessageConversionException(e.getMessage(), e);
        }
        XMLDecoder xdec = new XMLDecoder(in);
        return xdec.readObject();
    }

    public ByteArrayInputStream parseFastInfoset(byte[] data) throws UnsupportedEncodingException, TransformerFactoryConfigurationError, TransformerException {
     
        InputStream input = new ByteArrayInputStream(data);      
        ByteArrayOutputStream xmlDocument = new ByteArrayOutputStream();        
        Transformer tx = TransformerFactory.newInstance().newTransformer();       
        tx.transform(new FastInfosetSource(input), new StreamResult(xmlDocument));     
        return new ByteArrayInputStream(xmlDocument.toByteArray());
    }
    
    
    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        
        Message message = null;
        try {
            if(useFastInfoset) {
                message = session.createBytesMessage();
              
                ((BytesMessage) message).writeBytes(getMessageContentAsFastInfoset(object));
            } else {
                message = session.createTextMessage();
                ((TextMessage) message).setText(getMessageContent(object));
            }
            message.setBooleanProperty(JMS_USE_FASTINFOSET_PROPERTY, useFastInfoset);     
        } catch (Exception e) {
            throw new MessageConversionException(e.getMessage(), e);
        }

        return message;
    }

    private String getMessageContent(Object object) throws UnsupportedEncodingException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        XMLEncoder xenc = new XMLEncoder(byteStream);
        initializeEncoder(xenc);
        initializeEncoderForObject(xenc, object);
        xenc.writeObject(object);
        xenc.close();
        return byteStream.toString("UTF-8");
    }
    
    private byte[] getMessageContentAsFastInfoset(Object object) throws ParserConfigurationException, SAXException, IOException {
        
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        XMLEncoder xenc = new XMLEncoder(byteStream);
        initializeEncoder(xenc);
        initializeEncoderForObject(xenc, object);
        xenc.writeObject(object);
        xenc.close();
        InputStream xmlDocument = new ByteArrayInputStream(byteStream.toByteArray());
        byteStream.reset();
        SAXDocumentSerializer saxDocumentSerializer = new SAXDocumentSerializer();
        saxDocumentSerializer.setOutputStream(byteStream);       
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance(); 
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();    
        saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", saxDocumentSerializer);     
        saxParser.parse(xmlDocument, saxDocumentSerializer);
        return byteStream.toByteArray();
        
    }
    
    private void initializeEncoderForObject(Encoder encoder, Object object) {
        if (!hasDefaultConstructor(object.getClass())) {
            if (object.getClass().isAnnotationPresent(SerializationConstructor.class)) {
                String[] constructorParams = object.getClass().getAnnotation(SerializationConstructor.class).value();
                encoder.setPersistenceDelegate(object.getClass(), new DefaultPersistenceDelegate(constructorParams));
            }
        }
    }

    private boolean hasDefaultConstructor(Class<?> clazz) {
        try {
            clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException nse) {
            return false;
        }
        return true;
    }

    /***
     * Initialize the encoder (XML). Add PersistenceDelegate for all classes without an empty constructor here.
     * 
     * @param encoder
     *            The encoder to initialize
     ***/
    private void initializeEncoder(Encoder encoder) {
        for (DefaultJavaSerializationConstructor cons : DefaultJavaSerializationConstructor.values()) {

            encoder.setPersistenceDelegate(cons.getType(),
                        new DefaultPersistenceDelegate(cons.getConstructorParameterNames()));
        }
        if (serializationConstructorInfo != null) {
            for (Map.Entry<Class<?>, String[]> cons : serializationConstructorInfo.entrySet()) {
                encoder.setPersistenceDelegate(cons.getKey(), new DefaultPersistenceDelegate(cons.getValue()));
            }
        }
        for (DefaultJavaPersistenceDelegate delegate : DefaultJavaPersistenceDelegate.values()) {
            encoder.setPersistenceDelegate(delegate.getType(), delegate.getPersistenceDelegate());
        }
        if (persistenceDelegateInfo != null) {
            for (Map.Entry<Class<?>, PersistenceDelegate> delegate : persistenceDelegateInfo.entrySet()) {
                encoder.setPersistenceDelegate(delegate.getKey(), delegate.getValue());
            }
        }

    }

    
   
    
    /**
     * @return the serializationConstructorInfo
     */
    public Map<Class<?>, String[]> getSerializationConstructorInfo() {
        return serializationConstructorInfo;
    }

    /**
     * @param serializationConstructorInfo the serializationConstructorInfo to set
     */
    public void setSerializationConstructorInfo(Map<Class<?>, String[]> serializationConstructorInfo) {
        this.serializationConstructorInfo = serializationConstructorInfo;
    }

    /**
     * @return the persistenceDelegateInfo
     */
    public Map<Class<?>, PersistenceDelegate> getPersistenceDelegateInfo() {
        return persistenceDelegateInfo;
    }

    /**
     * @param persistenceDelegateInfo the persistenceDelegateInfo to set
     */
    public void setPersistenceDelegateInfo(Map<Class<?>, PersistenceDelegate> persistenceDelegateInfo) {
        this.persistenceDelegateInfo = persistenceDelegateInfo;
    }

    
}
