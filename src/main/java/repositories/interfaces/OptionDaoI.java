package repositories.interfaces;

import entities.Option;

import java.util.List;

public interface OptionDaoI extends IGenericDAO<Option> {

    List<String> getAllNames();

    Option findByName(String name);

    List<Option> findByNames(String[] names);

    boolean notUsed(int id);

    List<String> getAllMandatoryNames(String[] names);

    List<String> getAllIncompatibleNames(String[] names);

    List<String> getOptionsInTariffNames(int id);

    List<String> getMandatoryFor(int id);

}
