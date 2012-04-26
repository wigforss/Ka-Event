package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only synthetic classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsSyntheticClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isSynthetic();
	}
}
