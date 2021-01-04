package de.codecentric.cxf.autodetection;


import de.codecentric.cxf.autodetection.diagnostics.CxfSpringBootMavenPropertiesNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PackageNameReaderTest {

    @Test
    public void read_Sei_Implementation_Package_From_cxfSpringBootMavenProperties_file() throws CxfSpringBootMavenPropertiesNotFoundException {

        String packageName = PackageNameReader.build().readSeiImplementationPackageNameFromCxfSpringBootMavenProperties();

        assertEquals(packageName, "de.codecentric");
    }

    @Test public void read_Sei_And_WebServiceClient_Package_From_cxfSpringBootMavenProperties_file() throws CxfSpringBootMavenPropertiesNotFoundException {

        String packageName = PackageNameReader.build().readSeiAndWebServiceClientPackageNameFromCxfSpringBootMavenProperties();

        assertEquals(packageName, "de.codecentric.namespace.weatherservice");
    }
}
