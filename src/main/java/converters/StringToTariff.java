package converters;

import entities.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import services.TariffService;

@Component
public class StringToTariff implements Converter<String, Tariff> {

    @Autowired
    TariffService service;

    @Override
    public Tariff convert(String name){
        return service.findByName(name);
    }
}
