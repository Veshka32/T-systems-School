package validators;

import entities.TariffOption;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

@Component
public class OptionValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TariffOption.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TariffOption option=(TariffOption) o;
        Set<TariffOption> options=option.getIncompatibleOptions();
        if (options.contains(option))
            errors.rejectValue("incompatibleOptions","option.incompatibleOptions.invalid");
    }
}
