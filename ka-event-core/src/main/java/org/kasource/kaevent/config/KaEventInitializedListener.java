package org.kasource.kaevent.config;

/**
 * Interface for listening to when, the ka-event environment has been configured and initialized.
 * 
 * @author rikardwigforss
 * @version $Id$
 */
public interface KaEventInitializedListener {
    public void doInitialize(KaEventConfiguration configuration);
}
