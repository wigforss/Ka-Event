package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only array classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsArrayClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isArray();
	}
}
