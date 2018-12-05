package dao.implementations;

import dao.interfaces.OptionDaoI;
import model.entity.Option;
import model.entity.OptionRelation;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class OptionDAO extends GenericDAO<Option> implements OptionDaoI {

    public List<Object[]> getAllNamesAndIds() {
        return sessionFactory.getCurrentSession().createQuery("select o.id,o.name from Option o", Object[].class).getResultList();
    }

    @Override
    public Optional<Option> findByName(String name) {
        try {
            return Optional.of(sessionFactory.getCurrentSession()
                    .createNamedQuery("find_by_name", Option.class)
                    .setParameter("name",name)
                    .getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Option> findByNameLike(String name) {

        return sessionFactory.getCurrentSession()
                .createNamedQuery("find_by_name_like", Option.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    @Override
    public List<Option> findByIds(Integer[] ids) {
        if (ids.length == 0) return Collections.emptyList();
        return sessionFactory.getCurrentSession()
                    .createNamedQuery("find_by_ids", Option.class)
                    .setParameterList("ids", ids)
                    .getResultList();
    }

    @Override
    public boolean notUsed(int id) {
        boolean notUsed=sessionFactory.getCurrentSession()
                .createNamedQuery("is_option_used_in_Contract")
                .setParameter("id",id)
                .setMaxResults(1)
                .getResultList().isEmpty();

        boolean notUsed1=sessionFactory.getCurrentSession()
                .createNamedQuery("is_option_used_in_Tariff")
                .setParameter("id",id)
                .setMaxResults(1)
                .getResultList().isEmpty();

        boolean notMandatory=sessionFactory.getCurrentSession()
                .createNamedQuery("is_option_Mandatory")
                .setParameter("id",id)
                .setMaxResults(1)
                .getResultList().isEmpty();
        return (notUsed && notUsed1 && notMandatory);
    }

    @Override
    public List<OptionRelation> getMandatoryRelation(Integer[] ids) {
        if (ids.length == 0) return Collections.emptyList();
        return sessionFactory.getCurrentSession().createNamedQuery("get_mandatory_for", OptionRelation.class)
                .setParameterList("ids", ids)
                .getResultList();
    }


    @Override
    public List<OptionRelation> getIncompatibleRelationInRange(Integer[] ids) {
        if (ids.length == 0) return Collections.emptyList();
        return sessionFactory.getCurrentSession().createNamedQuery("get_incompatible_for_in_range", OptionRelation.class)
                .setParameterList("ids", ids)
                .getResultList();
    }

    @Override
    public List<Option> getIncompatibleWithTariff(Integer[] ids, Integer[] tariffIds) {
        List<Option> oneSide = sessionFactory.getCurrentSession().createNamedQuery("get_incompatible", Option.class)
                .setParameterList("ids", ids)
                .setParameterList("ids2", tariffIds)
                .getResultList();

        List<Option> anotherSide = sessionFactory.getCurrentSession().createNamedQuery("get_incompatible", Option.class)
                .setParameterList("ids", tariffIds)
                .setParameterList("ids2", ids)
                .getResultList();

        anotherSide.addAll(oneSide);
        return anotherSide;
    }
}
