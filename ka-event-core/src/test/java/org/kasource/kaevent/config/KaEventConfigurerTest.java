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
        configurer.addListener(new KaEventInitializedListener() {
            
            @Override
            public void doInitialize(KaEventConfiguration config) {
                assertNotNull(config.getBeanResolver());
                assertNotNull(config.getChannelFactory());
                assertNotNull(config.getChannelRegister());
                assertNotNull(config.getEventFactory());
                assertNotNull(config.getEventMethodinvoker());
                assertNotNull(config.getEventRegister());
                assertNotNull(config.getEventSender());
                assertNotNull(config.getQueueThread());
                assertNotNull(config.getSoListenerRegister());
                
            }
        });
        configurer.configure(eventDispatcher,null);
        
    }
    
    @Test
    public void configureTest() {
        configurer.addListener(new KaEventInitializedListener() {
            
            @Override
            public void doInitialize(KaEventConfiguration config) {
                assertNotNull(config.getBeanResolver());
                assertNotNull(config.getChannelFactory());
                assertNotNull(config.getChannelRegister());
                assertNotNull(config.getEventFactory());
                assertNotNull(config.getEventMethodinvoker());
                assertNotNull(config.getEventRegister());
                assertNotNull(config.getEventSender());
                assertNotNull(config.getQueueThread());
                assertNotNull(config.getSoListenerRegister());
                
            }
        });
        configurer.configure(eventDispatcher,"classpath:org/kasource/kaevent/config/simple-config.xml");
       
    }
    
    @Test
    public void configureAdvancedTest() {
        configurer.addListener(new KaEventInitializedListener() {
            @Override
            public void doInitialize(KaEventConfiguration config) {
                assertNotNull(config.getBeanResolver());
                assertNotNull(config.getChannelFactory());
                assertNotNull(config.getChannelRegister());
                assertNotNull(config.getEventFactory());
                assertNotNull(config.getEventMethodinvoker());
                assertNotNull(config.getEventRegister());
                assertNotNull(config.getEventSender());
                assertNotNull(config.getQueueThread());
                assertNotNull(config.getSoListenerRegister());
                
            }
        });
        configurer.configure(eventDispatcher,"classpath:org/kasource/kaevent/config/advanced-config.xml");
        
    }
    
}
