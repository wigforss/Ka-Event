package org.kasource.commons.reflection.classfilter;

/**
 * Filters classes
 * 
 * @author Rikard Wigforss
 **/
public interface ClassFilter {
	 boolean passFilter(Class<?> clazz);
}
