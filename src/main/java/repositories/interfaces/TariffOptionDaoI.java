package repositories.interfaces;

import entities.TariffOption;
import repositories.interfaces.IGenericDAO;

import java.util.List;

public interface TariffOptionDaoI extends IGenericDAO<TariffOption> {
    boolean isNameExist(String name);

    List<String> getAllNames();

    TariffOption findByName(String name);

    boolean isUsed(int id);
}
