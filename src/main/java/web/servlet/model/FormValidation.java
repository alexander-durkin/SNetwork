package web.servlet.model;

import java.util.HashMap;
import java.util.Map;

public class FormValidation {
    private final Map<String, FieldValidation> fields = new HashMap<>();
    private final Map<String, Boolean> errors = new HashMap<>();

    public Map<String, FieldValidation> getFields() {
        return fields;
    }

    public Map<String, Boolean> getErrors() {
        return errors;
    }

    public boolean isValid() {
        return fields.isEmpty() && errors.isEmpty();
    }
}
