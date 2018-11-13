package dao.interfaces;

import model.entity.Option;
import model.entity.OptionRelation;

import java.util.List;

public interface OptionDaoI extends IGenericDAO<Option> {

    List<String> getAllNames();

    Option findByName(String name);

    List<Option> findByNames(String[] names);

    List<Option> findByNameLike(String name);

    boolean notUsed(int id);

    List<String> getAllMandatoryNames(String[] names);

    List<String> getAllIncompatibleNames(String[] names);

    List<String> getOptionsInTariffNames(int id);

    List<String> getMandatoryFor(int id);

    /**
     * Return sub-list from option names such as option with specific id is mandatory for them
     *
     * @param id
     * @param names
     * @return
     */
    List<String> isMandatoryFor(int id, String[] names);

    /**
     * @param names
     * @return Return list of incompatible options pairs from names
     */
    List<OptionRelation> getIncompatibleFor(String[] names);

}
