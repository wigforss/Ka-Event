package org.kasource.kaevent.example.cdi.simple;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.exceptions.DeploymentException;

public class ExampleRunner {
	 public static void main(String[] args) {
	    
	         WeldContainer weld = new Weld().initialize();
	         Thermometer config = weld.instance().select(Thermometer.class).get();
	         config.run();
	     
		
		 
	 }
	 
	
}
