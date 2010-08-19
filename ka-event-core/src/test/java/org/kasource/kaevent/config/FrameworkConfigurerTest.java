/**
 * 
 */
package org.kasource.kaevent.config;



import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author Rikard Wigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class FrameworkConfigurerTest {

  
   
    @TestedObject
    private FrameworkConfigurer configurer;
    

    @Test
    public void configureDefaultTest() {

        FrameworkConfiguration config = configurer.configure(null);
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
    
    @Test
    public void configureTest() {

        FrameworkConfiguration config = configurer.configure("classpath:org/kasource/kaevent/config/simple-config.xml");
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
    
    @Test
    public void configureAdvancedTest() {

        FrameworkConfiguration config = configurer.configure("classpath:org/kasource/kaevent/config/advanced-config.xml");
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
}
