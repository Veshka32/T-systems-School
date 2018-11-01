package repositories.implementations;

import entities.TariffOption;
import org.springframework.stereotype.Repository;
import repositories.implementations.GenericDAO;
import repositories.interfaces.TariffOptionDaoI;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TariffOptionDAO extends GenericDAO<TariffOption> implements TariffOptionDaoI {

    @Override
    public List<String> getAllNames() {
        return sessionFactory.getCurrentSession().createQuery("select o.name from TariffOption o",String.class)
                .getResultList();
    }

    @Override
    public TariffOption findByName(String name){
        try {
            return sessionFactory.getCurrentSession()
                    .createNamedQuery("find_by_name",TariffOption.class)
                    .setParameter("name",name)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<TariffOption> findByNames(String[] names){
            return sessionFactory.getCurrentSession()
                    .createNamedQuery("find_by_names",TariffOption.class)
                    .setParameterList("names",names)
                    .getResultList();
    }

    @Override
    public boolean isUsed(int id){
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

    public List<String> getAllMandatoryNames(String[] names){
         return sessionFactory.getCurrentSession().createNamedQuery("get_all_mandatory_names",String.class)
                .setParameterList("names",names)
                .getResultList();
    }

    public List<String> getAllIncompatibleNames(String[] names){
        return sessionFactory.getCurrentSession().createNamedQuery("get_all_incompatible_names",String.class)
                .setParameterList("names",names)
                .getResultList();
    }
}
