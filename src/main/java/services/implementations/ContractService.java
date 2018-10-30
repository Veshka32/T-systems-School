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
import repositories.interfaces.ClientDaoI;
import repositories.interfaces.ContractDaoI;
import repositories.interfaces.TariffDaoI;
import repositories.interfaces.TariffOptionDaoI;
import services.ServiceException;
import services.interfaces.ContractServiceI;
import services.interfaces.PhoneNumberServiceI;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractService implements ContractServiceI {
    @Autowired
    ContractDaoI contractDAO;

    @Autowired
    TariffDaoI tariffDAO;

    @Autowired
    TariffOptionDaoI tariffOptionDAO;

    @Autowired
    ClientDaoI clientDAO;

    @Autowired
    PhoneNumberServiceI phoneNumberService;

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
    public void block(int id){
        Contract contract=contractDAO.findOne(id);
        if (!contract.isBlocked() && !contract.isBlockedByAdmin())
            contract.setBlocked(true);
    }

    @Override
    public void unblock(int id){
        Contract contract=contractDAO.findOne(id);
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
    public Contract getFull(int id){
        Contract contract=contractDAO.findOne(id);
        Hibernate.initialize(contract.getTariff().getOptions());
        Hibernate.initialize(contract.getOptions());
        return contract;
    }

    @Override
    public void addOptions(int id,Collection<TariffOption> options) {
        Contract contract=contractDAO.findOne(id);
        contract.getOptions().addAll(options);
        contractDAO.update(contract);
    }

    @Override
    public void deleteOption(int id, int optionId) throws ServiceException{
        Contract contract=contractDAO.findOne(id);
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
