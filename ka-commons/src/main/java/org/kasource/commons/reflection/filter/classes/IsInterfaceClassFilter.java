package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only interface classes.
 * 
 * @author Rikard Wigforss
 **/
public class IsInterfaceClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isInterface();
	}
}
