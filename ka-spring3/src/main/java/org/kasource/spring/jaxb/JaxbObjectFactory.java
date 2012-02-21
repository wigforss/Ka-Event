
package org.kasource.spring.jaxb;



import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.core.io.Resource;



/**
 * Create object by unmarshalling a XML Resource using JAXB.
 * 
 * This class can be used directly in spring XML configuration like:
 * <pre>
 * <bean id="jaxbFactory" class="org.kasource.spring.jaxb.JaxbObjectFactory"/>
 *   
 *   <bean id="config" factory-bean="jaxbFactory" factory-method="unmarshalObject">
 *       <constructor-arg name="resource" value="file:${APP_HOME}/conf/config.xml"/>
 *       <constructor-arg name="packageName" value="org.my.app.conf"/><br/>
 *   </bean>
 * </pre>
 * 
 * The factory can also be use directly in code like:
 * <pre>
 * ApplicationContext ctx; // initialized elsewhere
 * JaxbObjectFactory factory = new JaxbObjectFactory();
 * org.my.app.conf.AppConfig config = (AppConfig) factory.unmarshalObject(ctx.getResource("file:/some/resource/path/myConfig.xml), org.my.app.conf.AppConfig.class.getPackage().getName());
 * </pre>
 * 
 * @author rikardwi
 **/
public class JaxbObjectFactory {

    /**
     * Unmarshall the object using JAXB from input resource and return it as an Object.
     * 
     * @param resource
     *            Resource of XML Content to unmarshall.
     * @param packageName
     *            Java Package name of JAXB classes representing the resource content.
     * 
     * @return The Unmarshalled object.
     * 
     * @throws IOException
     *             If resource does not have a readable InputStream.
     * @throws JAXBException
     *             If the resources could not be unmarshalled using classes from packageName.
     */
    public Object unmarshalObject(Resource resource, String packageName) throws IOException, JAXBException {
        InputStream is = resource.getInputStream();
        JAXBContext context = JAXBContext.newInstance(packageName);
        Object result = context.createUnmarshaller().unmarshal(is);
        is.close();
        return result;
    }

}
