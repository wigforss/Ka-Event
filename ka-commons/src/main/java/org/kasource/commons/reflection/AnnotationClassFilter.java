package org.kasource.commons.reflection;

import java.lang.annotation.Annotation;

/**
 * Filters classes by presence of a certain annotation
 * 
 * @author Rikard Wigforss
 **/
public class AnnotationClassFilter implements ClassFilter{

	private Class<? extends Annotation> annotation;
	
	
	public AnnotationClassFilter(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
		
	}
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isAnnotationPresent(annotation);
	}

}
