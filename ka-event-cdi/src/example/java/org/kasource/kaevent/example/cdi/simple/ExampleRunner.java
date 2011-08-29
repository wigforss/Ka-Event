package org.kasource.kaevent.example.cdi.simple;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class ExampleRunner {
	 public static void main(String[] args) {
		 WeldContainer weld = new Weld().initialize();
		 Thremometer thremometer = weld.instance().select(Thremometer.class).get();
		 thremometer.run();
		 
	 }
}
