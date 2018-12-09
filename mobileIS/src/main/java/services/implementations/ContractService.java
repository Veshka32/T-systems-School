/**
 * This class implements {@code ContractServiceI} interface.
 * It is a service-layer class for manipulating with {@code Contract} entities.
 * <p>
 *
 * @author Natalia Makarchuk
 */
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
import model.stateful.CartInterface;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.ContractServiceI;
import services.interfaces.PhoneNumberServiceI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@EnableTransactionManagement
@Transactional
public class ContractService implements ContractServiceI {
    private static final String MESSAGE = "Option(s) ";
    private static final String BLOCKED = "Contract is blocked";

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

    /**
     * Returns json representation of {@code Contract} with specific phone number (phone supposed to be unique)
     *
     * @param phone unique phone number
     * @return String in json format
     */
    @Override
    public String getJsonByPhone(String phone) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        try {
            Optional<ContractDTO> dto = getByPhone(phone);
            if (dto.isPresent()) {
                setSuccess(element);
                element.getAsJsonObject().add("contract", gson.toJsonTree(dto.get()));
            } else {
                setError(element, "There is no such contract");
            }
        } catch (NumberFormatException e) {
            setError(element, "must be 10 digits");
        }
        return gson.toJson(element);
    }

    /**
     * Build data transfer object for {@code Contract} with specific phone number (supposed to be unique)
     * or empty if such a contract doesn't exist
     *
     * @param phone unique phone number
     * @return Optional contains contract properties or empty Optional
     */
    public Optional<ContractDTO> getByPhone(String phone) {
        if (!phone.matches("^[0-9]{10}")) throw new NumberFormatException();
        Contract contract = contractDAO.findByPhone(Long.parseLong(phone));
        return contract == null ? Optional.empty() : Optional.of(new ContractDTO(contract));
    }

    /**
     * Return contract with specific id
     *
     * @param id database id of contract
     * @return {@code Contract} with specific id
     */
    @Override
    public Contract get(int id) {
        return contractDAO.findOne(id);
    }

    /**
     * Build data transfer object for {@code Contract} with specific id, including fields of type collection (initialized)
     *
     * @param id database id of desired {@code Contract} object
     * @return {@code ContractDTO} object contains contract properties
     */
    @Override
    public ContractDTO getDTO(int id) {
        Contract contract = contractDAO.findOne(id);
        ContractDTO dto = new ContractDTO(contract);
        Set<Option> options = contract.getOptions();
        dto.setOptionsIds(options.stream().map(Option::getId).collect(Collectors.toSet()));
        dto.setOptionNames(options.stream().map(Option::getName).collect(Collectors.joining(", ")));
        return dto;
    }

    /**
     * Add additional data into contract data transfer object
     *
     * @param dto data transfer object keeps desired data
     */
    @Override
    public void addData(ContractDTO dto) {
        Map<String, Integer> m = dto.getAllOptions();
        optionDao.getAllNamesAndIds().forEach(array -> m.put((String) array[1], (Integer) array[0]));
        Map<String, Integer> m1 = dto.getAllTariffs();
        tariffDAO.getAllNamesAndIds().forEach(array -> m1.put((String) array[1], (Integer) array[0]));
    }

    /**
     * Create new {@code Contract} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return empty Optional if contract is successfully created or error message if not
     */
    @Override
    public Optional<String> create(ContractDTO dto) {
        //get tariff and it's options
        Tariff tariff = tariffDAO.findOne(dto.getTariffId());
        Optional<String> error = checkCompatibility(dto, tariff);

        if (error.isPresent()) return Optional.of(error.get());

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
        dto.setId(contractDAO.save(contract));
        return Optional.empty();
    }

    /**
     * Update all fields in corresponding {@code Contract} with field values from data transfer object
     *
     * @param dto data transfer object contains contract id and properties
     * @return either empty Optional if contract is successfully updated or error message if not
     */
    @Override
    public Optional<String> update(ContractDTO dto) {
        Tariff tariff = tariffDAO.findOne(dto.getTariffId());
        Optional<String> error = checkCompatibility(dto, tariff);
        if (error.isPresent()) return error;

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
        return Optional.empty();
    }

    /**
     * Delete {@code Contract} with specific id from database and its corresponding {@code User}
     *
     * @param id id of contract to delete
     */
    @Override
    public void delete(int id) {
        contractDAO.deleteById(id);
    }

    /**
     * Set block property of specific contract to true;
     *
     * @param id id of {@code Contract} to block
     */
    @Override
    public void block(int id) {
        Contract contract = contractDAO.findOne(id);
        if (!contract.isBlocked() && !contract.isBlockedByAdmin())
            contract.setBlocked(true);
    }

    /**
     * Set block property of specific contract to false;
     *
     * @param id id of {@code Contract}  to unblock
     */
    @Override
    public void unblock(int id) {
        Contract contract = contractDAO.findOne(id);
        if (!contract.isBlockedByAdmin() && contract.isBlocked())
            contract.setBlocked(false);
    }

    /**
     * Construct object contains contract in specific range from database and additional info (total number of contracts in database)
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (equals total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with contracts in specific range and additional info
     */
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

    /**
     * Return contract with eager fetched collection fields (no proxies)
     *
     * @param id id {@code Contract}
     * @return {@code Contract} with fully loaded fields from database
     */
    @Override
    public Contract getFull(int id) {
        Contract contract = contractDAO.findOne(id);
        Hibernate.initialize(contract.getTariff().getOptions());
        Hibernate.initialize(contract.getOptions());
        return contract;
    }

    /**
     * Return contract with eager fetched collection fields (no proxies)
     *
     * @param phone phone number
     * @return {@code Contract} with fully loaded fields from database
     */
    @Override
    public Contract getFullByPhone(long phone) {
        Integer id = contractDAO.getIdByPhone(phone);
        return getFull(id);
    }

    //<-----client's actions. Admin blocking check must be done before any actions----->

    /**
     * Add new options into the specific contract
     *
     * @param id      id of {@code Contract}
     * @param options collection of {@code Option} to add
     * @return either empty Optional if contract is successfully updated or error message if not
     */
    @Override
    public Optional<String> addOptions(int id, Collection<Option> options) {
        if (options.isEmpty()) return Optional.of("Nothing to buy");

        Contract contract = contractDAO.findOne(id);
        if (contract.isBlockedByAdmin() || contract.isBlocked()) return Optional.of(BLOCKED);

        List<Option> duplicate = options.stream().filter(o -> contract.getOptions().contains(o)).collect(Collectors.toList());
        if (!duplicate.isEmpty())
            return Optional.of("These options already included in your contract: " + duplicate.toString());

        duplicate = options.stream().filter(o -> contract.getTariff().getOptions().contains(o)).collect(Collectors.toList());
        if (!duplicate.isEmpty())
            return Optional.of("These options already included in your tariff: " + duplicate.toString());

        //check if all options has its' mandatory
        Integer[] newOptionIds = options.stream().map(Option::getId).collect(Collectors.toList()).toArray(new Integer[]{});
        List<OptionRelation> relations = optionDao.getMandatoryRelation(newOptionIds);
        List<String> required = relations.stream()
                .filter(relation -> !(contract.getOptions().contains(relation.getAnother()) || options.contains(relation.getAnother()) || contract.getTariff().getOptions().contains(relation.getAnother())))
                .map(relation -> relation.getAnother().getName())
                .collect(Collectors.toList());
        if (!required.isEmpty()) {
            return Optional.of("More options are required as mandatory: " + required.stream().collect(Collectors.joining(", ")));
        }

        //check if new options are compatible with tariff and options in contract, throw exception if not
        Integer[] existedOptionIds = Stream.concat(contract.getOptions().stream(), contract.getTariff().getOptions().stream())
                .map(Option::getId)
                .collect(Collectors.toList())
                .toArray(new Integer[]{});

        Optional<String> error = checkIncompatibilityWithTariff(newOptionIds, existedOptionIds);
        if (error.isPresent()) return error;

        //check if new options compatible with each other, throw exception if not
        error = checkIncompatibility(newOptionIds);
        if (error.isPresent()) return error;

        contract.getOptions().addAll(options);
        contractDAO.update(contract);

        return Optional.empty();
    }

    /**
     * Add new options from cart into specific contract
     *
     * @param cart      {@code CartInterface} session stateful object
     * @return json formatted String contains result of the action
     */
    @Override
    public String addOptionsToJson(CartInterface cart) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        Optional<String> error = addOptions(cart.getContractId(), cart.getOptions());
        if (error.isPresent()) {
            setError(element, error.get());
        } else {
            cart.clear();
            setSuccess(element);
        }
        return gson.toJson(element);
    }

    /**
     * Delete specific option from specific contract
     *
     * @param id       id of {@code Contract}
     * @param optionId id of {@code Option} to delete
     * @return either empty Optional if option is successfully deleted or error message if not
     */

    @Override
    public Optional<String> deleteOption(int id, int optionId) {
        Contract contract = contractDAO.findOne(id);
        if (contract.isBlockedByAdmin() || contract.isBlocked()) return Optional.of(BLOCKED);

        Option option = optionDao.findOne(optionId);

        //check if option is mandatory for some other
        Set<Integer> optionInContract = contract.getOptions().stream().map(Option::getId).collect(Collectors.toSet());
        optionInContract.remove(optionId); //don't search mandatory relation for target option
        List<OptionRelation> required = optionDao.getMandatoryRelation(optionInContract.toArray(new Integer[]{}));
        List<String> mandatoryFor = required.stream().filter(r -> r.getAnother().getId() == optionId).map(r -> r.getOne().getName()).collect(Collectors.toList());

        if (!mandatoryFor.isEmpty())
            return Optional.of(MESSAGE + option.getName() + " is mandatory for other options in your contract: " + mandatoryFor.stream().collect(Collectors.joining(", ")) + ". Delete them first");

        contract.getOptions().remove(option);
        contractDAO.update(contract);
        return Optional.empty();
    }

    /**
     * Delete specific option from specific contract
     *
     * @param id       id of {@code Contract}
     * @param optionId id of {@code Option} to delete
     * @return json formatted String contains result of the action
     */
    @Override
    public String deleteOptionJson(int id, int optionId) {
        Gson gson = new Gson();
        JsonElement element = new JsonObject();
        Optional<String> error = deleteOption(id, optionId);
        if (error.isPresent()) {
            setError(element, error.get());
        } else {
            setSuccess(element);
            element.getAsJsonObject().addProperty("id", optionId);
        }

        return gson.toJson(element);
    }

    /**
     * Replace tariff in contract to the new one
     *
     * @param id       id of {@code Contract}
     * @param tariffId id of {@code Tariff} to set
     * @return either empty Optional if tariff is successfully changed or error message if not
     */
    @Override
    public Optional<String> setTariff(int id, int tariffId) {
        Contract contract = contractDAO.findOne(id);
        if (contract.isBlockedByAdmin() || contract.isBlocked()) return Optional.of(BLOCKED);

        Tariff tariff = tariffDAO.findOne(tariffId);
        contract.getOptions().clear();
        contract.setTariff(tariff);
        contractDAO.update(contract);
        return Optional.empty();
    }

    /*
     * Check compatibility of options in contract.
     * Return either Optional with error message or empty Optional if logic is ok
     */
    private Optional<String> checkCompatibility(ContractDTO dto, Tariff tariff) {

        if (!dto.getOptionsIds().isEmpty()) {

            //get options in contract
            Set<Integer> optionsInContract = dto.getOptionsIds();
            Set<Integer> optionsInTariffIds = tariff.getOptions().stream().map(Option::getId).collect(Collectors.toSet());
            optionsInContract.removeIf(optionsInTariffIds::contains); //remove all options that already in tariff

            //check if all options has its' mandatory
            List<OptionRelation> relations = optionDao.getMandatoryRelation(optionsInContract.toArray(new Integer[]{}));
            Set<String> requires = relations.stream()
                    .filter(r -> !(tariff.getOptions().contains(r.getAnother()) || optionsInContract.contains(r.getAnother().getId())))
                    .map(r -> r.getAnother().getName())
                    .collect(Collectors.toSet());

            if (!requires.isEmpty())
                return Optional.of("More options are required as mandatory: " + requires.stream().collect(Collectors.joining(", ")));

            //check if all options are compatible with each other
            Optional<String> error = checkIncompatibility(optionsInContract.toArray(new Integer[]{}));
            if (error.isPresent()) return error;

            //check if all options are compatible with options in tariff, throw exception if not
            Integer[] optionInTariffIds = tariff.getOptions().stream().map(Option::getId).collect(Collectors.toList()).toArray(new Integer[]{});
            error = checkIncompatibilityWithTariff(optionsInContract.toArray(new Integer[]{}), optionInTariffIds);
            if (error.isPresent()) return error;
        }
        return Optional.empty();
    }

    /*
     * Check if specific options are compatible with options in specific tariff
     * @param newIds options' ids
     * @param existedIds options in tariff ids
     * @return either Optional with error message or empty Optional if logic is ok
     */
    private Optional<String> checkIncompatibilityWithTariff(Integer[] newIds, Integer[] existedIds) {
        List<Option> incompatibleWithTariff = optionDao.getIncompatibleWithTariff(newIds, existedIds);
        if (!incompatibleWithTariff.isEmpty()) {
            return Optional.of(MESSAGE + incompatibleWithTariff.stream().map(Option::getName).collect(Collectors.joining(", ")) + " incompatible with your contract");
        }
        return Optional.empty();
    }

    /*
     * Check if specific options are compatible with each other
     * @param ids options for checking
     * @return either Optional with error message or empty Optional if logic is ok
     */
    private Optional<String> checkIncompatibility(Integer[] ids) {
        List<OptionRelation> relations = optionDao.getIncompatibleRelationInRange(ids);
        if (!relations.isEmpty()) {
            String s = relations.stream()
                    .map(r -> r.getOne().getName() + " and " + r.getAnother().getName())
                    .collect(Collectors.joining(", "));
            return Optional.of(MESSAGE + s + " incompatible with each other");
        }
        return Optional.empty();
    }

    private void setError(JsonElement element, String message) {
        element.getAsJsonObject().addProperty("status", "error");
        element.getAsJsonObject().addProperty("message", message);
    }

    private void setSuccess(JsonElement element) {
        element.getAsJsonObject().addProperty("status", "success");
    }
}
