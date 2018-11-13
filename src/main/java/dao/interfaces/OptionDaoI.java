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

    /**
     * Get names of all mandatory options for specific option
     *
     * @param id
     * @return
     */
    List<String> getAllMandatoryNames(int id);

    /**
     * Get names of all incompatible  options for specific option
     *
     * @param id
     * @return
     */
    List<String> getAllIncompatibleNames(int id);

    List<String> getOptionsInTariffNames(int id);

    /**
     * get all option names which is mandatory for specific option
     *
     * @param id
     * @return
     */
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

    /**
     * Get all OptionRelation of type MANDATORY where OptionRelation.one name in specific range
     *
     * @param names
     * @return
     */

    List<OptionRelation> getMandatoryFor(String[] names);

}
