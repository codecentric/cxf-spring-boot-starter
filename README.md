SOAP-Webservices powered by Spring Boot & Apache CXF
=============================
[![Build Status](https://travis-ci.org/codecentric/cxf-spring-boot-starter.svg?branch=master)](https://travis-ci.org/codecentric/cxf-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/cxf-spring-boot-starter/badge.svg?branch=master)](https://coveralls.io/github/codecentric/cxf-spring-boot-starter?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.codecentric/cxf-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.codecentric/cxf-spring-boot-starter/)
[![Dependency Status](https://www.versioneye.com/user/projects/5720e300fcd19a0051856aa7/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5720e300fcd19a0051856aa7)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)


# Features include:

* Generating all necessary Java-Classes using JAX-B from your WSDL/XSDs (using the complementing Maven plugin [cxf-spring-boot-starter-maven-plugin]
* Booting up Apache CXF within Spring Context with 100% pure Java-Configuration
* Configure CXF to use slf4j and serve Logging-Interceptors, to log only the SOAP-Messages onto console 
* Extract the SoapMessages for processing in ELK-Stack, like [docker-elk](https://github.com/jonashackt/docker-elk)


# Documentation

### Initial Setup

* Create a Spring Boot maven project. It’s pretty much just using spring-boot-starter-parent as a parent und adding the Spring Boot build plugin. Then add cxf-spring-boot-starter as dependency and the [cxf-spring-boot-starter-maven-plugin] as build-plugin (see the example [cxf-boot-simple](https://github.com/jonashackt/cxf-boot-simple)):

```
<dependencies>
	<dependency>
		<groupId>de.codecentric</groupId>
    	<artifactId>cxf-spring-boot-starter</artifactId>
    	<version>1.0.5.RELEASE</version>
	</dependency>
</dependencies>
```


```
<build>
    <plugins>
        <plugin>
            <groupId>de.codecentric</groupId>
            <artifactId>cxf-spring-boot-starter-maven-plugin</artifactId>
            <version>1.0.5.RELEASE</version>
            <executions>
				<execution>
					<goals>
						<goal>generate</goal>
					</goals>
				</execution>
			</executions>
        </plugin>
    </plugins>
</build>
```



* place your .wsdl-File (and all the imported XSDs) into src/main/resources/wsdl
* run mvn generate-sources to generate all necessary Java-Classes from your WSDL/XSD 
* create a application.properties and optionally set the BaseURL of your Webservices via soap.service.base.url=/yourUrlHere

### Create a WebService Client

* If you instantiate a JaxWsProxyFactoryBean, you need to set an Adress containing your configured (or the standard) soap.service.base.url. To get the correct path, just autowire the CxfAutoConfiguration like:

``` 
@Autowired
private CxfAutoConfiguration cxfAutoConfiguration;
```

and obtain the base.url by calling

```
cxfAutoConfiguration.getBaseUrl()
```



### SOAP-Message-Logging

Activate via Property soap.messages.logging=true in application.properties.

### Extract the SoapMessages for processing in ELK-Stack

Activate via Property soap.messages.extract=true in application.properties.

### XML Schema Validation 

The standard behavior of Apache CXF with XML validation errors (non schema compliant XML or incorrect XML itself) is to return a SOAP fault including the corresponding exception in CXF:
```
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <soap:Fault>
         <faultcode>soap:Server</faultcode>
         <faultstring>wrong number of arguments while invoking public de.codecentric.namespace.weatherservice.general.ForecastRequest de.codecentric.cxf.WeatherServiceEndpoint.getCityForecastByZIP(de.codecentric.namespace.weatherservice.general.ForecastRequest) throws net.bipro.namespace.BiproException with params null.</faultstring>
      </soap:Fault>
   </soap:Body>
</soap:Envelope>
```
Many SOAP based standards demand a custom SOAP-Fault, that should be delivered in case of XML validation errors. To Implement that behavior, you have to:
* Implement the Interface [CustomFaultDetailBuilder](https://github.com/codecentric/cxf-spring-boot-starter/blob/master/src/main/java/de/codecentric/cxf/xmlvalidation/CustomFaultDetailBuilder.java) as Spring
[@Component](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/stereotype/Component.html) and return the JAX-B generated Object, that represents your WebService´ Fault-Details (be careful to take the right one, often the term 'Exception' is used twice...)
* Configure your Implementation as @Bean - only then, XML Schema Validation will be activated

# Todos:

* ASCII-Doc
* logstash-Configuration
* REST-Healthstatus-Endpoints, that check whether the soap-Services (dynamic?!) are available - using Swagger/Springfox
* kill the need for @ComponentScan("de.codecentric.cxf")

# Contribution

If you want to know more or even contribute to this Spring Boot Starter, maybe you need some information like:
* [SpringBoot Docs - Creating your own auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html)
* [Boot your own Infrastructure - JavaMagazin](https://public.centerdevice.de/a10fb484-49a8-4a70-ada9-5eeda8c69465)


[cxf-spring-boot-starter-maven-plugin]:https://github.com/codecentric/cxf-spring-boot-starter-maven-plugin


