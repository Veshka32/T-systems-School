package validators;

import entities.TariffOption;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OptionValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TariffOption.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        /**
         * TODO
         */
    }
}
