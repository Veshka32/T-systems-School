package repositories.interfaces;

import entities.TariffOption;
import repositories.interfaces.IGenericDAO;

import java.util.List;

public interface TariffOptionDaoI extends IGenericDAO<TariffOption> {

    List<String> getAllNames();

    TariffOption findByName(String name);

    List<TariffOption> findByNames(String[] names);

    boolean isUsed(int id);

    List<String> getAllMandatoryNames(String[] names);

    List<String> getAllIncompatibleNames(String[] names);

}
