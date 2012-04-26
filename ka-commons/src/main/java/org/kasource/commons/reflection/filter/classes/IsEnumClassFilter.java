package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only enum classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsEnumClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isEnum();
	}
}
