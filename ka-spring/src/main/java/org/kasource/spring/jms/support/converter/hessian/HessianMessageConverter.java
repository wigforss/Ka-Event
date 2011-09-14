package org.kasource.spring.jms.support.converter.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianFactory;
import com.caucho.hessian.io.SerializerFactory;

public class HessianMessageConverter implements MessageConverter {

    private HessianFactory factory = new HessianFactory();

   

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        BytesMessage bytesMessage = (BytesMessage) message;
        byte[] data = new byte[(int) bytesMessage.getBodyLength()];

        bytesMessage.readBytes(data);
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        Hessian2Input in = factory.createHessian2Input(bin);
       

        try {
            in.startMessage();
            Object object = in.readObject();
            in.completeMessage();
            return object;

        } catch (IOException e) {
            throw new MessageConversionException(e.getMessage(), e);
        } finally {

            try {

                in.close();
            } catch (IOException e) {
            }

        }
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = factory.createHessian2Output(bos);

        try {
            out.startMessage();
            out.writeObject(object);
            out.completeMessage();
            out.close();
            byte[] data = bos.toByteArray();
            BytesMessage message = session.createBytesMessage();
            message.writeBytes(data);
            return message;
        } catch (IOException e) {
            throw new MessageConversionException(e.getMessage(), e);
        }

    }

    public void setSerializationFactory(List<SerializerFactory> factories) {
        SerializerFactory mainFactory = factory.getSerializerFactory();
        for(SerializerFactory serializerFactory : factories) {
            mainFactory.addFactory(serializerFactory);
        }
    }

    /**
     * @param factory the factory to set
     */
    public void setFactory(HessianFactory factory) {
        this.factory = factory;
    }


}
