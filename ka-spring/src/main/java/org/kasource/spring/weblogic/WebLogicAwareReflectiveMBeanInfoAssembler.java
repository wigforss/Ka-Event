package org.kasource.spring.weblogic;



import javax.management.Descriptor;

import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;


/**
 * Extend MetadataMBeanInfoAssembler with an extra descriptor which WebLogic WLFD requires.
 * 
 * Source code and usage examples found at: http://pauldone.blogspot.com/2010_02_01_archive.html
 * 
 * @author rikardwi
 **/
public class WebLogicAwareReflectiveMBeanInfoAssembler extends MetadataMBeanInfoAssembler {
    private static final String WLDF_MBEAN_TYPE_DESCPTR_KEY = "DiagnosticTypeName";
    private static final String NAME_MBEAN_DESCPTR_KEY = "name";
    private static final String MBEAN_KEYNAME_SUFFIX = "MBean";



    @Override
    protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey) {
        super.populateMBeanDescriptor(descriptor, managedBean, beanKey);
        descriptor.setField(WLDF_MBEAN_TYPE_DESCPTR_KEY, descriptor.getFieldValue(NAME_MBEAN_DESCPTR_KEY)
                + MBEAN_KEYNAME_SUFFIX);
    }
}
