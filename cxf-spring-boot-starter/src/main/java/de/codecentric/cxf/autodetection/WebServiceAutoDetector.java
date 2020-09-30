package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.autodetection.diagnostics.SeiImplClassNotFoundException;
import de.codecentric.cxf.autodetection.diagnostics.SeiNotFoundException;
import de.codecentric.cxf.autodetection.diagnostics.WebServiceClientNotFoundException;
import de.codecentric.cxf.common.BootStarterCxfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;


@Component
public class WebServiceAutoDetector {

    private static final Logger LOG = LoggerFactory.getLogger(WebServiceAutoDetector.class);
    protected static final String NO_CLASS_FOUND = "No class found";
    private final WebServiceScanner webServiceScanner;
    private final ApplicationContext applicationContext;

    public static final Class<WebService> SEI_ANNOTATION = WebService.class;
    public static final Class<WebServiceClient> WEB_SERVICE_CLIENT_ANNOTATION = WebServiceClient.class;

    private final String seiAndWebServiceClientPackageName;
    private final String seiImplementationPackageName;

    public WebServiceAutoDetector(WebServiceScanner webServiceScanner, ApplicationContext applicationContext) throws BootStarterCxfException {
        this.webServiceScanner = webServiceScanner;
        this.applicationContext = applicationContext;
        seiAndWebServiceClientPackageName = PackageNameReader.build().readSeiAndWebServiceClientPackageNameFromCxfSpringBootMavenProperties();
        seiImplementationPackageName = PackageNameReader.build().readSeiImplementationPackageNameFromCxfSpringBootMavenProperties();
    }

    /**
     * Detects and instantiates the SEI-Implementation. Therefore it detects the SEI itself first.
     *
     * @param <T> returns the instantiated Service Endpoint Interface (SEI) implementation
     * @return    returns the instantiated Service Endpoint Interface (SEI) implementation
     * @throws BootStarterCxfException if the the SEI or it's implementation class wasn't found
     */
    public <T> T searchAndInstantiateSeiImplementation() throws BootStarterCxfException {
        return searchAndInstantiateSeiImplementation(searchServiceEndpointInterface());
    }

    @SuppressWarnings("unchecked")
    protected  <T> T searchAndInstantiateSeiImplementation(Class seiName) throws BootStarterCxfException {
        Class<T> implementingClass = null;
        try {
            implementingClass = webServiceScanner.scanForClassWhichImplementsAndPickFirst(seiName, seiImplementationPackageName);
            LOG.info("Found SEI implementing class: '{}'", implementingClass.getName());
        } catch (BootStarterCxfException exception) {
            throw SeiImplClassNotFoundException.build().setNotFoundClassName(seiName.getName()).setScannedBasePackage(seiImplementationPackageName);
        }
        return instantiateFromClass(implementingClass);
    }

    public Class searchServiceEndpointInterface() throws BootStarterCxfException {
        try{
            Class sei = webServiceScanner.scanForClassWithAnnotationAndIsAnInterface(SEI_ANNOTATION, seiAndWebServiceClientPackageName);
            LOG.info("Found Service Endpoint Interface (SEI): '{}'", sei.getName());
            return sei;
        } catch (BootStarterCxfException exception) {
            throw new SeiNotFoundException();
        }
    }

    @SuppressWarnings("unchecked")
    public Service searchAndInstantiateWebServiceClient() throws BootStarterCxfException {
        try{
            Class<Service> webServiceClientClass = webServiceScanner.scanForClassWithAnnotationAndPickTheFirstOneFound(WEB_SERVICE_CLIENT_ANNOTATION, seiAndWebServiceClientPackageName);
            LOG.info("Found WebServiceClient class: '{}'", webServiceClientClass.getName());
            return instantiateFromClass(webServiceClientClass);
        } catch (BootStarterCxfException exception) {
            throw new WebServiceClientNotFoundException();
        }
    }

    private <T> T instantiateFromClass(Class<T> clazz) throws BootStarterCxfException {
        try {
            return applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
        } catch (BeansException exception) {
            throw new BootStarterCxfException("Class couldn´t be instantiated", exception);
        }
    }

}
