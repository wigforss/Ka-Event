package org.kasource.kaevent.config;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides means to listen to when the ka-event environment has been configured and initialized.
 *  
 * @author rikardwi
 **/
public final class KaEventInitializer {

	private static final KaEventInitializer INSTANCE = new KaEventInitializer();
	private Set<KaEventInitializedListener> listeners = new HashSet<KaEventInitializedListener>();

	private KaEventConfiguration configuration;
	
	private KaEventInitializer() { }
	
	/**
	 * Provides static access to the singleton instance of this class.
	 * 
	 * @return The singleton instance.
	 **/
	public static KaEventInitializer getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Adds a listener. If the environment has already been configured
	 * the listener will be invoked instantly.
	 * 
	 * @param listener listener to add.
	 **/
	public  void addListener(KaEventInitializedListener listener) {
		if (configuration != null) {
            listener.doInitialize(configuration);
        } 
        listeners.add(listener);
	}

	/**
	 * Remove a listener.
	 * 
	 * @param listener	Listener to remove.
	 **/
	public  void removeListener(KaEventInitializedListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Invoked when the configuration has been done by the KaEventConfigurer class.
	 * 
	 * Will call all registered listeners.
	 * 
	 * @param configuration	The configuration.
	 **/
	static void  setConfiguration(KaEventConfiguration configuration) {
		INSTANCE.configuration = configuration;
		INSTANCE.notifyListeners();
	}
	
	/**
	 * Notifies (calls) all listeners.
	 **/
	private void notifyListeners() {
    	for (KaEventInitializedListener listener : listeners) {
            listener.doInitialize(configuration);
        }
    }

}
