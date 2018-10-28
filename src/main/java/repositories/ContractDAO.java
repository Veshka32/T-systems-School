package repositories;

import entities.Client;
import entities.Contract;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class ContractDAO extends GenericDAO<Contract>{
    public List<Contract> getClientContracts(int clientId){
        return sessionFactory.getCurrentSession().createNamedQuery("get_client_contracts", Contract.class)
        .setParameter("clientId",clientId)
        .getResultList();
    }

    public Client findClientByPhone(long phone) throws NoResultException {
        return sessionFactory.getCurrentSession().createNamedQuery("get_client_by_phone", Client.class)
                .setParameter("phone",phone)
                .getSingleResult();
    }
}
