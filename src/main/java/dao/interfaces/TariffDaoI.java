package dao.interfaces;

import model.entity.Tariff;

import java.util.List;

public interface TariffDaoI extends IGenericDAO<Tariff> {

    List<String> getAllNames();

    Tariff findByName(String name);

    boolean isUsed(int id);
}
