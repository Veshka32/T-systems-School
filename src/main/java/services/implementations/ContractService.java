package services.implementations;

import entities.Client;
import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
import entities.dto.ContractDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.implementations.ClientDAO;
import repositories.implementations.ContractDAO;
import repositories.implementations.TariffDAO;
import repositories.implementations.TariffOptionDAO;
import services.ServiceException;
import services.interfaces.ContractServiceI;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractService implements ContractServiceI {
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

    @Override
    public Client findClientByPhone(long phone) {
        return contractDAO.findClientByPhone(phone);
    }

    @Override
    public Contract findByPhone(long phone){
        return contractDAO.findByPhone(phone);
    }

    @Override
    public Contract get(int id){
        return contractDAO.findOne(id);
    }

    @Override
    public ContractDTO getDTO(int id){
        Contract contract=contractDAO.findOne(id);
        ContractDTO dto= new ContractDTO(contractDAO.findOne(id));
        Set<TariffOption> optionNames=contract.getOptions();
        dto.setOptionsNames(optionNames.stream().map(TariffOption::getName).collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public void create(ContractDTO dto) throws ServiceException {
        Tariff tariff=tariffDAO.findByName(dto.getTariffName());
        Set<TariffOption> options=tariff.getOptions();
        List<TariffOption> extra=dto.getOptionsNames().stream()
                .map(name->tariffOptionDAO.findByName(name))
                .filter(o->!options.contains(o)) //remove option that already in tariff
                .collect(Collectors.toList());

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
        contract.setOptions(new HashSet<>(extra));
        contract.setTariff(tariff);
        contract.setNumber(phoneNumberService.getNext());

        contractDAO.save(contract);
        dto.setId(contract.getId());
        dto.setNumber(contract.getNumber()+"");
    }

    @Override
    public void update(ContractDTO dto) throws ServiceException{
        Tariff tariff=tariffDAO.findByName(dto.getTariffName());
        Set<TariffOption> options=tariff.getOptions();
        List<TariffOption> extra=dto.getOptionsNames().stream()
                .map(name->tariffOptionDAO.findByName(name))
                .filter(o->!options.contains(o)) //remove option that already in tariff
                .collect(Collectors.toList());

        //check if all options has its mandatory
        Optional<TariffOption> any=extra.stream()
                .flatMap(o->o.getMandatoryOptions().stream())
                .filter(o->!(options.contains(o) || extra.contains(o)))
                .findFirst();
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

        Contract contract=contractDAO.findOne(dto.getId());
        contract.setOptions(new HashSet<>(extra));
        contract.setTariff(tariff);
        contractDAO.update(contract);
    }

    @Override
    public void delete(int id){
        contractDAO.deleteById(id);
    }

    @Override
    public void block(long phone){
        Contract contract=contractDAO.findByPhone(phone);
        if (!contract.isBlocked() && !contract.isBlockedByAdmin())
            contract.setBlocked(true);
    }

    @Override
    public void unblock(long phone){
        Contract contract=contractDAO.findByPhone(phone);
        if (!contract.isBlockedByAdmin() && contract.isBlocked())
            contract.setBlocked(false);
    }

    @Override
    public List<Contract> getAllClientContracts(int clientId) {
        return contractDAO.getClientContracts(clientId);
    }
    @Override
    public List<Contract> getAll() {
        return contractDAO.findAll();
    }

    @Override
    public Contract getFull(long phone){
        Contract contract=contractDAO.findByPhone(phone);
        Hibernate.initialize(contract.getTariff().getOptions());
        Hibernate.initialize(contract.getOptions());
        return contract;
    }

    @Override
    public void deleteOption(long phone, int optionId) throws ServiceException{
        Contract contract=contractDAO.findByPhone(phone);
        TariffOption option=tariffOptionDAO.findOne(optionId);
        Optional<TariffOption> any=contract.getOptions().stream().filter(o-> option.getMandatoryOptions().contains(option)).findFirst();
        if (any.isPresent()) throw new ServiceException("Option "+option.getName()+" is mandatory for option"+any.get().getName());
        contract.getOptions().remove(option);
        contractDAO.update(contract);
    }

    @Override
    public void setTariff(long phone, int tariffId){
        Contract contract=contractDAO.findByPhone(phone);
        Tariff tariff=tariffDAO.findOne(tariffId);
        contract.setTariff(tariff);
        contractDAO.update(contract);
    }
}
