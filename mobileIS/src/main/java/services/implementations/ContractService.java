package services.implementations;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dao.interfaces.ClientDaoI;
import dao.interfaces.ContractDaoI;
import dao.interfaces.OptionDaoI;
import dao.interfaces.TariffDaoI;
import model.dto.ContractDTO;
import model.entity.*;
import model.helpers.PaginateHelper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.exceptions.ServiceException;
import services.interfaces.ContractServiceI;
import services.interfaces.PhoneNumberServiceI;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class ContractService implements ContractServiceI {
    private static final String MESSAGE = "Option ";

    private ContractDaoI contractDAO;
    private TariffDaoI tariffDAO;
    private OptionDaoI optionDao;
    private ClientDaoI clientDAO;
    private PhoneNumberServiceI phoneNumberService;

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
    public String getJsonByPhone(String phone) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        try {
            ContractDTO dto = getByPhone(phone);
            if (dto == null) {
                setError(element, "there is no such contract");
            } else {
                element.getAsJsonObject().addProperty("status", "success");
                element.getAsJsonObject().add("contract", gson.toJsonTree(dto));
            }
        } catch (NumberFormatException e) {
            setError(element, "must be 10 digits");
        }
        return gson.toJson(element);
    }

    private void setError(JsonElement element, String message) {
        element.getAsJsonObject().addProperty("status", "error");
        element.getAsJsonObject().addProperty("message", message);
    }

    @Override
    public ContractDTO getByPhone(String phone) {
        if (!phone.matches("^[0-9]{10}")) throw new NumberFormatException();
        Contract contract = contractDAO.findByPhone(Long.parseLong(phone));
        if (contract == null) return null;
        else return new ContractDTO(contract);
    }

    @Override
    public Contract get(int id) {
        return contractDAO.findOne(id);
    }

    @Override
    public ContractDTO getDTO(int id) {
        Contract contract = contractDAO.findOne(id);
        ContractDTO dto = new ContractDTO(contract);
        Set<Option> options = contract.getOptions();
        dto.setOptionsIds(options.stream().map(Option::getId).collect(Collectors.toSet()));
        dto.setOptionNames(options.stream().map(Option::getName).collect(Collectors.joining(", ")));
        return dto;
    }

    public void addData(ContractDTO dto) {
        Map<String, Integer> m = dto.getAllOptions();
        optionDao.getAllNamesAndIds().forEach(array -> m.put((String) array[1], (Integer) array[0]));
        Map<String, Integer> m1 = dto.getAllTariffs();
        tariffDAO.getAllNamesAndIds().forEach(array -> m1.put((String) array[1], (Integer) array[0]));
    }

    @Override
    public int create(ContractDTO dto) throws ServiceException {
        //get tariff and it's options
        Tariff tariff = tariffDAO.findOne(dto.getTariffId());
        checkCompatibility(dto, tariff);

        Contract contract = new Contract();
        contract.setOwner(clientDAO.findOne(dto.getOwnerId()));
        if (!dto.getOptionsIds().isEmpty()) {
            Set<Integer> extra = dto.getOptionsIds();
            Integer[] params = extra.toArray(new Integer[]{});
            contract.setOptions(new HashSet<>(optionDao.findByIds(params)));
        }
        contract.setTariff(tariff);
        contract.setNumber(phoneNumberService.getNext());
        contract.setBlockedByAdmin(dto.isBlockedByAdmin());
        contract.setBlocked(dto.isBlocked());
        return contractDAO.save(contract);
    }

    private void checkCompatibility(ContractDTO dto, Tariff tariff) throws ServiceException {

        List<Integer> optionsInTariff = optionDao.getOptionsInTariffIds(tariff.getId());

        if (!dto.getOptionsIds().isEmpty()) {

            //get options in contract
            Set<Integer> optionsInContract = dto.getOptionsIds();
            optionsInContract.removeAll(optionsInTariff); //remove all options that already in tariff

            if (optionsInContract.isEmpty()) return;

            //check if all options has its' mandatory
            List<Integer> ids = optionDao.getMandatoryIdsFor(optionsInContract.toArray(new Integer[]{}));
            List<Integer> allMandatories = ids.stream()
                    .filter(id -> !(optionsInTariff.contains(id) || optionsInContract.contains(id)))
                    .collect(Collectors.toList());

            if (!allMandatories.isEmpty())
                throw new ServiceException("More options are required as mandatory: " + optionDao.getNamesByIds(allMandatories.toArray(new Integer[]{})).toString());

            //check if all options are compatible with each other and with options in tariff
            List<Integer> params = new ArrayList<>(optionsInContract);
            params.addAll(optionsInTariff);
            List<OptionRelation> relations = optionDao.getIncompatibleRelationInRange(params.toArray(new Integer[]{}));
            if (!relations.isEmpty()) {
                String s = relations.stream()
                        .map(r -> r.getOne().getName() + " and " + r.getAnother().getName())
                        .collect(Collectors.joining(", "));
                throw new ServiceException("Options " + s + " incompatible with each other");
            }
        }
    }

    @Override
    public void update(ContractDTO dto) throws ServiceException {
        Tariff tariff = tariffDAO.findOne(dto.getTariffId());
        checkCompatibility(dto, tariff);

        Contract contract = contractDAO.findOne(dto.getId());
        contract.getOptions().clear();

        if (!dto.getOptionsIds().isEmpty()) {
            Set<Integer> extra = dto.getOptionsIds();
            Integer[] params = extra.toArray(new Integer[]{});
            contract.getOptions().addAll((optionDao.findByIds(params)));
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
    public PaginateHelper<Contract> getPaginateData(Integer currentPage, int rowPerPage) {
        if (currentPage == null) currentPage = 1;  //if no page specified, show first page
        if (currentPage < 1 || rowPerPage < 0) throw new IllegalArgumentException();
        List<Contract> optionsForPage = contractDAO.allInRange((currentPage - 1) * rowPerPage, rowPerPage);
        int total = contractDAO.count().intValue();
        int totalPage = total / rowPerPage;
        if (total % rowPerPage > 0) totalPage++;
        return new PaginateHelper<>(optionsForPage, totalPage);
    }

    @Override
    public Contract getFull(int id) {
        Contract contract = contractDAO.findOne(id);
        Hibernate.initialize(contract.getTariff().getOptions());
        Hibernate.initialize(contract.getOptions());
        return contract;
    }

    @Override
    public Contract getFullByPhone(long phone) {
        Integer id = contractDAO.getIdByPhone(phone);
        return getFull(id);
    }

    //client's actions. Admin blocking check must be done before any actions

    @Override
    public void addOptions(int id, Collection<Option> options) throws ServiceException {
        if (options.isEmpty()) throw new ServiceException("nothing to buy");
        Contract contract = contractDAO.findOne(id);
        if (contract.isBlockedByAdmin()) return;
        Set<Option> optionInContract = contract.getOptions();
        Set<Option> optionsInTariff = contract.getTariff().getOptions();

        //check if all options has its' mandatory
        Integer[] params = options.stream().map(Option::getId).collect(Collectors.toList()).toArray(new Integer[]{});
        List<OptionRelation> relations = optionDao.getMandatoryRelation(params);
        List<String> allMandatories = relations.stream()
                .filter(relation -> !(optionInContract.contains(relation.getAnother()) || options.contains(relation.getAnother()) || optionsInTariff.contains(relation.getAnother())))
                .map(relation -> relation.getAnother().getName())
                .collect(Collectors.toList());
        if (!allMandatories.isEmpty()) {
            throw new ServiceException("More options are required as mandatory: " + allMandatories.toString());
        }

        //check if all options are compatible with each other
        relations = optionDao.getIncompatibleRelationInRange(params);
        if (!relations.isEmpty()) {
            String s = relations.stream().map(r -> r.getOne().getName() + " and " + r.getAnother().getName()).collect(Collectors.joining(", "));
            throw new ServiceException("Options " + s + " incompatible with each other");
        }

        contract.getOptions().addAll(options);
        contractDAO.update(contract);
    }

    @Override
    public void deleteOption(int id, int optionId) throws ServiceException {
        Contract contract = contractDAO.findOne(id);
        if (contract.isBlockedByAdmin()) return;
        Option option = optionDao.findOne(optionId);

        //check if option is mandatory for some other
        Set<Integer> optionInContract = contract.getOptions().stream().map(Option::getId).collect(Collectors.toSet());
        List<Integer> ids = optionDao.getMandatoryIdsFor(optionInContract.toArray(new Integer[]{}));
        optionInContract.retainAll(ids);
        if (!optionInContract.isEmpty())
            throw new ServiceException(MESSAGE + optionDao.getNamesByIds(new Integer[]{optionId}).toString() + " is mandatory for other options: " + optionDao.getNamesByIds(optionInContract.toArray(new Integer[]{})).toString() + ". Delete them first");
        contract.getOptions().remove(option);
        contractDAO.update(contract);
    }

    @Override
    public String deleteOptionJson(int id, int optionId) {
        Contract contract = contractDAO.findOne(id);
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        if (contract.isBlockedByAdmin()) {
            element.getAsJsonObject().addProperty("status", "error");
            element.getAsJsonObject().addProperty("message", "contract is blocked");
            return gson.toJson(element);
        }

        Option option = optionDao.findOne(optionId);

        //check if option is mandatory for some other
        Set<Integer> optionInContract = contract.getOptions().stream().map(Option::getId).collect(Collectors.toSet());
        List<String> names = optionDao.getMandatoryForInRange(optionInContract.toArray(new Integer[]{}), optionId);
        if (!names.isEmpty()) {
            element.getAsJsonObject().addProperty("status", "error");
            element.getAsJsonObject().addProperty("message", MESSAGE + optionDao.getNamesByIds(new Integer[]{optionId}).toString() + " is mandatory for other options: " + names.toString() + ". Delete them first");
            element.getAsJsonObject().addProperty("id", optionId);
            return gson.toJson(element);
        }

        contract.getOptions().remove(option);
        contractDAO.update(contract);
        element.getAsJsonObject().addProperty("status", "success");
        element.getAsJsonObject().addProperty("id", optionId);
        return gson.toJson(element);
    }

    @Override
    public void setTariff(int id, int tariffId) {
        Contract contract = contractDAO.findOne(id);
        if (contract.isBlockedByAdmin()) return;
        Tariff tariff = tariffDAO.findOne(tariffId);
        contract.getOptions().clear();
        contract.setTariff(tariff);
        contractDAO.update(contract);
    }
}
