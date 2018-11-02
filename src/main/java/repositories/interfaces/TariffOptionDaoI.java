package repositories.interfaces;

import entities.TariffOption;

import java.util.List;

public interface TariffOptionDaoI extends IGenericDAO<TariffOption> {

    List<String> getAllNames();

    TariffOption findByName(String name);

    List<TariffOption> findByNames(String[] names);

    boolean notUsed(int id);

    List<String> getAllMandatoryNames(String[] names);

    List<String> getAllIncompatibleNames(String[] names);

}
