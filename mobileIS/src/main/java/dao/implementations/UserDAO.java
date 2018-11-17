package dao.implementations;

import dao.interfaces.UserDaoI;
import model.entity.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserDAO extends GenericDAO<Account> implements UserDaoI {

    @Override
    public Account findByLogin(String login) {
        try{return sessionFactory.getCurrentSession()
                .createNamedQuery("find_user_by_login", Account.class)
                .setParameter("login",login)
                .getSingleResult();
    } catch (NoResultException e){
            return null;
        }

}}
