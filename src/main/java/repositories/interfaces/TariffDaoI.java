package repositories.interfaces;

import entities.Tariff;
import entities.TariffOption;
import repositories.interfaces.IGenericDAO;

import java.util.List;

public interface TariffDaoI extends IGenericDAO<Tariff> {

    List<String> getAllNames();

    boolean isNameExist(String name);

    Tariff findByName(String name);

    boolean isUsed(int id);
}
