package services;

import entities.Tariff;

import java.util.List;

public interface TariffServiceI {

    void create(String name);

    void delete(int id);

    Tariff get(int id);

    Tariff findByName(String name);

    List<Tariff> getAll();

    void update(Tariff tariff);

    void deleteOption(int tariffId, int optionId);

    void deleteIncompatibleOption(int tariffId, int optionId);

}
