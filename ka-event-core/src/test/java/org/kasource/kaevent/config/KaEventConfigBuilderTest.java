package org.kasource.kaevent.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.ChangeEvent;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.config.KaEventConfig;
import org.kasource.kaevent.config.KaEventConfigBuilder;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class KaEventConfigBuilderTest {

    @TestedObject
    private KaEventConfigBuilder builder;
    
    @Test
    public void emptyConfigBuild() {
        KaEventConfig config = builder.build();
  
        assertNull(config.getBeanResolver());
        assertNull(config.getChannelFactory());
        assertNull(config.getChannels());
        
        assertTrue(config.getEventQueue().isEmpty());
        assertNull(config.getEvents());
        
    }
    
    @Test
    public void addChannelTest() {
        builder.addChannel("testChannel");
        KaEventConfig config = builder.build();
        
        assertNull(config.getBeanResolver());
        assertNull(config.getChannelFactory());
        assertFalse(config.getChannels().getChannel().isEmpty());
        assertEquals(1, config.getChannels().getChannel().size());
        assertEquals("testChannel", config.getChannels().getChannel().get(0).getName());
        assertTrue(config.getEventQueue().isEmpty());
        assertTrue(config.getEvents().getEvent().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addNonAnnotatedWithoutBindingEvent() {
        builder.addEvent(ChangeEvent.class);
        builder.build();
    }
    
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void addUnboundAnnotatedEventTest() {
        builder.addEvent(UnboundAnnotatedEvent.class);
        builder.build();
    }
    
    @Test
    public void addEventTest() {
        builder.addEvent(AnnotatedEvent.class);
        KaEventConfig config = builder.build();
     
        assertNull(config.getBeanResolver());
        assertNull(config.getChannelFactory());
        assertNull(config.getChannels());
        
        assertTrue(config.getEventQueue().isEmpty());
        assertNotNull(config.getEvents());
        assertEquals(1, config.getEvents().getEvent().size());
        assertEquals(AnnotatedEvent.class.getName(), config.getEvents().getEvent().get(0).getEventClass());
        assertNull(config.getEvents().getEvent().get(0).getListenerInterface());
        assertNull(config.getEvents().getEvent().get(0).getAnnotation());
        assertEquals(AnnotatedEvent.class.getName(), config.getEvents().getEvent().get(0).getName());  
    }
    
    @Test
    public void addEventBindToInterfaceAndAnnotationTest() {
        builder.addEvent(MyEvent.class, MyEventListener.class, OnMyEvent.class);
        KaEventConfig config = builder.build();
     
        assertNull(config.getBeanResolver());
        assertNull(config.getChannelFactory());
        assertNull(config.getChannels());
        
        assertTrue(config.getEventQueue().isEmpty());
        assertNotNull(config.getEvents());
        assertEquals(1, config.getEvents().getEvent().size());
        assertEquals(MyEvent.class.getName(), config.getEvents().getEvent().get(0).getEventClass());
        assertEquals(MyEventListener.class.getName(), config.getEvents().getEvent().get(0).getListenerInterface());
        assertEquals(OnMyEvent.class.getName(), config.getEvents().getEvent().get(0).getAnnotation());
        assertEquals(MyEvent.class.getName(), config.getEvents().getEvent().get(0).getName());  
    }
    
    @Test
    public void addEventBoundToInterfaceTest() {
        builder.addEventBoundToInterface(MyEvent.class, MyEventListener.class);
        KaEventConfig config = builder.build();
     
        assertNull(config.getBeanResolver());
        assertNull(config.getChannelFactory());
        assertNull(config.getChannels());
        
        assertTrue(config.getEventQueue().isEmpty());
        assertNotNull(config.getEvents());
        assertEquals(1, config.getEvents().getEvent().size());
        assertEquals(MyEvent.class.getName(), config.getEvents().getEvent().get(0).getEventClass());
        assertEquals(MyEventListener.class.getName(), config.getEvents().getEvent().get(0).getListenerInterface());
        
        assertNull(config.getEvents().getEvent().get(0).getAnnotation());
        assertEquals(MyEvent.class.getName(), config.getEvents().getEvent().get(0).getName());  
    }
    
    @Test
    public void addEventBoundToAnnotationTest() {
        builder.addEventBoundToAnnotation(MyEvent.class, OnMyEvent.class);
        KaEventConfig config = builder.build();
     
        assertNull(config.getBeanResolver());
        assertNull(config.getChannelFactory());
        assertNull(config.getChannels());
        
        assertTrue(config.getEventQueue().isEmpty());
        assertNotNull(config.getEvents());
        assertEquals(1, config.getEvents().getEvent().size());
        assertEquals(MyEvent.class.getName(), config.getEvents().getEvent().get(0).getEventClass());
        assertNull(config.getEvents().getEvent().get(0).getListenerInterface());
        assertEquals(OnMyEvent.class.getName(), config.getEvents().getEvent().get(0).getAnnotation());
        assertEquals(MyEvent.class.getName(), config.getEvents().getEvent().get(0).getName());  
    }
    
    
    @SuppressWarnings("serial")
    @Event
    private static class UnboundAnnotatedEvent extends EventObject {

        public UnboundAnnotatedEvent(Object obj) {
            super(obj);
        }
        
    }
    
    
    @SuppressWarnings("serial")
    @Event(listener = AnnotatedEventListener.class)
    private static class AnnotatedEvent extends EventObject {

        public AnnotatedEvent(Object obj) {
            super(obj);
        }
        
    }
    
    private interface AnnotatedEventListener extends EventListener {
        public void onEvent(AnnotatedEvent event);
    }
    
    
    @SuppressWarnings("serial")
    private static class MyEvent extends EventObject {

        public MyEvent(Object obj) {
            super(obj);
        }
        
    }
    
    private interface MyEventListener extends EventListener {
        public void onEvent(MyEvent event);
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface OnMyEvent {
        
    }
}
