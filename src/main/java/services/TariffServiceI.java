package services;

import entities.Tariff;
import entities.TariffOption;

import java.util.List;

public interface TariffServiceI {

    void create(String name);

    void delete(int id);

    Tariff get(int id);

    Tariff findByName(String name);

    List<Tariff> getAll();

    void addBaseOptions(int tariffId,int... optionId);

    void deleteBaseOptions(int tariffId,int... optionId);

    void updatePrice(int tariffId,int price);

    void setIncompatibleOptions(int tariffId, int... optionId);

    void removeIncompatibleOptions(int tariffId, int... optionId);

    void setIncompatibleTafiff(int tafiffId,int... tariff2Id);

    void removeIncompatibleTariff(int tariffId, int... tariff2Id);

    void archive(int tariffId);

}
