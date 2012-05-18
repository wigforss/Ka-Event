package org.kasource.kaevent.config;

/**
 * Interface for listening to when, the ka-event environment has been configured and initialized.
 * 
 * @author rikardwigforss
 * @version $Id$
 */
public interface KaEventInitializedListener {
    
    /**
     * Do any initialization needed.
     * 
     * @param configuration The configuration.
     **/
    public void doInitialize(KaEventConfiguration configuration);
}
