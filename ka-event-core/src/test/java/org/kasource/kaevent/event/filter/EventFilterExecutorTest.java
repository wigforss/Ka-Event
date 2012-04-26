package org.kasource.kaevent.event.filter;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.event.ChangeEvent;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventFilterExecutorTest {
    
    @Mock
    private EventFilter<EventObject> filter;
    
    @Mock
    private EventFilter<EventObject> filter2;
    
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
        expect(filter.isApplicable(ChangeEvent.class)).andReturn(true);
        expect(filter.passFilter(event)).andReturn(true);
        EasyMockUnitils.replay();
        
        assertTrue(filterExecutor.passFilters(filters, event));
    }
    
    @Test
    public void oneFilterApplicableAndNotPassTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.isApplicable(ChangeEvent.class)).andReturn(true);
        expect(filter.passFilter(event)).andReturn(false);
        EasyMockUnitils.replay();
        
        assertFalse(filterExecutor.passFilters(filters, event));
    }
    
    @Test
    public void oneFilterNotApplicableTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.isApplicable(ChangeEvent.class)).andReturn(false);
       
        EasyMockUnitils.replay();
        
        assertTrue(filterExecutor.passFilters(filters, event));
    }
    
    @Test
    public void twoFiltersApplicableAndPassTest() {
        List<EventFilter<? extends EventObject>> filters = new ArrayList<EventFilter<? extends EventObject>>();
        filters.add(filter);
        filters.add(filter2);
        ChangeEvent event = new ChangeEvent("test");
        expect(filter.isApplicable(ChangeEvent.class)).andReturn(true);
        expect(filter.passFilter(event)).andReturn(true);
        expect(filter2.isApplicable(ChangeEvent.class)).andReturn(true);
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
        expect(filter.isApplicable(ChangeEvent.class)).andReturn(true);
        expect(filter.passFilter(event)).andReturn(true);
        expect(filter2.isApplicable(ChangeEvent.class)).andReturn(true);
        expect(filter2.passFilter(event)).andReturn(false);
        EasyMockUnitils.replay();
        
        assertFalse(filterExecutor.passFilters(filters, event));
    }

}
