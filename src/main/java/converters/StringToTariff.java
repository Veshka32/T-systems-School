package converters;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import services.OptionServiceI;
import services.TariffServiceI;

@Component
public class StringToTariff implements Converter<String, Tariff> {

    @Autowired
    TariffServiceI service;

    @Override
    public Tariff convert(String name){
        return service.findByName(name);
    }
}
