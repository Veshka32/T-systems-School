package validators;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TariffValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Tariff.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Tariff tariff=(Tariff)o;

        for (TariffOption op:tariff.getIncompatibleOptions()
             ) {
            if (tariff.getOptions().contains(op))
                errors.rejectValue("options","tariff.options.intersection1");
        }

    }
}
