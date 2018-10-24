package services;

import entities.Contract;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ContractDAO;
import repositories.GenericDAO;
import repositories.IGenericDAO;

import java.util.List;

@Service
@Transactional
public class ContractService {
    @Autowired
    ContractDAO  contractDAO;

    public Contract findByPhone(int phone) {
        return contractDAO.findByNaturalId(phone);
    }

    public Contract create(Contract contract){
        int id=contractDAO.create(contract);
        return contractDAO.findOne(id);
    }

    public void update(Contract contract) {
        contractDAO.update(contract);
    }

    public List<Contract> getAllClientContracts(int clientId) {
        return contractDAO.getClientContracts(clientId);
    }
}
