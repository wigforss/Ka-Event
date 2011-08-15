package org.kasource.kaevent.listener.register;

import java.util.EventListener;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.ChannelListener;



public class RegisterListenerInterceptor implements MethodInterceptor {

	private RegisterListenerByAnnotation registerListenerHandler = RegisterListenerByAnnotationImpl.getInstance();
	
	private boolean doRegister;
	
	public RegisterListenerInterceptor(boolean doRegister) {
		this.doRegister = doRegister;
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("Interceptor: "+invocation.getThis());
		Object listener = invocation.getThis();
		@SuppressWarnings("unchecked")
		Class<? extends EventListener> listenerClass = (Class<? extends EventListener>) invocation.getMethod().getDeclaringClass();
		ChannelListener channelListener = listenerClass.getAnnotation(ChannelListener.class);
		if(channelListener != null) {
			if(doRegister) {
				registerListenerHandler.registerChannelListener(channelListener, listener);
			} else {
				registerListenerHandler.unregisterChannelListener(channelListener, listener);
			}
		}
		BeanListener beanListener = listenerClass.getAnnotation(BeanListener.class);
		if(beanListener != null) {
			if(doRegister) {
				registerListenerHandler.registerBeanListener(beanListener, listener);
			} else {
				registerListenerHandler.unregisterBeanListener(beanListener, listener);
			}
		}
		return invocation.proceed();
	}

	
}
