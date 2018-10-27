package repositories;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TariffDAO extends GenericDAO<Tariff>{
    public List<TariffOption> getOptions(int tariffId){
        return sessionFactory.getCurrentSession().createNamedQuery("get_options", TariffOption.class)
        .setParameter("id",tariffId)
        .getResultList();
    }

    public boolean isNameExist(String name) {
        try {
            sessionFactory.getCurrentSession().createNamedQuery("is_tariffName_exists")
                    .setParameter("name",name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public Tariff findByName(String name){
        try {
            return sessionFactory.getCurrentSession()
                    .createNamedQuery("find_tariff_by_name",Tariff.class)
                    .setParameter("name",name)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    public boolean isUsed(int id){
        return !sessionFactory.getCurrentSession()
                .createNamedQuery("is_used")
                .setParameter("id",id)
                .setMaxResults(1)
                .getResultList().isEmpty();
    }


}
