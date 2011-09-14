package org.kasource.spring.jms.support.converter.javabeans;


import java.sql.Timestamp;

public enum DefaultJavaSerializationConstructor {
    SQL_TIMESTAMP (Timestamp.class, "time");
    
    
    private Class<?> type;
    private String[] constructorParameterNames;
    
    DefaultJavaSerializationConstructor(Class<?> type, String... constructorParameterName) {
        this.type = type;
        this.constructorParameterNames = constructorParameterName;
        
    }

    /**
     * @return the type
     */
    protected Class<?> getType() {
        return type;
    }

    /**
     * @return the constructorParameterNames
     */
    protected String[] getConstructorParameterNames() {
        return constructorParameterNames;
    }

    
    
}
