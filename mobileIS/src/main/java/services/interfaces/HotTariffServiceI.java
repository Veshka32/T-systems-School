package services.interfaces;


public interface HotTariffServiceI {
    int NUMBER_OF_HOT = 3;

    void pushHots();

    void pushIfHots(int tariffId);
}
