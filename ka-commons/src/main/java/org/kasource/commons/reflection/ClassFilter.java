package org.kasource.commons.reflection;

/**
 * Filters classes
 * 
 * @author Rikard Wigforss
 **/
public interface ClassFilter {
	 boolean passFilter(Class<?> clazz);
}
