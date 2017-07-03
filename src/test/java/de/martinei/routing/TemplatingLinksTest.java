package de.martinei.routing;

import de.martinei.routing.templatelinks.TemplatingCompositeUriComponentsContributor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import static de.martinei.routing.DummyServletRequest.createDummyServletRequest;
import static de.martinei.routing.templatelinks.TemplateParameters.templateParam;
import static de.martinei.routing.templatelinks.TemplateParameters.value;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TemplatingLinksTest.TestConfig.class)
public class TemplatingLinksTest {


    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        // Since MvcUriComponentBuilder fails to declare it dependencies explicitly but prefers to
        // look them up from ThreadLocals like ServletRequestHolder we need to
        // fake a request Cycle to test ist.
        createDummyServletRequest(wac);
    }

    @Test
    public void simpleValue() {
        String link =
                MvcUriComponentsBuilder.fromMethodCall(
                        MvcUriComponentsBuilder.on(TestController.class).oneParam(value(33l))).build().toUriString();
        assertEquals("http://localhost/OneParam/33", link);
    }

    @Test
    public void simpleTemplateLink() {

        String link =
                MvcUriComponentsBuilder.fromMethodCall(
                        MvcUriComponentsBuilder.on(TestController.class).oneParam(templateParam("id"))).build().toUriString();
        assertEquals("http://localhost/OneParam/{id}", link);
    }

    @Test
    public void simpleTemplateWithObjectParam() {

        String link =
                MvcUriComponentsBuilder.fromMethodCall(
                        MvcUriComponentsBuilder.on(TestController.class).objectTypedParam(templateParam("id"))).build().toUriString();
        assertEquals("http://localhost/objectParam/{id}", link);
    }

    @Test
    public void TemplatesAndValues() {
        String link =
                MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(TestController.class).
                        mixedTarget(
                                templateParam("id"),
                                value("5555"))
                ).build().toUriString();
        assertEquals("http://localhost/TwoParams/{id}/and/5555", link);
    }


    @Test(expected = IllegalArgumentException.class)
    public void useWithoutValueFails() {
        // We expect a Exception here telling the user to not mix plain params with templateParam
        String link =
                MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(TestController.class).
                        mixedTarget(
                                55l,
                                templateParam("second"))
                ).build().toUriString();
    }

    @Configuration
    public static class TestConfig extends WebMvcConfigurationSupport {
        @Bean
        @Primary
        public CompositeUriComponentsContributor mvcUriComponentsContributor(RequestMappingHandlerAdapter requestMappingHandlerAdapter,
                                                                             FormattingConversionService mvcConversionService) {
            return new TemplatingCompositeUriComponentsContributor(
                    requestMappingHandlerAdapter.getArgumentResolvers(), mvcConversionService);
        }
    }

}
