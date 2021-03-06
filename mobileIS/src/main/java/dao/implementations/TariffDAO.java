package dao.implementations;

import dao.interfaces.TariffDaoI;
import model.entity.Tariff;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TariffDAO extends GenericDAO<Tariff> implements TariffDaoI {


    public List<Object[]> getAllNamesAndIds() {
        return sessionFactory.getCurrentSession().createQuery("select t.id,t.name from Tariff t", Object[].class).getResultList();
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
