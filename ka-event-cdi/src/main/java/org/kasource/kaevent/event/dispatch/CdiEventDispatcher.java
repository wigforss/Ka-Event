package org.kasource.kaevent.event.dispatch;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.kasource.kaevent.config.CdiKaEventConfigurer;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;

@ApplicationScoped @Named("kaEvent.eventDispatcher")
public class CdiEventDispatcher extends DefaultEventDispatcher {

	/*@Inject
	private CdiKaEventConfigurer configurer;
	*/
	
	@Inject
	public CdiEventDispatcher(CdiKaEventConfigurer configurer) { 
		configurer.configure();
	}
	
	/*
	@PostConstruct	
	public void initialize() {
		System.out.println("Config");
		configurer.configure();
	}
	*/
	public void test() {}
	
}
