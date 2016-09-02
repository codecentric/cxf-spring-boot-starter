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

* Create a Spring Boot maven project. It’s pretty much just using spring-boot-starter-parent as a parent und adding the Spring Boot build plugin. Then add cxf-spring-boot-starter as dependency and the [cxf-spring-boot-starter-maven-plugin] as build-plugin (see the example [cxf-boot-simple]):

```
<dependencies>
	<dependency>
		<groupId>de.codecentric</groupId>
    	<artifactId>cxf-spring-boot-starter</artifactId>
    	<version>1.0.6.RELEASE</version>
	</dependency>
</dependencies>
```


```
<build>
    <plugins>
        <plugin>
            <groupId>de.codecentric</groupId>
            <artifactId>cxf-spring-boot-starter-maven-plugin</artifactId>
            <version>1.0.7.RELEASE</version>
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

> only temporarily!

* due to logback / logstash-logback-encoder issue downgrade Spring Boot logback dependency (see [Extract the SoapMessages for processing in ELK-Stack](#extract-the-soapmessages-for-processing-in-elk-stack))

```
 <properties>
    <!-- Overriding SpringBoot 1.3.4ff´s logback 1.1.7, because of http://jira.qos.ch/browse/LOGBACK-1164 which is only fixed in the unreleased 1.1.8  -->
    <logback.version>1.1.6</logback.version>
</properties>
```

* place your .wsdl-File (and all the imported XSDs) into a folder somewhere under __src/main/resources__ (see [cxf-spring-boot-starter-maven-plugin] for details)
* run __mvn generate-sources__ to generate all necessary Java-Classes from your WSDL/XSD 


# Additional Configuration Options

### Customize URL, where your SOAP services are published

* create a application.properties and set the BaseURL of your Webservices via __soap.service.base.url=/yourUrlHere__


### Customize title of generated CXF-site

* place a __cxf.servicelist.title=Your custom title here__ in application.properties


### SOAP-Message-Logging

SOAP-Messages will be logged only and printed onto STOUT/Console for fast analysis in development.

Activate via Property __soap.messages.logging=true__ in application.properties.

### Extract the SoapMessages for processing in ELK-Stack

> If you want to use this Logging feature to extract the SoapMessages for processing in ELK-Stack, as a Workaround for now you have to stick to __Spring Boot 1.3.3.RELEASE__, due to [problems](https://github.com/logstash/logstash-logback-encoder/issues/160) in the [logstash-logback-encoder], which is itself a bug in [logback 1.1.7](http://jira.qos.ch/browse/LOGBACK-1164), which again is delivered with SpringBoot. This is only, till the SpringBoot guys [update to logback 1.1.8](https://github.com/spring-projects/spring-boot/issues/6214) - but this is not released [yet](https://mvnrepository.com/artifact/ch.qos.logback/logback-core).
If you want to use newer SpringBoot versions, you could try to override the logback-version with a 
```<logback.version>1.1.6</logback.version>``` in your Maven´s properties-tag.


The cxf-spring-boot-starter brings some nice features, you can use with an ELK-Stack to monitor your SOAP-Service-Calls:

* Extract SOAP-Service-Method for Loganalysis (based on WSDL 1.1 spec, 1.2 not supported for now - because this is read from the HTTP-Header field SoapAction, which isn´ mandatory in 1.2 any more)
* Dead simple Calltime-Logging
* Correlate all Log-Messages (Selfmade + ApacheCXFs SOAP-Messages) within the Scope of one Service-Consumer`s Call in Kibana via logback´s [MDC], placed in a Servlet-Filter

##### HowTo use

* Activate via Property __soap.messages.extract=true__ in application.properties
* Add a __logback-spring.xml__ file to __src/main/resources__ (otherwise the feature will not be activated) and configure the [logstash-logback-encoder] (which is delivered with this spring-boot-starter), like:

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml"/>
    <logger name="org.springframework" level="WARN"/>
    <!-- more logging config here -->
    
    
    <!-- Logstash-Configuration -->
	<!-- For details see https://github.com/logstash/logstash-logback-encoder/tree/logstash-logback-encoder-4.5 -->
	<appender name="logstash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
		<!-- You may want to configure a default instance, this could be done like
		<destination>${LOGANALYSIS_HOST:-192.168.99.100}:5000</destination> as discribed here:
		http://logback.qos.ch/manual/configuration.html#defaultValuesForVariables
		Set the SystemProperty with
		env LOGANALYSIS_HOST={{ loganalysis.host }}
		e.g. in a service.upstart.conf.j2, when using Ansible and deploying to Ubuntu -->
		<destination>192.168.99.100:5000</destination>
		<!-- encoder is required -->
	    <encoder class="net.logstash.logback.encoder.LogstashEncoder">
		   	<includeCallerData>true</includeCallerData>
		   	<customFields>{"service_name":"WeatherService_1.0"}</customFields>
		   	<fieldNames>
		   		<message>log-msg</message>
		   	</fieldNames>
	   	</encoder>
	   	<keepAliveDuration>5 minutes</keepAliveDuration>
	</appender>
	
	<root level="INFO">
	    <appender-ref ref="logstash" />
	</root>
</configuration>
```

* Now some fields will become available in your kibana dashboard (in other words in your elasticsearch index), e.g. soap-message-inbound contains the Inbound Message
* see all of them here [ElasticsearchField.java](https://github.com/codecentric/cxf-spring-boot-starter/blob/master/src/main/java/de/codecentric/cxf/logging/ElasticsearchField.java)

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
* Implement the Interface [CustomFaultBuilder](https://github.com/codecentric/cxf-spring-boot-starter/blob/master/src/main/java/de/codecentric/cxf/xmlvalidation/CustomFaultBuilder.java) as Spring
[@Component](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/stereotype/Component.html)
* Override Method createCustomFaultMessage(FaultType faultContent) an give back appropriate Messages you want to see in faultstring: <soap:Fault><faultstring>YOUR CUSTOM MESSAGE HERE</faultstring>
* Override Method createCustomFaultDetail(String originalFaultMessage, FaultType faultContent) and return the JAX-B generated Object, that represents your WebService´ Fault-Details (be really careful to take the right one!!, often the term 'Exception' is used twice... - e.g. with the [BiPro]-Services)
* Configure your Implementation as @Bean - only then, XML Schema Validation will be activated


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


# Dones:

* Support to use soap.messages.logging and soap.messages.extract at the same time
* Only activate SoapMessage extraction, if logback-spring.xml is present

# Todos:

* ASCII-Doc
* REST-Healthstatus-Endpoints, that check whether the soap-Services (dynamic?!) are available - using Swagger/Springfox
* kill the need for @ComponentScan("de.codecentric.cxf")
* make deps optional
* Support for WS-Security-Token (e.g. used in [BiPro]

# Contribution

If you want to know more or even contribute to this Spring Boot Starter, maybe you need some information like:
* [SpringBoot Docs - Creating your own auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html)
* [Boot your own Infrastructure - JavaMagazin](https://public.centerdevice.de/a10fb484-49a8-4a70-ada9-5eeda8c69465)



[cxf-spring-boot-starter-maven-plugin: set wsdl directory]:(https://github.com/codecentric/cxf-spring-boot-starter-maven-plugin#set-wsdl-directory-optional)
[cxf-spring-boot-starter-maven-plugin]:https://github.com/codecentric/cxf-spring-boot-starter-maven-plugin
[BiPro]:https://bipro.net
[logstash-logback-encoder]:https://github.com/logstash/logstash-logback-encoder
[MDC]:http://logback.qos.ch/manual/mdc.html
[cxf-boot-simple]:https://github.com/codecentric/spring-samples/tree/master/cxf-boot-simple


