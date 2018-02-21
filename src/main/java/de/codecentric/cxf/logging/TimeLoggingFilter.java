package de.codecentric.cxf.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class TimeLoggingFilter extends GenericFilterBean{

    private static final String MDC_KEY = "time-calltime";
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeLoggingFilter.class);

    private BeanFactory beanFactory;

    public TimeLoggingFilter(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            populateTimingInformation();
        }
    }

    private void populateTimingInformation() {
        final Span currentSpan = tracer().getCurrentSpan();
        final long callTimeInMs = currentSpan.getAccumulatedMicros() / 1000;
        MDC.put(MDC_KEY, Long.toString(callTimeInMs));
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("Call time {}ms", callTimeInMs);
        }
    }

    Tracer tracer() {
        return this.beanFactory.getBean(Tracer.class);
    }
}
