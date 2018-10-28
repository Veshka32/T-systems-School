package services;

import entities.Client;
import entities.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ContractDAO;

import javax.persistence.NoResultException;
import java.util.List;

@Service
@Transactional
public class ContractService {
    @Autowired
    ContractDAO  contractDAO;

    public Client findClientByPhone(long phone) throws NoResultException {
        return contractDAO.findClientByPhone(phone);
    }

    public Contract findByPhone(long phone){
        /**
         * todo
         */
        return null;
    }

    public Contract create(Contract contract){
        int id=contractDAO.save(contract);
        return contractDAO.findOne(id);
    }

    public void update(Contract contract) {
        contractDAO.update(contract);
    }

    public List<Contract> getAllClientContracts(int clientId) {
        return contractDAO.getClientContracts(clientId);
    }
    public List<Contract> getAll() {
        return contractDAO.findAll();
    }

}
