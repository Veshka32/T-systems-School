package dao.implementations;

import dao.interfaces.ClientDaoI;
import model.entity.Client;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class ClientDAO extends GenericDAO<Client> implements ClientDaoI {

    @Override
    public Client findByPassportId(String passport){
        try {
            return sessionFactory.getCurrentSession().createNamedQuery("find_by_passport",Client.class)
            .setParameter("passport",passport)
            .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
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
