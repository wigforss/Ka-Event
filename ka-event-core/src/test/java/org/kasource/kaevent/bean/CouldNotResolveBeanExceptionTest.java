/**
 * 
 */
package org.kasource.kaevent.bean;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
/**
 * @author rikardwigforss
 *
 */
public class CouldNotResolveBeanExceptionTest {

    @Test
    public void createExceptionWithMessage() {
        CouldNotResolveBeanException e = new CouldNotResolveBeanException("test");
        assertEquals("test", e.getMessage());
    }
    
    @Test
    public void createExceptionWithException() {
        RuntimeException re = new RuntimeException();
        CouldNotResolveBeanException e = new CouldNotResolveBeanException(re);
        assertEquals(re, e.getCause());
    }
    
    @Test
    public void createExceptionWithMessageAndException() {
        RuntimeException re = new RuntimeException();
        CouldNotResolveBeanException e = new CouldNotResolveBeanException("test",re);
        assertEquals(re, e.getCause());
        assertEquals("test", e.getMessage());
    }
    
    
}
