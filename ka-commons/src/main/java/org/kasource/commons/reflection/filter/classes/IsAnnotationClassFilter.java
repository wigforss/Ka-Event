package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only annotation classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsAnnotationClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isAnnotation();
	}
}
