package de.martinei.routing.templatelinks;

import java.util.LinkedList;
import java.util.Queue;

public class TemplateParameters {

    private static ThreadLocal<Queue<TemplateParameter>> parameters = ThreadLocal.withInitial(LinkedList::new);

    static ThreadLocal<Boolean> numberOfParametersWasChecked = ThreadLocal.withInitial(() -> false);

    static Queue<TemplateParameter> currentParameters() {
        return parameters.get();
    }

    public static <T> T templateParam(String name) {
        currentParameters().add(new TemplateParameter(true, name));
        TemplateParameters.numberOfParametersWasChecked.set(false);
        return null;
    }

    public static <T> T value(T value) {
        currentParameters().add(new TemplateParameter(false, value));
        TemplateParameters.numberOfParametersWasChecked.set(false);
        return value;
    }

    public static class TemplateParameter {
        private final boolean isTemplated;

        private final Object value;

        private TemplateParameter(boolean isTemplated, Object value) {
            this.isTemplated = isTemplated;
            this.value = value;
        }

        public boolean isTemplated() {
            return isTemplated;
        }

        public Object getValue() {
            return value;
        }
    };

}

