package converters;

import entities.TariffOption;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import services.OptionService;
import services.OptionServiceI;

import services.TariffServiceI;

@Component
public class StringToOption implements Converter<String, TariffOption> {

    @Autowired
    OptionServiceI optionService;

    @Override
    public TariffOption convert(String name){
        return optionService.findByName(name);
    }
}
