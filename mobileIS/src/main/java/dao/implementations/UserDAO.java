package dao.implementations;

import dao.interfaces.UserDaoI;
import model.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserDAO extends GenericDAO<User> implements UserDaoI {

    @Override
    public User findByLogin(String login) {
        try{return sessionFactory.getCurrentSession()
                .createNamedQuery("find_user_by_login", User.class)
                .setParameter("login",login)
                .getSingleResult();
    } catch (NoResultException e){
            return null;
        }

}}
