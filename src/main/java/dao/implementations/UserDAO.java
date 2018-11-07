package dao.implementations;

import dao.interfaces.UserDaoI;
import entities.MyUser;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserDAO extends GenericDAO<MyUser> implements UserDaoI {

    @Override
    public MyUser findByLogin(String login){
        try{return sessionFactory.getCurrentSession()
                .createNamedQuery("find_user_by_login",MyUser.class)
                .setParameter("login",login)
                .getSingleResult();
    } catch (NoResultException e){
            return null;
        }

}}
