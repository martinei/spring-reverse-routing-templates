package de.martinei.routing.templatelinks;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.method.support.UriComponentsContributor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Map;


/**
 * Extended Version of CompositeUriComponentsContributor with support for Template Parameters
 */
public class TemplatingCompositeUriComponentsContributor extends CompositeUriComponentsContributor {

    public TemplatingCompositeUriComponentsContributor(UriComponentsContributor... contributors) {
        super(contributors);
    }

    public TemplatingCompositeUriComponentsContributor(Collection<?> contributors) {
        super(contributors);
    }

    public TemplatingCompositeUriComponentsContributor(Collection<?> contributors, ConversionService cs) {
        super(contributors, cs);
    }

    @Override
    public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {
        if (!TemplateParameters.currentParameters().isEmpty()) {

            if (!TemplateParameters.numberOfParametersWasChecked.get()) {
                System.out.println("Checking Number of Parameters!");
                int actualSize = TemplateParameters.currentParameters().size();
                TemplateParameters.numberOfParametersWasChecked.set(true);
                System.out.println("Actual Size " + actualSize + " Count: "+ parameter.getMethod().getParameterCount());
                if (actualSize != parameter.getMethod().getParameterCount()) {
                    TemplateParameters.currentParameters().clear();
                    throw new IllegalArgumentException("You need to use templateParam and valueParam for all Parameter!");
                };

            }

            TemplateParameters.TemplateParameter templParam = TemplateParameters.currentParameters().poll();

            if (templParam.isTemplated()) {
                super.contributeMethodArgument(parameter, "{"+templParam.getValue() + "}", builder, uriVariables, conversionService);
                return;
            }
        }
            super.contributeMethodArgument(parameter, value, builder, uriVariables,conversionService);
    }

}
