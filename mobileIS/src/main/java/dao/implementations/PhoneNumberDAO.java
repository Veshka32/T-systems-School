package dao.implementations;

import dao.interfaces.PhoneNumberDaoI;
import model.entity.NumberGenerator;
import org.hibernate.LockMode;
import org.springframework.stereotype.Repository;


@Repository
public class PhoneNumberDAO extends GenericDAO<NumberGenerator> implements PhoneNumberDaoI {

    @Override
    public NumberGenerator getForUpdate(int id) {
        return sessionFactory.getCurrentSession().get(NumberGenerator.class, id, LockMode.PESSIMISTIC_WRITE);
    }

}
