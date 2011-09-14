package org.kasource.spring.jms.support.converter.jaxb;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() { }
    
    public Car createCar() {
        return new Car();
    }
}
