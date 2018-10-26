package repositories;

import entities.TariffOption;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TariffOptionDAO extends GenericDAO<TariffOption> {
    public List<TariffOption> getIncompatibleOptions(int id) {
        return sessionFactory.getCurrentSession().createNativeQuery("select incompatibleOption_id from TariffOption t left join option_option o on t.id = o.option_id where t.id=2\n" +
                "union\n" +
                "select option_id from TariffOption t left join option_option o on t.id = o.incompatibleOption_id where t.id=2", TariffOption.class)
                .setParameter("optionId", id)
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
        return sessionFactory.getCurrentSession()
                .createNamedQuery("find_by_name",TariffOption.class)
                .setParameter("name",name)
                .getSingleResult();
    }
}
