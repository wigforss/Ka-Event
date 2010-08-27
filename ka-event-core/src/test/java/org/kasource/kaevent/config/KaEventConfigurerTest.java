/**
 * 
 */
package org.kasource.kaevent.config;



import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.EventDispatcher;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author Rikard Wigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class KaEventConfigurerTest {

  
   @Mock
   private EventDispatcher eventDispatcher;
    
    @TestedObject
    private KaEventConfigurer configurer;
    

    @Test
    public void configureDefaultTest() {
        KaEventInitializer.getInstance().addListener(new KaEventInitializedListener() {
            
            @Override
            public void doInitialize(KaEventConfiguration config) {
                assertNotNull(config.getBeanResolver());
                assertNotNull(config.getChannelFactory());
                assertNotNull(config.getChannelRegister());
                assertNotNull(config.getEventFactory());
                assertNotNull(config.getEventMethodInvoker());
                assertNotNull(config.getEventRegister());
                assertNotNull(config.getEventSender());
                assertNotNull(config.getQueueThread());
                assertNotNull(config.getSourceObjectListenerRegister());
                
            }
        });
        configurer.configure(eventDispatcher,null);
        
    }
    
    @Test
    public void configureTest() {
    	KaEventInitializer.getInstance().addListener(new KaEventInitializedListener() {
            
            @Override
            public void doInitialize(KaEventConfiguration config) {
                assertNotNull(config.getBeanResolver());
                assertNotNull(config.getChannelFactory());
                assertNotNull(config.getChannelRegister());
                assertNotNull(config.getEventFactory());
                assertNotNull(config.getEventMethodInvoker());
                assertNotNull(config.getEventRegister());
                assertNotNull(config.getEventSender());
                assertNotNull(config.getQueueThread());
                assertNotNull(config.getSourceObjectListenerRegister());
                
            }
        });
        configurer.configure(eventDispatcher,"classpath:org/kasource/kaevent/config/simple-config.xml");
       
    }
    
    @Test
    public void configureAdvancedTest() {
    	KaEventInitializer.getInstance().addListener(new KaEventInitializedListener() {
            @Override
            public void doInitialize(KaEventConfiguration config) {
                assertNotNull(config.getBeanResolver());
                assertNotNull(config.getChannelFactory());
                assertNotNull(config.getChannelRegister());
                assertNotNull(config.getEventFactory());
                assertNotNull(config.getEventMethodInvoker());
                assertNotNull(config.getEventRegister());
                assertNotNull(config.getEventSender());
                assertNotNull(config.getQueueThread());
                assertNotNull(config.getSourceObjectListenerRegister());
                
            }
        });
        configurer.configure(eventDispatcher,"classpath:org/kasource/kaevent/config/advanced-config.xml");
        
    }
    
}
