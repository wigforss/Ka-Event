package org.kasource.kaevent.bean;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class GuiceBeanResolver implements BeanResolver{

	@Inject
	private Injector injector;
	
	
	@Override
	public <T> T getBean(String beanName, Class<T> ofType) {
		Key<T> bindKey = Key.get(ofType, Names.named(beanName));
		return  injector.getInstance(bindKey);	
	}

}
