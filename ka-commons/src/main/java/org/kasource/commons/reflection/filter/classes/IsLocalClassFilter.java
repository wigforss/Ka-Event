package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only local classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsLocalClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isLocalClass();
	}
}
