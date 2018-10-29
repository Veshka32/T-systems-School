package services;

import entities.Client;
import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
import entities.dto.ContractDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ClientDAO;
import repositories.ContractDAO;
import repositories.TariffDAO;
import repositories.TariffOptionDAO;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractService {
    @Autowired
    ContractDAO  contractDAO;

    @Autowired
    TariffDAO tariffDAO;

    @Autowired
    TariffOptionDAO tariffOptionDAO;

    @Autowired
    ClientDAO clientDAO;

    @Autowired
    PhoneNumberService phoneNumberService;

    public Client findClientByPhone(long phone) {
        return contractDAO.findClientByPhone(phone);
    }

    public Contract findByPhone(long phone){
        return contractDAO.findByPhone(phone);
    }

    public Contract get(int id){
        return contractDAO.findOne(id);
    }

    public void create(ContractDTO dto) throws ServiceException{
        Tariff tariff=tariffDAO.findByName(dto.getTariffName());
        Set<TariffOption> options=tariff.getOptions();
        List<TariffOption> extra=dto.getOptionsNames().stream().map(name->tariffOptionDAO.findByName(name)).collect(Collectors.toList());

        //check if all options has its mandatory
        Optional<TariffOption> any=extra.stream().flatMap(o->o.getMandatoryOptions().stream()).filter(o->!(options.contains(o) || extra.contains(o))).findFirst();
        if (any.isPresent()) throw new ServiceException("Option "+any.get().getName()+" requires options "+any.get().getMandatoryOptions().toString());

        //check if extra are compatible with each other
        for (TariffOption op:extra) {
        any=op.getIncompatibleOptions().stream().filter(o->extra.contains(o)).findFirst();
            if (any.isPresent()) throw new ServiceException("Option "+op.getName()+" incompatible with option "+any.get().getName());
        }

        //check if extra are compatible with tariff options
        for(TariffOption op:extra){
            any=op.getIncompatibleOptions().stream().filter(o->options.contains(o)).findFirst();
            if (any.isPresent()) throw new ServiceException("Option "+op.getName()+" incompatible with tariff option "+any.get().getName());
        }

        Contract contract=new Contract();
        contract.setOwner(clientDAO.findOne(dto.getOwnerId()));
        contract.setOptions(options);
        contract.setTariff(tariff);
        contract.setNumber(phoneNumberService.getNext());

        contractDAO.save(contract);
        dto.setId(contract.getId());
        dto.setNumber(contract.getNumber()+"");
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
