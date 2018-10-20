package converters;

import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import services.OptionServiceI;

@Component
public class StringToOption implements Converter<String, TariffOption> {

    @Autowired
    OptionServiceI optionService;

    @Override
    public TariffOption convert(String name){
        return optionService.findByName(name);
    }
}
