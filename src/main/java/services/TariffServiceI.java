package services;

import entities.Tariff;

import java.util.List;

public interface TariffServiceI {

    int create(Tariff tariff);

    void delete(int id);

    Tariff get(int id);

    Tariff findByName(String name);

    List<Tariff> getAll();

    void update(Tariff tariff);

    void deleteOption(int tariffId, int optionId);

    void deleteIncompatibleOption(int tariffId, int optionId);

}
