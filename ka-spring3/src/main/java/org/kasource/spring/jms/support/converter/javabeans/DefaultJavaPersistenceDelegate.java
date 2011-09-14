package org.kasource.spring.jms.support.converter.javabeans;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.sql.Date;

public enum DefaultJavaPersistenceDelegate {
    SQL_DATE(Date.class, new PersistenceDelegate() {
        protected Expression instantiate(Object oldInstance, Encoder out) {
            return new Expression(oldInstance, oldInstance.getClass(), "new", new Object[] { Long.valueOf(
                    ((java.sql.Date) oldInstance).getTime()) });
        }
    });
    
    private Class<?> type;
    
    private PersistenceDelegate persistenceDelegate;
    
    DefaultJavaPersistenceDelegate(Class<?> type, PersistenceDelegate persistenceDelegate) {
        this.type = type;
        this.persistenceDelegate = persistenceDelegate;
    }

    /**
     * @return the type
     */
    protected Class<?> getType() {
        return type;
    }

    /**
     * @return the persistenceDelegate
     */
    protected PersistenceDelegate getPersistenceDelegate() {
        return persistenceDelegate;
    }
    
    
}
