package org.kasource.kaevent.bean;

import java.util.Map;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * Bean Resolver for Guice.
 * 
 * Will locate beans using the Guice Injector.
 * 
 * @author rikardwi
 **/
@Singleton
public class GuiceBeanResolver implements BeanResolver {

	@Inject
	private Injector injector;
	
	
	/**
	 * Returns bean from Guice.
	 * 
	 * @param <T> Class of the bean
	 * @param beanName Name of the bean to return.
	 * @param ofType Class of the bean.
	 * 
	 * @return Bean from the Guice Injector.
	 * 
	 * @throws CouldNotResolveBeanException if no bean named beanName can be found.
	 * @throws ClassCastException if the bean found could not be cast to T.
	 **/
	@SuppressWarnings("unchecked")
    @Override
	public <T> T getBean(String beanName, Class<T> ofType) throws CouldNotResolveBeanException, ClassCastException {
		Key<T> bindKey = Key.get(ofType, Names.named(beanName));
		try {
			return  injector.getInstance(bindKey);
		} catch (RuntimeException e) {
			Key<?> beanKey = getBeanKey(beanName);
			if (beanKey == null) {
				throw new CouldNotResolveBeanException("Could not find any bean named " + beanName);
			}
			return (T) injector.getInstance(beanKey);
		}
	}
	
	/**
	 * Returns the key for the bean with name beanName.
	 * 
	 * @param beanName Name of bean to find Key for.
	 * 
	 * @return Key for named bean or null if no Key found.
	 **/
	private Key<?> getBeanKey(String beanName) {
		Map<Key<?>, Binding<?>> bindings = injector.getBindings();
		for (Key<?> key : bindings.keySet()) {
			if (key.getAnnotationType() != null && key.getAnnotationType().equals(Named.class)) {
				Named named = (Named) key.getAnnotation();
				if (named.value().equals(beanName)) {
					return key;
				}
			}
		}
		return null;
	}

}
