/**
 * 
 */
package org.kasource.commons.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author rikardwigforss
 *
 */
public class StringUtilsTest {

    @Test
    public void singleVariableReplacementTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        assertEquals("hello tstName", StringUtils.replaceVariables("hello ${name}", variables, false));
    }
    
    @Test
    public void singleVariableReplacementNullVariablesTest() {
        assertEquals("hello ${name}", StringUtils.replaceVariables("hello ${name}", null, false));
    }
    
    @Test
    public void singleVariableReplacementNullVariablesSystemValueTest() {
        System.setProperty("name2", "SystemName");
        assertEquals("hello SystemName", StringUtils.replaceVariables("hello ${name2}", null, false));
    }
    
    @Test
    public void twoVariableReplacementTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        variables.put("city", "New York");
        assertEquals("hello tstName I'm from New York", StringUtils.replaceVariables("hello ${name} I'm from ${city}", variables, false));
    }
    
    @Test
    public void twoVariableReplacementOneUnknownVariablesTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        assertEquals("hello tstName I'm from ${city}", StringUtils.replaceVariables("hello ${name} I'm from ${city}", variables, false));
    }
    
    @Test
    public void twoVariableReplacementOneSystemFallbackTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        System.setProperty("city", "Stockholm");
        assertEquals("hello tstName I'm from Stockholm", StringUtils.replaceVariables("hello ${name} I'm from ${city}", variables, false));
    }
    
    @Test
    public void twoVariableReplacementOneSystemFallbackCityTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        variables.put("city", "New York");
        System.setProperty("city", "Stockholm");
        assertEquals("hello tstName I'm from New York", StringUtils.replaceVariables("hello ${name} I'm from ${city}", variables, false));
    }
    
    @Test
    public void twoVariableReplacementOneEnvironmentFallbackTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        assertEquals("hello tstName I'm running Java "+System.getenv("JAVA_JVM_VERSION"), StringUtils.replaceVariables("hello ${name} I'm running Java ${JAVA_JVM_VERSION}", variables, false));
    }
    
    @Test
    public void twoVariableReplacementOneEnvironmentAndSystemFallbackTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        System.setProperty("JAVA_JVM_VERSION", "1.7.0");
        assertEquals("hello tstName I'm running Java 1.7.0", StringUtils.replaceVariables("hello ${name} I'm running Java ${JAVA_JVM_VERSION}", variables, false));
    }
    
    
    @Test
    public void twoVariableReplacementOneSystemOverrideTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        variables.put("city", "New York");
        System.setProperty("city", "Stockholm");
        assertEquals("hello tstName I'm from Stockholm", StringUtils.replaceVariables("hello ${name} I'm from ${city}", variables, true));
    }
    
    @Test
    public void twoVariableReplacementOneEnvironmentOverrideTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        variables.put("JAVA_JVM_VERSION", "1.5.0");
        Properties sysProps = System.getProperties();
        sysProps.remove("JAVA_JVM_VERSION");
        assertEquals("hello tstName I'm running Java "+System.getenv("JAVA_JVM_VERSION"), StringUtils.replaceVariables("hello ${name} I'm running Java ${JAVA_JVM_VERSION}", variables, true));
    }
    
    @Test
    public void twoVariableReplacementOneSystemAndEnvironmentOverrideTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "tstName");
        variables.put("JAVA_JVM_VERSION", "1.5.0");
        System.setProperty("JAVA_JVM_VERSION", "1.7.0");
        assertEquals("hello tstName I'm running Java 1.7.0", StringUtils.replaceVariables("hello ${name} I'm running Java ${JAVA_JVM_VERSION}", variables, true));
    }
}
