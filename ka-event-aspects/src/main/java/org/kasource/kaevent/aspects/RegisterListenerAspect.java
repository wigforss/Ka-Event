package org.kasource.kaevent.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;





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
 * @author wigforss
 * @version $Id$
 **/
@Aspect
public class RegisterListenerAspect {

    public RegisterListenerAspect() {
        
    }
     
    
    
    private EventDispatcher eventDispatcher;
    private RegisterListenerByAnnotation registerListenerHandler = new RegisterListenerByAnnotationImpl();

    @SuppressWarnings("unused")
    @Pointcut("@target(com.kenai.sadelf.annotations.impl.ChannelListener) && execution(*.new(..))")
    private void channelListenerConstructor() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@annotation(com.kenai.sadelf.annotations.impl.RegisterListener) && @target(com.kenai.sadelf.annotations.impl.ChannelListener) && execution(*.new(..))")
    private void channelListenerRegisterAnnotatedConstructor() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@target(com.kenai.sadelf.annotations.impl.ChannelListener) && execution(@com.kenai.sadelf.annotations.impl.RegisterListener * *(..))")
    private void channelListenerRegisterAnnotatedMethod() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@target(com.kenai.sadelf.annotations.impl.ChannelListener) && execution(@com.kenai.sadelf.annotations.impl.UnregisterListener * *(..))")
    private void channelListenerUnregisterAnnotatedMethod() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@target(com.kenai.sadelf.annotations.impl.BeanListener) && execution(*.new(..))")
    private void beanListenerConstructor() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@annotation(com.kenai.sadelf.annotations.impl.RegisterListener) && @target(com.kenai.sadelf.annotations.impl.BeanListener) && execution(*.new(..))")
    private void beanListenerRegisterAnnotatedConstructor() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@target(com.kenai.sadelf.annotations.impl.BeanListener) && execution(@com.kenai.sadelf.annotations.impl.RegisterListener * *(..))")
    private void beanListenerRegisterAnnotatedMethod() {
    }

    @SuppressWarnings("unused")
    @Pointcut("@target(com.kenai.sadelf.annotations.impl.BeanListener) && execution(@com.kenai.sadelf.annotations.impl.UnregisterListener * *(..))")
    private void beanListenerUnregisterAnnotatedMethod() {
    }

    // Advice

    @After("channelListenerRegisterAnnotatedConstructor() || channelListenerRegisterAnnotatedMethod()")
    public void registerChannelListenerByRegisterAnnotation(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        ChannelListener channelListenerAnnotation = (ChannelListener) listener.getClass().getAnnotation(
                org.kasource.kaevent.listener.kenai.sadelf.annotations.impl.ChannelListener.class);
        if (channelListenerAnnotation.autoRegister() == false) {
            registerListenerHandler.registerChannelListener(channelListenerAnnotation, listener);
        }
    }

    @After("channelListenerConstructor()")
    public void registerChannelByAutoregister(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        ChannelListener channelListenerAnnotation = (ChannelListener) listener.getClass().getAnnotation(
                org.kasource.kaevent.listener.kenai.sadelf.annotations.impl.ChannelListener.class);
        if (channelListenerAnnotation.autoRegister() == true) {
            registerListenerHandler.registerChannelListener(channelListenerAnnotation, listener);
        }

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
                org.kasource.kaevent.listener.kenai.sadelf.annotations.impl.ChannelListener.class);
        registerListenerHandler.unregisterChannelListener(channelListenerAnnotation, listener);
    }

    @After("beanListenerRegisterAnnotatedConstructor() || beanListenerRegisterAnnotatedMethod()")
    public void registerBeanListenerByRegisterAnnotation(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        BeanListener beanListenerAnnotation = (BeanListener) listener.getClass().getAnnotation(
                org.kasource.kaevent.listener.kenai.sadelf.annotations.impl.BeanListener.class);

        if (beanListenerAnnotation.autoRegister() == false) {
            registerListenerHandler.registerBeanListener(beanListenerAnnotation, listener);
        }

    }

    @After("beanListenerConstructor()")
    public void registerBeanListenerByAutoregister(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        BeanListener beanListenerAnnotation = (BeanListener) listener.getClass().getAnnotation(
                org.kasource.kaevent.listener.kenai.sadelf.annotations.impl.BeanListener.class);
        if (beanListenerAnnotation.autoRegister() == true) {
            registerListenerHandler.registerBeanListener(beanListenerAnnotation, listener);
        }

    }

    @After("beanListenerUnregisterAnnotatedMethod()")
    public void unregisterBeanListener(JoinPoint thisJoinPoint) {
        Object listener = thisJoinPoint.getTarget();
        BeanListener beanListenerAnnotation = (BeanListener) listener.getClass().getAnnotation(
                org.kasource.kaevent.listener.kenai.sadelf.annotations.impl.BeanListener.class);
        registerListenerHandler.unregisterBeanListener(beanListenerAnnotation, listener);

    }

    public void initialize() {
        registerListenerHandler.setEventDispatcher(eventDispatcher);
        registerListenerHandler.initialize();
    }

    @Required
    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

}
