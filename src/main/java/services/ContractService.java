package services;

import entities.Contract;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.GenericDAO;
import repositories.IGenericDAO;

@Service
public class ContractService implements ContractServiceI {
    IGenericDAO<Contract>  contractDAO;

    @Autowired
    public void setOptionDAO(GenericDAO<Contract> optionDAO ) {
        this.contractDAO=optionDAO;
        optionDAO.setClass(Contract.class);
    }

    @Override
    @Transactional
    public Contract findByPhone(int phone) {
        return contractDAO.findByNaturalId(phone);
    }

    @Override
    @Transactional
    public Contract create(Contract contract){
        int id=contractDAO.create(contract);
        return contractDAO.findOne(id);
    }
}
