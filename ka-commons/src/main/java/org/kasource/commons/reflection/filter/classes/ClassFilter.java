package org.kasource.commons.reflection.filter.classes;

/**
 * Filters classes
 * 
 * @author Rikard Wigforss
 **/
public interface ClassFilter {
	 boolean passFilter(Class<?> clazz);
}
