package validators;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Set;

@Component
public class TariffValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Tariff.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Tariff tariff=(Tariff)o;
        Set<TariffOption> tariffOptions=tariff.getOptions();

        for (TariffOption op:tariff.getIncompatibleOptions()
                ) {
            if (tariff.getOptions().contains(op))
                errors.rejectValue("incompatibleOptions","tariff.incompatibleOptions.intersection");
        }

        for (TariffOption option:tariffOptions) {
            List<TariffOption> bad=option.getIncompatibleTariffOptions();
            if (bad.stream().anyMatch(x->tariffOptions.contains(x)))
                errors.rejectValue("options","tariff.options.intersection");
        }

    }
}
