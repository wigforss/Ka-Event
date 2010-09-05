package org.kasource.commons.osgi;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.reflection.ClassFilter;
import org.kasource.commons.reflection.IsInterfaceClassFilter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;

public class OsgiUtils {

	private static final ClassFilter INTERFACE_FILTER = new IsInterfaceClassFilter();

	public static Set<ServiceReference> getServices(Bundle bundle,
			Set<String> serviceInterfaceClasses) {
		
		ServiceReference[] services = bundle.getRegisteredServices();
		Set<ServiceReference> matchingServices = new HashSet<ServiceReference>();

		for (ServiceReference serviceRef : services) {
			String[] interfaces = (String[]) serviceRef
					.getProperty(Constants.OBJECTCLASS);
			for (String interfaceName : interfaces) {
				if (serviceInterfaceClasses.contains(interfaceName)) {
					matchingServices.add(serviceRef);
				}
			}
		}
		return matchingServices;
	}

	public static Set<Class<?>> getInterfaces(BundleContext bundleContext,
			Bundle bundle) {
		return getClasses(bundleContext, bundle, INTERFACE_FILTER);
	}

	public static Set<Class<?>> getClasses(BundleContext bundleContext,
			Bundle bundle, ClassFilter filter) {
		Set<Class<?>> bundleClasses = new HashSet<Class<?>>();

		ServiceReference ref = bundleContext
				.getServiceReference(PackageAdmin.class.getName());
		PackageAdmin pa = (PackageAdmin) bundleContext.getService(ref);
		ExportedPackage[] packages = pa.getExportedPackages(bundle);
		if (packages != null) {
			for (ExportedPackage ePackage : packages) {
				String packageName = ePackage.getName();
				String packagePath = "/" + packageName.replace('.', '/');
				// find all the class files in current exported package
				Enumeration<?> classes = bundle.findEntries(packagePath,
						"*.class", false);
				if (classes != null) {
					while (classes.hasMoreElements()) {
						URL url = (URL) classes.nextElement();
						String path = url.getPath();
						String className = path.replace('/', '.').substring(1,
								path.length() - 6);
						try {
							
							Class<?> clazz = bundle.loadClass(className);
							if (filter.passFilter(clazz)) {
								bundleClasses.add(clazz);
							}
						} catch (NoClassDefFoundError be){
						} catch (ClassNotFoundException e) {						
						}
					}
				}
			}
		}
		bundleContext.ungetService(ref);
		return bundleClasses;
	}

}
