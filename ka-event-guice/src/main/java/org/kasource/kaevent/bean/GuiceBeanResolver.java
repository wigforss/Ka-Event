package org.kasource.kaevent.bean;

import java.util.Map;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

@Singleton
public class GuiceBeanResolver implements BeanResolver{

	@Inject
	private Injector injector;
	
	
	@Override
	public <T> T getBean(String beanName, Class<T> ofType) {
		Key<T> bindKey = Key.get(ofType, Names.named(beanName));
		try {
			return  injector.getInstance(bindKey);
		} catch(RuntimeException e) {
			Key<?> beanKey = getBeanKey(beanName);
			if(beanKey == null) {
				throw e;
			}
			return (T) injector.getInstance(beanKey);
		}
	}
	
	private Key<?> getBeanKey(String beanName) {
		Map<Key<?>, Binding<?>> bindings = injector.getBindings();
		for(Key<?> key : bindings.keySet()) {
			if(key.getAnnotationType() != null && key.getAnnotationType().equals(Named.class)) {
				Named named = (Named) key.getAnnotation();
				if(named.value().equals(beanName)) {
					return key;
				}
			}
		}
		return null;
	}

}
