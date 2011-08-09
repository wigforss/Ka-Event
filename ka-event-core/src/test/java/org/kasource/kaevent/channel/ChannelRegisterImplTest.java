/**
 * 
 */
package org.kasource.kaevent.channel;

import static org.junit.Assert.assertEquals;

import java.awt.event.WindowEvent;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ChannelRegisterImplTest {
    
    @Mock
    private ListenerChannel channel;
    
    @TestedObject
    private ChannelRegisterImpl register;

    
    @Test
    public void registerChannelTest() {
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        eventSet.add(ChangeEvent.class);
        EasyMock.expect(channel.getName()).andReturn("testChannel").times(2);
        EasyMock.expect(channel.getEvents()).andReturn(eventSet);
        EasyMockUnitils.replay();
        register.registerChannel(channel);
    }
    
    @Test
    public void registerEventTest() {
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        eventSet.add(ChangeEvent.class);
        EasyMock.expect(channel.getName()).andReturn("testChannel").times(2);
        EasyMock.expect(channel.getEvents()).andReturn(eventSet);
        channel.registerEvent(ChangeEvent.class);
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        register.registerChannel(channel);
        register.registerEvent("testChannel", ChangeEvent.class);
    }
    
    @Test(expected=NoSuchChannelException.class)
    public void registerEventToUnknownChannelTest() {
        register.registerEvent("testChannel", ChangeEvent.class);
    }
    
    
    @Test
    public void handleEventTest() {
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        eventSet.add(ChangeEvent.class);
        EasyMock.expect(channel.getName()).andReturn("testChannel").times(3);
        EasyMock.expect(channel.getEvents()).andReturn(eventSet);
        EasyMockUnitils.replay();
        register.registerChannel(channel);
        register.registerEventHandler(channel, WindowEvent.class);
    }
    
    @Test
    public void handleAlreadyHandledEventTest() {
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        eventSet.add(ChangeEvent.class);
        EasyMock.expect(channel.getName()).andReturn("testChannel").times(3);
        EasyMock.expect(channel.getEvents()).andReturn(eventSet);
        EasyMockUnitils.replay();
        register.registerChannel(channel);
        register.registerEventHandler(channel, ChangeEvent.class);
    }
    
    
    @Test(expected=NoSuchChannelException.class)
    public void handleEventUnknownChannelTest() {
        EasyMock.expect(channel.getName()).andReturn("testChannel").times(2);
        EasyMockUnitils.replay();
        register.registerEventHandler(channel, ChangeEvent.class);
    }
    
    @Test
    public void getChannelsByEventTest() {
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        eventSet.add(ChangeEvent.class);
        EasyMock.expect(channel.getName()).andReturn("testChannel").times(2);
        EasyMock.expect(channel.getEvents()).andReturn(eventSet);
        EasyMockUnitils.replay();
        register.registerChannel(channel);
        Set<Channel> channels = register.getChannelsByEvent(ChangeEvent.class);
        assertEquals(channel, channels.iterator().next());
    }
    
    @Test
    public void getChannelTest() {
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        EasyMock.expect(channel.getName()).andReturn("testChannel").times(2);
        EasyMock.expect(channel.getEvents()).andReturn(eventSet);
        EasyMockUnitils.replay();
        register.registerChannel(channel);
        assertEquals(register.getChannel("testChannel"), channel);
      
    }
    
    @Test(expected=NoSuchChannelException.class)
    public void getUnknownChannelTest() {
        register.getChannel("testChannel");
      
    }
}
