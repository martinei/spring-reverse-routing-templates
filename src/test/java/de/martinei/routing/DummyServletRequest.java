package de.martinei.routing;


import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.servlet.DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE;


/**
 * Utils to create Dummy / Mock ServletRequests for the curren thead.
 * This is necessary since our Resource rely on {@link org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder}
 * which reads the current request from a Threadlocal (@link {@link RequestContextHolder})
 *
 */
public class DummyServletRequest {

    /**
     * Create a dummy Servlet Request and set it in {@link RequestContextHolder}
     */
    public static void createDummyServletRequest(WebApplicationContext context) {
        MockMvcRequestBuilders.get("/foo").buildRequest(context.getServletContext());
        HttpServletRequest req = new MockHttpServletRequest();
        HttpServletResponse resp = new MockHttpServletResponse();

        req.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        RequestAttributes attrs = new ServletRequestAttributes(req, resp);
        RequestContextHolder.setRequestAttributes(attrs);

    }

}