package validators;

import entities.Contract;
import entities.TariffOption;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

@Component
public class ContractValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Contract.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Contract contract=(Contract) o;


            //errors.rejectValue("incompatibleOptions","option.incompatibleOptions.invalid");
    }
}
