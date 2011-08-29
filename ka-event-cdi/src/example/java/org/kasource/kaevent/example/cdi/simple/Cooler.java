package org.kasource.kaevent.example.cdi.simple;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Cooler {
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}
}
