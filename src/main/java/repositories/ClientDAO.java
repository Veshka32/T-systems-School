package repositories;

import entities.Client;
import entities.Contract;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class ClientDAO extends GenericDAO<Client>{
    public boolean isPassportExist(String id) {
        try {
            sessionFactory.getCurrentSession().createNamedQuery("is_passport_exists")
                    .setParameter("id",id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public boolean isEmailExists(String email) {
        try {
            sessionFactory.getCurrentSession().createNamedQuery("is_email_exists")
                    .setParameter("email",email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public Client findByPassportId(String passport){
        try {
            return sessionFactory.getCurrentSession().createNamedQuery("find_by_passport",Client.class)
            .setParameter("passport",passport)
            .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    public Client findByEmail(String email){
        try {
            return sessionFactory.getCurrentSession().createNamedQuery("find_by_email",Client.class)
                    .setParameter("email",email)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

}
