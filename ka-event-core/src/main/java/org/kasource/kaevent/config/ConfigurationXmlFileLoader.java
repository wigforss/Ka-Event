package org.kasource.kaevent.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.kasource.commons.util.StringUtils;

/**
 * Loads the XML Configuration from file location path.
 * 
 * @author rikardwi
 **/
public class ConfigurationXmlFileLoader {

    /**
     * Constructor. 
     */
    ConfigurationXmlFileLoader() { }
    
    /**
     * Loads a configuration from file or classpath location and returns it.
     * 
     * @param inPath
     *            Location of the XML Configuration file.
     * 
     * @return The Configuration created from the XML file.
     * @throws IllegalArgumentException
     *             if no XML file could be found or the XML could not be unmarshalled.
     **/
     KaEventConfig loadXmlFromPath(String inPath) throws IllegalArgumentException {
      
        String path = getPath(inPath);
       
        try {
            InputStream xmlStream = getXmlStream(path, inPath.startsWith("file:"));
            KaEventConfig xmlConfig = loadXmlConfig(xmlStream);
            if (xmlConfig == null) {
                throw new IllegalArgumentException("Could not unmarshal xml configuration file " 
                            + inPath);
            }
            return xmlConfig;
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Could not parse xml configuration file " + inPath, e);
        } 
        

    }
    
    /**
     * Returns the Input Stream for path. 
     * 
     * @param path file or classpath to configuration XML File.
     * @param fromFile true load XML from file, else false.
     * @return Input Stream from path.
     * 
     * @throws IllegalArgumentException If XML file could not be found.
     **/
    private InputStream getXmlStream(String path, boolean fromFile) throws IllegalArgumentException {
        InputStream xmlStream = null;
        if (!fromFile) {
            xmlStream = KaEventConfigurer.class.getClassLoader().getResourceAsStream(path);
            if (xmlStream == null) {
                throw new IllegalArgumentException("Could not load xml configuration file " + path
                            + " from class path");
            }
        } else {
            try {
                xmlStream = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Could not load xml configuration file " + path, e);
            }
        }
        return xmlStream;
    }
    
    /**
     * Remove prefix and returns the path to the file.
     * 
     * @param inPath path to use.
     * 
     * @return path to use.
     */
    private String getPath(String inPath) {
        String path = inPath;
        if (path.startsWith("classpath:")) {
            path = path.substring("classpath:".length());
        } else if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }
        path = StringUtils.replaceVariables(path, null, true);
        return path;
    }

    /**
     * Helper to loadXmlFromPath, loads the configuration from an InputStream.
     * 
     * @param istrm
     *            InputStream to load configuration from.
     * 
     * @return The configuration.
     * 
     * @throws JAXBException
     *             If the XML file (instrn) could not be unmarshalled.
     **/
    private KaEventConfig loadXmlConfig(InputStream istrm) throws JAXBException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(KaEventConfig.class.getPackage().getName());

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
       
            Object object = unmarshaller.unmarshal(istrm);
            return (KaEventConfig) object;
        } finally {
            closeStream(istrm);
        }

    }

    /**
     * Closes the InputStream.
     * 
     * @param istrm InputStream to clase.
     **/
    private void closeStream(InputStream istrm) {
        try {
            istrm.close();
        } catch (IOException e) {
           return;
        }
    }
}
