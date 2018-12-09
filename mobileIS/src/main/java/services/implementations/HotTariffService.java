/**
 * The {@code HotTariffService} implements {@code HotTariffServiceI} interface.
 * It is a service-layer class defining what tariffs consider as "hot" and how to represent them
 * <p>
 * This service send initial info about hot tariffs into jms queue immediately after bean is initialized
 * just in case some receivers are already waiting for it. Therefore, this bean depends on Jms Service initialization;
 *
 * @author Natalia Makarchuk
 */
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

    private static final int NUMBER_OF_HOT = 3; //define number of hot tariffs to send

    @Autowired
    JmsSenderI jmsSender;

    @Autowired
    TariffServiceI tariffService;

    @PostConstruct
    public void postConstruct() {
        pushHots();
    }

    /**
     * Send json representation of hot tariffs info into jms queue
     */
    @Override
    public void pushHots() {
        jmsSender.sendData(buildJson());
    }

    private String buildJson() {
        List<Tariff> tariffList = tariffService.getLast(NUMBER_OF_HOT);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(tariffList);
    }

    /**
     * Consider if specific tatiff is hot.
     * If true, send message to jms queue with fresh data about this tariff and other hot tariffs
     * @param tariffId if of tariff to consider
     */
    @Override
    public void pushIfHots(int tariffId) {
        List<Tariff> tariffList = tariffService.getLast(NUMBER_OF_HOT);
        Optional<Tariff> any = tariffList.stream().filter(t -> t.getId() == tariffId).findFirst();
        if (any.isPresent()) pushHots();
    }
}
