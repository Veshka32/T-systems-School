package dao.interfaces;

import entities.Tariff;

import java.util.List;

public interface TariffDaoI extends IGenericDAO<Tariff> {

    List<String> getAllNames();

    boolean isNameExist(String name);

    Tariff findByName(String name);

    boolean isUsed(int id);
}