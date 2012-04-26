package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only anonymous classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsAnonymousClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isAnonymousClass();
	}
}
