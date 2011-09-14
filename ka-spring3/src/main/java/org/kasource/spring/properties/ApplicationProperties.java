package org.kasource.spring.properties;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Exposes application properties read by org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.
 * 
 * @author rikardwi
 **/
public class ApplicationProperties extends PropertyPlaceholderConfigurer implements InitializingBean {
    
    private Properties properties;
    private int systemPropertiesMode;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        setSystemPropertyMode();
        
    }
    
    private void setSystemPropertyMode() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = PropertyPlaceholderConfigurer.class.getDeclaredField("systemPropertiesMode");
        field.setAccessible(true);
        systemPropertiesMode = field.getInt(this);
    }
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            Properties mergedProps = mergeProperties();

            // Convert the merged properties, if necessary.
            convertProperties(mergedProps);

            // Let the subclass process the properties.
            processProperties(beanFactory, mergedProps);
            properties = mergedProps;
        } catch (IOException ex) {
            throw new BeanInitializationException("Could not load properties", ex);
        }
    }
    
    /**
     * Return the value for the property with key.
     * 
     * @param key   Property name.
     * 
     * @return value of property with name key or null if no such property exists.
     **/
    public String getProperty(String key) {
        return this.resolvePlaceholder(key, properties, systemPropertiesMode);
    }
    
    /**
     * Return the value for the property with key.
     * 
     * @param key   Property name.
     * @param defaultValue  The value to return if no property is found using key.
     * 
     * @return value of property with name key or defaultValue if no such property exists.
     **/
    public String getProperty(String key, String defaultValue) {
        String value = this.resolvePlaceholder(key, properties, systemPropertiesMode);
        if(value == null) {
            return  defaultValue;
        }
        return value;
    }
    
}
