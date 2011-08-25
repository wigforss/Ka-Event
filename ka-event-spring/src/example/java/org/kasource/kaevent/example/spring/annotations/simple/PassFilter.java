package org.kasource.kaevent.example.spring.annotations.simple;

import java.util.EventObject;

import org.kasource.kaevent.event.filter.EventFilter;
import org.springframework.stereotype.Component;

///CLOVER:OFF
//CHECKSTYLE:OFF
@Component("passFilter")
public class PassFilter implements EventFilter<EventObject> {

    @Override
    public boolean passFilter(EventObject event) {
       System.out.println("Apply filter");
       return true;
    }

}
