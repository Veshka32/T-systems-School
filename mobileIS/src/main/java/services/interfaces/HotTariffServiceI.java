package services.interfaces;

import javax.annotation.PostConstruct;

public interface HotTariffServiceI {
    int NUMBER_OF_HOT = 3;

    @PostConstruct
    void postConstruct();

    void pushHots();

    void pushIfHots(int tariffId);
}
