package converters;

import entities.TariffOption;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OptionToString implements Converter<TariffOption,String> {

    @Override
    public String convert(TariffOption option){
    return option.getName();
}
}
