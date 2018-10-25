package repositories;

import entities.Contract;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContractDAO extends GenericDAO<Contract>{
    public List<Contract> getClientContracts(int clientId){
        return sessionFactory.getCurrentSession().createNamedQuery("get_client_contracts", Contract.class)
        .setParameter("clientId",clientId)
        .getResultList();
    }

    public int findClientByPhone(long phone){
        return sessionFactory.getCurrentSession().createNamedQuery("get_client_by_phone",Integer.class)
                .setParameter("phone",phone)
                .getSingleResult();
    }
}
