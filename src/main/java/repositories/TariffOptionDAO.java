package repositories;

import entities.TariffOption;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TariffOptionDAO extends GenericDAO<TariffOption> {
    public List<TariffOption> getIncompatibleOptions(int id) {
       return (List<TariffOption>)sessionFactory.getCurrentSession()
                .createNamedQuery("get_incompatible_options")
                .setParameter("id", id)
                .getResultList();

    }

    public List<TariffOption> getMandatoryOptions(int id) {
        return (List<TariffOption>)sessionFactory.getCurrentSession()
                .createNamedQuery("get_mandatory_options")
                .setParameter("id", id)
                .getResultList();
    }

    public boolean isNameExist(String name) {
        try {
            sessionFactory.getCurrentSession().createNamedQuery("is_name_exists")
                    .setParameter("name",name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public List<String> getAllNames() {
        return sessionFactory.getCurrentSession().createQuery("select o.name from TariffOption o")
                .getResultList();
    }

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

    public boolean isUsed(int id){
        /**
         * TODO
         */
        boolean isUsed=!sessionFactory.getCurrentSession()
                .createNamedQuery("is_option_used_in_Contract")
                .setParameter("id",id)
                .getResultList().isEmpty();

        boolean isUsed2=!sessionFactory.getCurrentSession()
                .createNamedQuery("is_option_used_in_Tariff")
                .setParameter("id",id)
                .getResultList().isEmpty();

        boolean isMandatory=!sessionFactory.getCurrentSession()
                .createNamedQuery("is_option_Mandatory")
                .setParameter("id",id)
                .getResultList().isEmpty();
        return (isUsed || isUsed2 || isMandatory);




        //return false;
    }
}
