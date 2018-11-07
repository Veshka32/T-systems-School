package dao.implementations;

import dao.interfaces.ContractDaoI;
import entities.Client;
import entities.Contract;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class ContractDAO extends GenericDAO<Contract> implements ContractDaoI {
    @Override
    public List<Contract> getClientContracts(int clientId){
        return sessionFactory.getCurrentSession().createNamedQuery("get_client_contracts", Contract.class)
        .setParameter("clientId",clientId)
        .getResultList();
    }

    @Override
    public Client findClientByPhone(long phone)  {
        try {
            return sessionFactory.getCurrentSession().createNamedQuery("get_client_by_phone", Client.class)
                    .setParameter("phone",phone)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Contract findByPhone(long phone)  {
        try {
            return sessionFactory.getCurrentSession().createNamedQuery("get_contract_by_phone", Contract.class)
                    .setParameter("phone",phone)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }
}
