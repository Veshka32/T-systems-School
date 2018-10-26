package repositories;

import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TariffDAO extends GenericDAO<Tariff>{
    public List<TariffOption> getOptions(int tariffId){
        return sessionFactory.getCurrentSession().createNamedQuery("get_options", TariffOption.class)
        .setParameter("id",tariffId)
        .getResultList();
    }
}
