package org.kasource.kaevent.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.kasource.kaevent.listener.implementations.BeanListener;
import org.kasource.kaevent.listener.implementations.ChannelListener;
import org.kasource.kaevent.listener.register.RegisterListenerByAnnotation;
import org.kasource.kaevent.listener.register.RegisterListenerByAnnotationImpl;





/**
 * Aspect that register / unregister instances that are annotated with
 * @ChannelLister,
 * 
 * @BeanListener, @RegisterListener and @UnregisterListener.
 * 
 *                Used when using spring-agent... -javaagent:"/home/rikard/.m2/repository/org/springframework/spring-agent/2.5.6/spring-agent-2.5.6.jar"
 * 
 *                Also make sure the aspect is enabled in the META-INF/aop.xml
 *                file.
 * 
 * @author Rikard Wigforss
 * @version $Id$
 **/
@Aspect
public class RegisterListenerAspect {

  
    private RegisterListenerByAnnotation registerListenerHandler = RegisterListenerByAnnotationImpl.getInstance();

    

    @SuppressWarnings("unused")
    @Pointcut("@target(org.kasource.kaevent.listener.implementations.ChannelListener) && execution(@org.kasource.kaevent.listener.RegisterListener * *(..))")
    private void channelListenerRegisterAnnotatedMethod() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@target(org.kasource.kaevent.listener.implementations.ChannelListener) && execution(@org.kasource.kaevent.listener.UnregisterListener * *(..))")
    private void channelListenerUnregisterAnnotatedMethod() {
    }


    @SuppressWarnings("unused")
    @Pointcut("@target(org.kasource.kaevent.listener.implementations.BeanListener) && execution(@org.kasource.kaevent.listener.RegisterListener * *(..))")
    private void beanListenerRegisterAnnotatedMethod() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@target(org.kasource.kaevent.listener.implementations.BeanListener) && execution(@org.kasource.kaevent.listener.UnregisterListener * *(..))")
    private void beanListenerUnregisterAnnotatedMethod() {
    }

    // Advice

    @After("channelListenerRegisterAnnotatedMethod()")
    public void registerChannelListenerByRegisterAnnotation(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        ChannelListener channelListenerAnnotation = (ChannelListener) listener.getClass().getAnnotation(
                ChannelListener.class);
       
            registerListenerHandler.registerChannelListener(channelListenerAnnotation, listener);
        
    }

   

    /**
     * Unregister channel listener from its channels.
     * 
     * @param thisJoinPoint
     **/
    @After("channelListenerUnregisterAnnotatedMethod()")
    public void unregisterChannelListener(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        ChannelListener channelListenerAnnotation = (ChannelListener) listener.getClass().getAnnotation(
               ChannelListener.class);
        registerListenerHandler.unregisterChannelListener(channelListenerAnnotation, listener);
    }

    @After("beanListenerRegisterAnnotatedMethod()")
    public void registerBeanListenerByRegisterAnnotation(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        BeanListener beanListenerAnnotation = (BeanListener) listener.getClass().getAnnotation(
               BeanListener.class);

       
            registerListenerHandler.registerBeanListener(beanListenerAnnotation, listener);
        

    }

   

    @After("beanListenerUnregisterAnnotatedMethod()")
    public void unregisterBeanListener(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        BeanListener beanListenerAnnotation = (BeanListener) listener.getClass().getAnnotation(
                BeanListener.class);
        registerListenerHandler.unregisterBeanListener(beanListenerAnnotation, listener);

    }

  

}
