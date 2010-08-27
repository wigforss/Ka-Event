package org.kasource.kaevent.config;

import java.util.HashSet;
import java.util.Set;

public class KaEventInitializer {

	private static KaEventInitializer INSTANCE = new KaEventInitializer();
	private Set<KaEventInitializedListener> listeners = new HashSet<KaEventInitializedListener>();

	private KaEventConfiguration configuration;
	private KaEventInitializer() {}
	
	
	public static KaEventInitializer getInstance() {
		return INSTANCE;
	}
	
	public  void addListener(KaEventInitializedListener listener){
		if(configuration != null) {
            listener.doInitialize(configuration);
        } 
        listeners.add(listener);
	}

	public  void removeListener(KaEventInitializedListener listener){
		listeners.remove(listener);
	}
	
	static void  setConfiguration(KaEventConfiguration configuration) {
		INSTANCE.configuration = configuration;
		INSTANCE.notifyListeners();
	}
	
	private void notifyListeners() {
    	for(KaEventInitializedListener listener : listeners) {
            listener.doInitialize(configuration);
        }
    }

}