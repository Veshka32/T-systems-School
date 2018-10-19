package converters;

import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import services.OptionServiceI;

@Component
public class OptionToString implements Converter<TariffOption,String> {

    @Override
    public String convert(TariffOption option){
    return option.getName();
}
}
