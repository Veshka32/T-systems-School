package services.implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.entity.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import services.interfaces.HotTariffServiceI;
import services.interfaces.JmsSenderI;
import services.interfaces.TariffServiceI;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@DependsOn("jmsSender")
public class HotTariffService implements HotTariffServiceI {

    @Autowired
    JmsSenderI jmsSender;

    @Autowired
    TariffServiceI tariffService;

    @PostConstruct
    public void postConstruct() {
        pushHots();
    }

    @Override
    public void pushHots() {
        jmsSender.sendData(buildJson());
    }

    private String buildJson() {
        List<Tariff> tariffList = tariffService.getLast(NUMBER_OF_HOT);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(tariffList);
    }

    @Override
    public void pushIfHots(int tariffId) {
        List<Tariff> tariffList = tariffService.getLast(NUMBER_OF_HOT);
        Optional<Tariff> any = tariffList.stream().filter(t -> t.getId() == tariffId).findFirst();
        if (any.isPresent()) pushHots();
    }
}
