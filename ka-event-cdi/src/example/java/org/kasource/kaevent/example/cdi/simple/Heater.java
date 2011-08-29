package org.kasource.kaevent.example.cdi.simple;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Heater {
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}
}
