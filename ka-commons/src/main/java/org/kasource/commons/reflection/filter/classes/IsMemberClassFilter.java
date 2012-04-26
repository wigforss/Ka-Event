package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only member classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsMemberClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isMemberClass();
	}
}
