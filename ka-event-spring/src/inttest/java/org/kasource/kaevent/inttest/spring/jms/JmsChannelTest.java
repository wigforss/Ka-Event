package org.kasource.kaevent.inttest.spring.jms;

import javax.annotation.Resource;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.EventDispatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:org/kasource/kaevent/inttest/spring/jms/jms-context.xml","classpath:org/kasource/kaevent/inttest/spring/jms/channel-context.xml"})
public class JmsChannelTest implements Runnable {

    @Resource
    private EventDispatcher eventDispatcher;
    
    @Resource
    private EventMessageListener listener;
    
    @Test
    public void sendMessage() throws InterruptedException {
        eventDispatcher.fire(new MyEvent("test"));
        Thread waitForListener = new Thread(this);
        waitForListener.start();
        waitForListener.join();
        assertEquals("test", listener.getSource());
    }
    
    public void run() {
        while(!listener.isMessageReceived()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
    }
}
