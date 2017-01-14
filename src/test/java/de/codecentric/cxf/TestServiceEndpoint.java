package de.codecentric.cxf;

import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.datatypes.ArrayOfForecast;
import de.codecentric.namespace.weatherservice.datatypes.Forecast;
import de.codecentric.namespace.weatherservice.datatypes.POP;
import de.codecentric.namespace.weatherservice.datatypes.Temp;
import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherInformationReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

public class TestServiceEndpoint implements WeatherService {

    private static final Logger LOG = LoggerFactory.getLogger(TestServiceEndpoint.class);
    private static de.codecentric.namespace.weatherservice.general.ObjectFactory objectFactoryGeneral = new de.codecentric.namespace.weatherservice.general.ObjectFactory();
    private static de.codecentric.namespace.weatherservice.datatypes.ObjectFactory objectFactoryDatatypes = new de.codecentric.namespace.weatherservice.datatypes.ObjectFactory();

    
    @Override
    public WeatherInformationReturn getWeatherInformation(String zip)
            throws WeatherException {
        LOG.info("Doing something useful :)");
        return null;
    }

    @Override
    public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest)
            throws WeatherException {
        LOG.info("Returning a forecast.");
        ForecastReturn forecastReturn = objectFactoryGeneral.createForecastReturn();
        forecastReturn.setCity("Weimar");
        //TODO: Map more fields
        forecastReturn.setState("Deutschland");
        forecastReturn.setSuccess(true);
        forecastReturn.setWeatherStationCity("Weimar");
        
        forecastReturn.setForecastResult(generateForecastResult(forecastReturn.getCity()));
        
        return forecastReturn;
    }

    @Override
    public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest)
            throws WeatherException {
        LOG.info("Doing something useful :)");
        return null;
    }
    
    private static ArrayOfForecast generateForecastResult(String city) {
        ArrayOfForecast forecastContainer = objectFactoryDatatypes.createArrayOfForecast();
        forecastContainer.getForecast().add(generateForecast(city));
        return forecastContainer;
    }


    private static Forecast generateForecast(String city) {
        Forecast forecast = objectFactoryDatatypes.createForecast();    
        forecast.setDate(generateCalendarFromNow());
        forecast.setDesciption("Vorhersage für " + city);
        forecast.setTemperatures(generateTemp());
        forecast.setProbabilityOfPrecipiation(generateRegenwahrscheinlichkeit());
        return forecast;
    }

    
    private static POP generateRegenwahrscheinlichkeit() {
        POP pop = objectFactoryDatatypes.createPOP();
        pop.setDaytime("22%");
        pop.setNighttime("5000%");
        return pop;
    }


    private static Temp generateTemp() {
        Temp temp = objectFactoryDatatypes.createTemp();
        temp.setDaytimeHigh("90°");
        temp.setMorningLow("0°");
        return temp;
    }


    private static XMLGregorianCalendar generateCalendarFromNow() {
        GregorianCalendar gregCal = GregorianCalendar.from(ZonedDateTime.now());
        XMLGregorianCalendar xmlGregCal = null;
        try {
            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal);
        } catch (DatatypeConfigurationException exception) {
            //LOG.calenderMappingNotWorking(exception);
        }
        return xmlGregCal;
    }

}
