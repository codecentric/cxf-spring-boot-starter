package de.codecentric.cxf.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class TimeLoggingFilter extends GenericFilterBean{

    private static final String MDC_KEY = "time-calltime";
    private static final Logger LOG = LoggerFactory.getLogger(TimeLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startMillis = System.currentTimeMillis();
        try {
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            populateTimingInformation(startMillis);
        }
    }

    private void populateTimingInformation(long startMillis) {

        final long callTimeInMs = System.currentTimeMillis() - startMillis;
        MDC.put(MDC_KEY, Long.toString(callTimeInMs));
        if(LOG.isInfoEnabled()){
            LOG.info("Call time {}ms", callTimeInMs);
        }
    }

}
