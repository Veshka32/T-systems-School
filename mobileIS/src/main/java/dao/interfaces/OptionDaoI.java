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

    List<Option> getIncompatibleWithTariff(Integer[] ids, Integer[] tariffIds);

    /**
     * @param ids
     * @return Return list of incompatible options pairs with ids in specific range
     */
    List<OptionRelation> getIncompatibleRelationInRange(Integer[] ids);


    /**
     * Get all OptionRelation of type MANDATORY where OptionRelation.one id in specific range
     *
     * @param ids
     * @return
     */
    List<OptionRelation> getMandatoryRelation(Integer[] ids);
}
