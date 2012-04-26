package org.kasource.commons.reflection.filter.classes;


/**
 * Composite filter that runs the filter list supplied in the constructor.
 * 
 * Use this class to combine filters.
 * 
 * @author rikard
 **/
public class ClassFilterList implements ClassFilter{

	private ClassFilter[] filters;
	
	public ClassFilterList(ClassFilter... filters) {
		this.filters = filters;
	}
	
	@Override
	public boolean passFilter(Class<?> clazz) {
		for(ClassFilter filter : filters) {
			if(!filter.passFilter(clazz)) {
				return false;
			}
		}
		return true;
	}

}
