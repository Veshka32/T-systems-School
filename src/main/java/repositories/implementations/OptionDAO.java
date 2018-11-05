package repositories.implementations;

import entities.Option;
import org.springframework.stereotype.Repository;
import repositories.interfaces.OptionDaoI;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class OptionDAO extends GenericDAO<Option> implements OptionDaoI {

    private static final String NAMES = "names";

    @Override
    public List<String> getAllNames() {
        return sessionFactory.getCurrentSession().createQuery("select o.name from Option o", String.class)
                .getResultList();
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
    public List<Option> findByNames(String[] names) {
            return sessionFactory.getCurrentSession()
                    .createNamedQuery("find_by_names", Option.class)
                    .setParameterList(NAMES, names)
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
    public List<String> getAllMandatoryNames(String[] names){
        return sessionFactory.getCurrentSession().createNamedQuery("get_all_mandatory_names", String.class)
                .setParameterList(NAMES, names)
                .getResultList();
    }

    @Override
    public List<String> getAllIncompatibleNames(String[] names){
        return sessionFactory.getCurrentSession().createNamedQuery("get_all_incompatible_names",String.class)
                .setParameterList(NAMES, names)
                .getResultList();
    }

    @Override
    public List<String> getOptionsInTariffNames(int id) {
        return sessionFactory.getCurrentSession().createNamedQuery("get_option_in_tariff_names", String.class)
                .setParameter("id", id)
                .getResultList();
    }
}
