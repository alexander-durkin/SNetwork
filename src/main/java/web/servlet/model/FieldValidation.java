package web.servlet.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FieldValidation {
    boolean isEmptyField;
    boolean isIncorrect;
}
