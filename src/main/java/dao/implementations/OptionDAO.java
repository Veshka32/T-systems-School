package dao.implementations;

import dao.interfaces.OptionDaoI;
import model.entity.Option;
import model.entity.OptionRelation;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class OptionDAO extends GenericDAO<Option> implements OptionDaoI {

    public List<Object[]> getAllNamesAndIds() {
        return sessionFactory.getCurrentSession().createQuery("select o.id,o.name from Option o", Object[].class).getResultList();
    }

    @Override
    public Option findByName(String name) {
        try {
            return sessionFactory.getCurrentSession()
                    .createNamedQuery("find_by_name", Option.class)
                    .setParameter("name",name)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
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
        return sessionFactory.getCurrentSession().createNamedQuery("get_mandatory_for", OptionRelation.class)
                .setParameterList("ids", ids)
                .getResultList();
    }

    @Override
    public List<Integer> getMandatoryIdsFor(Integer[] ids) {
        return sessionFactory.getCurrentSession().createNamedQuery("get_mandatory_ids", Integer.class)
                .setParameterList("ids", ids)
                .getResultList();
    }

    @Override
    public List<OptionRelation> getIncompatibleRelation(Integer[] ids) {
        return sessionFactory.getCurrentSession().createNamedQuery("get_incompatible_for", OptionRelation.class)
                .setParameterList("ids", ids)
                .getResultList();
    }

    @Override
    public List<Integer> getIncompatibleIdsFor(Integer[] ids) {
        List<Integer> oneSide = sessionFactory.getCurrentSession().createNamedQuery("get_incompatible_ids", Integer.class)
                .setParameterList("ids", ids)
                .getResultList();

        List<Integer> anotherSide = sessionFactory.getCurrentSession().createNamedQuery("get_incompatible_ids_1", Integer.class)
                .setParameterList("ids", ids)
                .getResultList();

        anotherSide.addAll(oneSide);
        return anotherSide;
    }


    @Override
    public List<String> getMandatoryNamesFor(int id) {
        return sessionFactory.getCurrentSession().createNamedQuery("get_mandatory_names", String.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<String> getIncompatibleNamesFor(int id) {
        List<String> oneSide = sessionFactory.getCurrentSession().createNamedQuery("get_incompatible_names", String.class)
                .setParameter("id", id)
                .getResultList();

        List<String> anotherSide = sessionFactory.getCurrentSession().createNamedQuery("get_incompatible_names_1", String.class)
                .setParameter("id", id)
                .getResultList();

        anotherSide.addAll(oneSide);
        return anotherSide;
    }

    @Override
    public List<Integer> getOptionsInTariffIds(int id) {
        return sessionFactory.getCurrentSession().createNamedQuery("get_option_in_tariff_ids", Integer.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<String> getOptionsInTariffNames(int id) {
        return sessionFactory.getCurrentSession().createNamedQuery("get_option_in_tariff_names", String.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<String> getNamesByIds(Integer[] ids) {
        return sessionFactory.getCurrentSession().createNamedQuery("get_names_by_ids", String.class)
                .setParameterList("ids", ids)
                .getResultList();
    }
}
