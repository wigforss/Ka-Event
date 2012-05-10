package org.kasource.kaevent.event.filter;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.sound.sampled.LineEvent;
import javax.swing.event.ChangeEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventFilterExecutorTest {
    
    @Mock
    private EventFilter filter;
    
    @Mock
    private EventFilter filter2;
    
    @TestedObject
    private EventFilterExecutor filterExecutor;
    
    
    
    @Test
    public void nullFilterTest() {
        ChangeEvent event = new ChangeEvent("test");
        assertTrue(filterExecutor.passFilters(null, event));
    }
    
    @Test
    public void noFilterTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        ChangeEvent event = new ChangeEvent("test");
        assertTrue(filterExecutor.passFilters(filters, event));
    }
  
    @Test
    public void oneFilterApplicableAndPassTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.handlesEvent()).andReturn(ChangeEvent.class);
        expect(filter.passFilter(event)).andReturn(true);
        EasyMockUnitils.replay();
        
        assertTrue(filterExecutor.passFilters(filters, event));
    }
    
    @Test
    public void oneFilterApplicableAndNotPassTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.handlesEvent()).andReturn(EventObject.class);
        expect(filter.passFilter(event)).andReturn(false);
        EasyMockUnitils.replay();
        
        assertFalse(filterExecutor.passFilters(filters, event));
    }
    
    @Test
    public void oneFilterNotApplicableTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.handlesEvent()).andReturn(LineEvent.class);
       
        EasyMockUnitils.replay();
        
        assertTrue(filterExecutor.passFilters(filters, event));
    }
    
    @Test
    public void twoFiltersApplicableAndPassTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        filters.add(filter2);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.handlesEvent()).andReturn(EventObject.class);
        expect(filter.passFilter(event)).andReturn(true);
        expect(filter2.handlesEvent()).andReturn(EventObject.class);
        expect(filter2.passFilter(event)).andReturn(true);
        EasyMockUnitils.replay();
        
        assertTrue(filterExecutor.passFilters(filters, event));
    }
    
    @Test
    public void twoFiltersApplicableAndSecondNotPassTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        filters.add(filter2);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.handlesEvent()).andReturn(EventObject.class);
        expect(filter.passFilter(event)).andReturn(true);
        expect(filter2.handlesEvent()).andReturn(EventObject.class);
        expect(filter2.passFilter(event)).andReturn(false);
        EasyMockUnitils.replay();
        
        assertFalse(filterExecutor.passFilters(filters, event));
    }

}
