package repositories;

import entities.MyUser;
import entities.Tariff;
import entities.TariffOption;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDAO extends GenericDAO<MyUser>{

    public MyUser findByLogin(String login){
        try{return sessionFactory.getCurrentSession()
                .createNamedQuery("find_user_by_login",MyUser.class)
                .setParameter("login",login)
                .getSingleResult();
    } catch (NoResultException e){
            return null;
        }

}}
