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

    @Override
    public List<String> getAllNames() {
        return (List<String>)sessionFactory.getCurrentSession().createQuery("select o.name from TariffOption o")
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
    public boolean isUsed(int id){
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
    }
}
