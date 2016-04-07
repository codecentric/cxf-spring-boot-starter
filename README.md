SOAP-Webservices powered by Spring Boot & Apache CXF
=============================
[![Build Status](https://travis-ci.org/jonashackt/cxf-spring-boot-starter.svg?branch=master)](https://travis-ci.org/jonashackt/cxf-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/github/jonashackt/cxf-spring-boot-starter/badge.svg?branch=master)](https://coveralls.io/github/jonashackt/cxf-spring-boot-starter?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/57061cdbfcd19a00415b0511/badge.svg?style=flat)](https://www.versioneye.com/user/projects/57061cdbfcd19a00415b0511)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)


### Features include:

* Generating all necessary Java-Classes using JAX-B from your WSDL/XSDs
* Booting up Apache CXF within Spring Context with 100% pure Java-Configuration
* Configure CXF to use slf4j and serve Logging-Interceptors, to log into ELK


### Documentation

* Create a Spring Boot maven project. It’s pretty much just using this spring-boot-starter as a parent:
```
<parent>
		<groupId>de.codecentric</groupId>
	    <artifactId>spring-boot-starter-cxf</artifactId>
	    <version>1.0.0-SNAPSHOT</version>
		<relativePath/> <!-- lookup parent from repository -->
</parent>
```

* place your .wsdl-File (and all the imported XSDs) into src/main/resources/wsdl
* run mvn generate-sources to generate all necessary Java-Classes from your WSDL/XSD 
* tbd


### Todos:

* ASCII-Doc