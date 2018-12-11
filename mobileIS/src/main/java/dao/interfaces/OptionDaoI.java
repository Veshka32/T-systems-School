package dao.interfaces;

import model.entity.Option;
import model.entity.OptionRelation;

import java.util.List;
import java.util.Optional;

public interface OptionDaoI extends IGenericDAO<Option> {

    List<Object[]> getAllNamesAndIds();

    Optional<Option> findByName(String name);

    List<Option> findByIds(Integer[] ids);

    List<Option> findByNameLike(String name);

    boolean notUsed(int id);

    List<Option> getIncompatibleWithTariff(Integer[] ids, Integer[] tariffIds);

    List<OptionRelation> getIncompatibleRelation(int id);

    List<OptionRelation> getMandatoryRelation(Integer[] ids);

    List<OptionRelation> getMutuallyIncompatible(Integer[] ids);

}
