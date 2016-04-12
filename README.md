SOAP-Webservices powered by Spring Boot & Apache CXF
=============================
[![Build Status](https://travis-ci.org/jonashackt/cxf-spring-boot-starter.svg?branch=master)](https://travis-ci.org/jonashackt/cxf-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/github/jonashackt/cxf-spring-boot-starter/badge.svg?branch=master)](https://coveralls.io/github/jonashackt/cxf-spring-boot-starter?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/57061cdbfcd19a00415b0511/badge.svg?style=flat)](https://www.versioneye.com/user/projects/57061cdbfcd19a00415b0511)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)


# Features include:

* Generating all necessary Java-Classes using JAX-B from your WSDL/XSDs (using the complementing parent-pom [cxf-spring-boot-starter-parent](https://github.com/jonashackt/cxf-spring-boot-starter-parent))
* Booting up Apache CXF within Spring Context with 100% pure Java-Configuration
* Configure CXF to use slf4j and serve Logging-Interceptors, to log only the SOAP-Messages onto console or directly into ELK, like [docker-elk](https://github.com/jonashackt/docker-elk)


# Documentation

### Initial Setup

* Create a Spring Boot maven project. It’s pretty much just using this [cxf-spring-boot-starter-parent](https://github.com/jonashackt/cxf-spring-boot-starter-parent) as a parent and adding cxf-spring-boot-starter as dependency (see the example [cxf-boot-simple](https://github.com/jonashackt/cxf-boot-simple))
```
<parent>
		<groupId>de.codecentric</groupId>
	    <artifactId>cxf-spring-boot-starter</artifactId>
	    <version>1.0.0-SNAPSHOT</version>
</parent>
```

* place your .wsdl-File (and all the imported XSDs) into src/main/resources/wsdl
* run mvn generate-sources to generate all necessary Java-Classes from your WSDL/XSD 

### XML Schema Validation 

* Implement the Interface [CustomFaultDetailBuilder](https://github.com/jonashackt/cxf-spring-boot-starter/blob/master/src/main/java/de/codecentric/cxf/xmlvalidation/CustomFaultDetailBuilder.java) and return the JAX-B generated Object, that represents your WebService´ Fault-Details (be careful to take the right one, often the term 'Exception' is used twice...)
* Configure your Implementation as @Bean - only then, XML Schema Validation will be activated

# Todos:

* ASCII-Doc

# Contribution

If you want to know more or even contribute to this Spring Boot Starter, maybe you need some information like:
*[SpringBoot Docs - Creating your own auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html}
*[Boot your own Infrastructure - JavaMagazin](https://public.centerdevice.de/a10fb484-49a8-4a70-ada9-5eeda8c69465)