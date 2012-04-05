package org.kasource.commons.reflection.classfilter;


/**
 * Composite filter that runs the filter list supplied in the constructor.
 * 
 * Use this class to combine filters.
 * 
 * @author rikard
 **/
public class FilterList implements ClassFilter{

	private ClassFilter[] filters;
	
	public FilterList(ClassFilter... filters) {
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
