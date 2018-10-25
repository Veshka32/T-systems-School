package repositories;

import entities.Contract;
import entities.TariffOption;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TariffOptionDAO extends GenericDAO<TariffOption>{
    public List<TariffOption> getIncompatibleOptions(int id){
        return sessionFactory.getCurrentSession().createNativeQuery("select incompatibleOption_id from TariffOption t left join option_option o on t.id = o.option_id where t.id=2\n" +
                "union\n" +
                "select option_id from TariffOption t left join option_option o on t.id = o.incompatibleOption_id where t.id=2", TariffOption.class)
        .setParameter("optionId",id)
        .getResultList();
    }
}
