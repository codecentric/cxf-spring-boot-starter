package de.codecentric.cxf.autodetection;


import de.codecentric.cxf.common.BootStarterCxfException;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PackageNameReaderTest {

    private PackageNameReader packageNameReader = new PackageNameReader();

    @Test public void
    read_Sei_Implementation_Package_From_cxfSpringBootMavenProperties_file() throws BootStarterCxfException {

        String packageName = packageNameReader.readSeiImplementationPackageNameFromCxfSpringBootMavenProperties();

        assertThat(packageName, is(equalTo("de.codecentric")));
    }

    @Test public void
    read_Sei_And_WebServiceClient_Package_From_cxfSpringBootMavenProperties_file() throws BootStarterCxfException {

        String packageName = packageNameReader.readSeiAndWebServiceClientPackageNameFromCxfSpringBootMavenProperties();

        assertThat(packageName, is(equalTo("de.codecentric.namespace.weatherservice")));
    }
}
