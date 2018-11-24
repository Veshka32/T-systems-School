package dao.interfaces;

import model.entity.Option;
import model.entity.OptionRelation;

import java.util.List;

public interface OptionDaoI extends IGenericDAO<Option> {

    List<Object[]> getAllNamesAndIds();

    Option findByName(String name);

    List<Option> findByIds(Integer[] ids);

    List<Option> findByNameLike(String name);

    boolean notUsed(int id);

    /**
     * get ids of options in specific tariff
     *
     * @param id
     * @return
     */
    List<Integer> getOptionsInTariffIds(int id);

    /**
     * get names of options in specific tariff
     *
     * @param id
     * @return
     */
    List<String> getOptionsInTariffNames(int id);

    /**
     * get all option names which is mandatory for specific option
     *
     * @param id
     * @return
     */
    List<String> getMandatoryNamesFor(int id);

    /**
     * get all option names which is incompatible with specific option
     *
     * @param id
     * @return
     */
    List<String> getIncompatibleNamesFor(int id);


    /**
     * @param ids
     * @return Return list of incompatible options pairs with ids in specific range
     */
    List<OptionRelation> getIncompatibleRelation(Integer[] ids);

    List<Integer> getIncompatibleIdsFor(Integer[] ids);

    /**
     * Get all OptionRelation of type MANDATORY where OptionRelation.one id in specific range
     *
     * @param ids
     * @return
     */
    List<OptionRelation> getMandatoryRelation(Integer[] ids);

    List<Integer> getMandatoryIdsFor(Integer[] ids);

    List<String> getMandatoryForInRange(Integer[] ids, int id);

    List<String> getNamesByIds(Integer[] ids);

}
