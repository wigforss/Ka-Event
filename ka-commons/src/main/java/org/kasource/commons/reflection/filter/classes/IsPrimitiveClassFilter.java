package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only primitive classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsPrimitiveClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isPrimitive();
	}
}
