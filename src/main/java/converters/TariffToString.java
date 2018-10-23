package converters;

import entities.Tariff;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TariffToString implements Converter<Tariff,String> {

    @Override
    public String convert(Tariff tariff){
    return tariff.getName();
}
}
