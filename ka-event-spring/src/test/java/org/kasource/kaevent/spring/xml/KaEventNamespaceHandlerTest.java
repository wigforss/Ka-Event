package org.kasource.kaevent.spring.xml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class KaEventNamespaceHandlerTest {

    @TestedObject
    private KaEventNamespaceHandler handler;
    
    @Test
    public void initTest() {
        handler.init();
    }
}
