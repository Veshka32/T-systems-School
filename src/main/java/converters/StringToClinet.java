package converters;

import entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import services.ClientServiceI;

@Component
public class StringToClinet implements Converter<String, Client> {

@Autowired
ClientServiceI clientServiceI;

    @Override
    public Client convert(String s) {
        return clientServiceI.get(Integer.parseInt(s));
    }
}
