package dao.implementations;

import dao.interfaces.TariffDaoI;
import entities.Tariff;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TariffDAO extends GenericDAO<Tariff> implements TariffDaoI {

    @Override
    public List<String> getAllNames() {
        return (List<String>)sessionFactory.getCurrentSession().createQuery("select t.name from Tariff t")
                .getResultList();
    }

    @Override
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

    @Override
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

    @Override
    public boolean isUsed(int id){
        return !sessionFactory.getCurrentSession()
                .createNamedQuery("is_used")
                .setParameter("id",id)
                .setMaxResults(1)
                .getResultList().isEmpty();
    }


}
