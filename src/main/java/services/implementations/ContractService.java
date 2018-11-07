package services.implementations;

import dao.interfaces.ClientDaoI;
import dao.interfaces.ContractDaoI;
import dao.interfaces.OptionDaoI;
import dao.interfaces.TariffDaoI;
import entities.Client;
import entities.Contract;
import entities.Option;
import entities.Tariff;
import entities.dto.ContractDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.ServiceException;
import services.interfaces.ContractServiceI;
import services.interfaces.PhoneNumberServiceI;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class ContractService implements ContractServiceI {
    ContractDaoI contractDAO;
    TariffDaoI tariffDAO;
    OptionDaoI optionDao;
    ClientDaoI clientDAO;
    PhoneNumberServiceI phoneNumberService;

    @Autowired
    public void setDAO(ClientDaoI clientDAO, ContractDaoI contractDAO, OptionDaoI optionDao, TariffDaoI tariffDAO, PhoneNumberServiceI phoneNumberService) {
        this.clientDAO = clientDAO;
        this.contractDAO = contractDAO;
        this.optionDao = optionDao;
        this.tariffDAO = tariffDAO;
        this.phoneNumberService = phoneNumberService;

        this.contractDAO.setClass(Contract.class);
        this.tariffDAO.setClass(Tariff.class);
        this.optionDao.setClass(Option.class);
        this.clientDAO.setClass(Client.class);
    }

    @Override
    public Client findClientByPhone(long phone) {
        return contractDAO.findClientByPhone(phone);
    }

    @Override
    public Contract findByPhone(long phone) {
        return contractDAO.findByPhone(phone);
    }

    @Override
    public Contract get(int id) {
        return contractDAO.findOne(id);
    }

    @Override
    public ContractDTO getDTO(int id) {
        Contract contract = contractDAO.findOne(id);
        ContractDTO dto = new ContractDTO(contract);
        Set<Option> optionNames = contract.getOptions();
        dto.setOptionsNames(optionNames.stream().map(Option::getName).collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public void create(ContractDTO dto) throws ServiceException {
        //get tariff and it's options
        Tariff tariff = tariffDAO.findByName(dto.getTariffName());
        checkCompatibility(dto, tariff);

        Contract contract = new Contract();
        contract.setOwner(clientDAO.findOne(dto.getOwnerId()));
        if (!dto.getOptionsNames().isEmpty()) {
            Set<String> extra = dto.getOptionsNames();
            String[] params = extra.toArray(new String[]{});
            contract.setOptions(new HashSet<>(optionDao.findByNames(params)));
        }
        contract.setTariff(tariff);
        contract.setNumber(phoneNumberService.getNext());
        contract.setBlockedByAdmin(dto.isBlockedByAdmin());
        contract.setBlocked(dto.isBlocked());
        contractDAO.save(contract);
        dto.setId(contract.getId());
        dto.setNumber(contract.getNumber() + "");
    }

    private void checkCompatibility(ContractDTO dto, Tariff tariff) throws ServiceException {

        List<String> options = optionDao.getOptionsInTariffNames(tariff.getId());

        if (!dto.getOptionsNames().isEmpty()) {
            //get extra options
            Set<String> extra = dto.getOptionsNames();
            extra.removeAll(options); //remove all options that already in tariff

            //check if all options has its' mandatory
            String[] params = extra.toArray(new String[]{});
            List<String> names = optionDao.getAllMandatoryNames(params);
            List<String> all = new ArrayList<>(options);
            all.addAll(extra);
            if (!names.isEmpty() && !all.containsAll(names))
                throw new ServiceException("More options are required as mandatory: " + names.toString());

            //check if all options are compatible with each other
            names = optionDao.getAllIncompatibleNames(params);
            if (!names.isEmpty()) {
                Optional<String> any = names.stream().filter(name -> all.contains(name)).findFirst();
                if (any.isPresent())
                    throw new ServiceException("Option " + any.get() + " are incompatible with each other or with tariff");
            }
        }
    }

    @Override
    public void update(ContractDTO dto) throws ServiceException {
        Tariff tariff = tariffDAO.findByName(dto.getTariffName());
        checkCompatibility(dto, tariff);

        Contract contract = contractDAO.findOne(dto.getId());
        contract.getOptions().clear();

        if (!dto.getOptionsNames().isEmpty()) {
            Set<String> extra = dto.getOptionsNames();
            String[] params = extra.toArray(new String[]{});
            contract.getOptions().addAll((optionDao.findByNames(params)));
        }

        contract.setTariff(tariff);
        contract.setBlockedByAdmin(dto.isBlockedByAdmin());
        contract.setBlocked(dto.isBlocked());
        contractDAO.update(contract);
    }

    @Override
    public void delete(int id) {
        contractDAO.deleteById(id);
    }

    @Override
    public void block(int id) {
        Contract contract = contractDAO.findOne(id);
        if (!contract.isBlocked() && !contract.isBlockedByAdmin())
            contract.setBlocked(true);
    }

    @Override
    public void unblock(int id) {
        Contract contract = contractDAO.findOne(id);
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
    public Contract getFull(int id) {
        Contract contract = contractDAO.findOne(id);
        Hibernate.initialize(contract.getTariff().getOptions());
        Hibernate.initialize(contract.getOptions());
        return contract;
    }

    //client's actions

    @Override
    public void addOptions(int id, Collection<Option> options) throws ServiceException {
        Contract contract = contractDAO.findOne(id);
        Set<String> optionInContract = contract.getOptions().stream().map(Option::getName).collect(Collectors.toSet());
        Set<String> optionsInTariff = contract.getTariff().getOptions().stream().map(Option::getName).collect(Collectors.toSet());
        Set<String> newOptions = options.stream().map(Option::getName).collect(Collectors.toSet());

        //check if all options has its' mandatory
        String[] params = newOptions.toArray(new String[]{});
        List<String> names = optionDao.getAllMandatoryNames(params);
        List<String> all = new ArrayList<>(optionInContract);
        all.addAll(optionsInTariff);
        all.addAll(newOptions);
        if (!names.isEmpty() && !all.containsAll(names))
            throw new ServiceException("More options are required as mandatory: " + names.toString());

        //check if all options are compatible with each other
        names = optionDao.getAllIncompatibleNames(params);
        if (!names.isEmpty()) {
            Optional<String> any = names.stream().filter(name -> all.contains(name)).findFirst();
            if (any.isPresent())
                throw new ServiceException("Option " + any.get() + " are incompatible with each other or with your tariff");
        }

        contract.getOptions().addAll(options);
        contractDAO.update(contract);
    }

    @Override
    public void deleteOption(int id, int optionId) throws ServiceException {
        Contract contract = contractDAO.findOne(id);
        Option option = optionDao.findOne(optionId);

        //check if option is mandatory for some other
        Set<String> optionInContract = contract.getOptions().stream().map(Option::getName).collect(Collectors.toSet());
        List<String> names = optionDao.getMandatoryFor(optionId);
        optionInContract.retainAll(names);
        if (!optionInContract.isEmpty())
            throw new ServiceException("Option " + option.getName() + " is mandatory for other options: " + optionInContract.toString() + ". Delete them first");
        contract.getOptions().remove(option);
        contractDAO.update(contract);
    }

    @Override
    public void setTariff(int id, int tariffId) {
        Contract contract = contractDAO.findOne(id);
        Tariff tariff = tariffDAO.findOne(tariffId);
        contract.setTariff(tariff);
        contractDAO.update(contract);
    }
}
