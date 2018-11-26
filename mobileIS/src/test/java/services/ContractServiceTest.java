package services;

import dao.implementations.ClientDAO;
import dao.implementations.ContractDAO;
import dao.implementations.OptionDAO;
import dao.implementations.TariffDAO;
import model.dto.ContractDTO;
import model.entity.*;
import model.enums.RELATION;
import model.helpers.PaginateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import services.exceptions.ServiceException;
import services.implementations.ContractService;
import services.implementations.PhoneNumberService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ContractServiceTest {
    @InjectMocks
    private ContractService contractService;

    @Mock
    private ContractDAO contractDAO;

    @Mock
    private OptionDAO optionDAO;

    @Mock
    private TariffDAO tariffDAO;

    @Mock
    private PhoneNumberService phoneNumberServiceI;

    @Mock
    private ClientDAO clientDAO;

    @BeforeEach
    void before() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createTest() {
        //set mocks
        Contract contract = new Contract();
        contract.setOptions(new HashSet<>());
        ContractDTO dto = new ContractDTO();
        dto.setOwnerId(1);
        dto.setTariffId(1);
        Option a = new Option();
        a.setName("a");
        a.setId(1);
        Option b = new Option();
        b.setName("b");
        b.setId(2);
        Option c = new Option();
        c.setName("c");
        c.setId(3);
        Option d = new Option();
        d.setName("d");
        d.setId(4);
        OptionRelation r = new OptionRelation(a, d, RELATION.MANDATORY); //a requires d

        Tariff tariff = new Tariff();
        tariff.setOptions(new HashSet<>());
        contract.setTariff(tariff);
        when(phoneNumberServiceI.getNext()).thenReturn(new Long(1));
        when(clientDAO.findOne(1)).thenReturn(new Client());
        when(tariffDAO.findOne(1)).thenReturn(tariff);

        //no options
        when(tariffDAO.findOne(1)).thenReturn(tariff);
        assertDoesNotThrow(() -> contractService.create(dto));

        //check if all options has its' mandatory
        dto.getOptionsIds().add(a.getId());
        when(optionDAO.getMandatoryRelation(new Integer[]{a.getId()})).thenReturn(Collections.singletonList(r)); // a requires d
        ServiceException e = assertThrows(ServiceException.class, () -> contractService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + d.getName());

        //mandatory is in tariff
        tariff.getOptions().add(d);
        when(optionDAO.getIncompatibleWithTariff(new Integer[]{a.getId()}, new Integer[]{d.getId()})).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleRelationInRange(new Integer[]{a.getId()})).thenReturn(Collections.emptyList());
        when(optionDAO.findByIds(new Integer[]{a.getId()})).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> contractService.create(dto));
        tariff.getOptions().clear();
        contract.getOptions().clear();

        //mandatory is in list
        dto.getOptionsIds().add(d.getId());
        when(optionDAO.getMandatoryRelation(new Integer[]{a.getId(), d.getId()})).thenReturn(Collections.singletonList(r)); // a requires d
        when(optionDAO.getIncompatibleRelationInRange(new Integer[]{a.getId(), d.getId()})).thenReturn(Collections.emptyList());
        when(optionDAO.findByIds(new Integer[]{a.getId(), d.getId()})).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> contractService.create(dto));
        dto.getOptionsIds().clear();
        contract.getOptions().clear();

        //no mandatory, options incompatible with each other
        r.setRelation(RELATION.INCOMPATIBLE); //a incompatible with d
        dto.getOptionsIds().add(a.getId());
        dto.getOptionsIds().add(d.getId());
        when(optionDAO.getIncompatibleRelationInRange(new Integer[]{a.getId(), d.getId()})).thenReturn(Collections.singletonList(r));
        e = assertThrows(ServiceException.class, () -> contractService.create(dto));
        assertEquals(e.getMessage(), "Option(s) " + a.getName() + " and " + d.getName() + " incompatible with each other");

        //no mandatory, option incompatible with tariff
        tariff.getOptions().add(d);
        dto.getOptionsIds().remove(d.getId());
        when(optionDAO.getIncompatibleWithTariff(new Integer[]{a.getId()}, new Integer[]{d.getId()})).thenReturn(Collections.singletonList(a));
        e = assertThrows(ServiceException.class, () -> contractService.create(dto));
        assertEquals(e.getMessage(), "Option(s) " + a.getName() + " incompatible with your contract");
    }

    @Test
    void blockTest() {
        Contract contract = new Contract();
        when(contractDAO.findOne(1)).thenReturn(contract);

        //client can block if not block by admin
        contractService.block(1);
        assert contract.isBlocked();

        //client can'not block if already blocked by admin
        contract.setBlocked(false);
        contract.setBlockedByAdmin(true);
        contractService.block(1);
        assert !contract.isBlocked();
    }

    @Test
    void unblockTest() {
        Contract contract = new Contract();
        contract.setBlocked(true);

        //client can unblock if not blocked by admin
        when(contractDAO.findOne(1)).thenReturn(contract);
        contractService.unblock(1);
        assert !contract.isBlocked();

        //client can't unblock if blocked by admin
        contract.setBlockedByAdmin(true);
        contract.setBlocked(true);
        contractService.unblock(1);
        assert contract.isBlocked();
    }

    @Test
    void addOptionsTest() {
        //set mocks
        Contract contract = new Contract();
        contract.setOptions(new HashSet<>());
        Option a = new Option();
        a.setName("a");
        a.setId(1);
        Option b = new Option();
        b.setName("b");
        b.setId(2);
        Option c = new Option();
        c.setName("c");
        c.setId(3);
        Option d = new Option();
        d.setName("d");
        d.setId(4);
        OptionRelation r = new OptionRelation(a, d, RELATION.MANDATORY); //a requires d

        List<Option> options = Arrays.asList(a, b);
        Tariff tariff = new Tariff();
        contract.setTariff(tariff);

        //add empty list
        ServiceException e = assertThrows(ServiceException.class, () -> contractService.addOptions(1, Collections.emptyList()));
        assertEquals(e.getMessage(), "Nothing to buy");

        //contract blocked
        when(contractDAO.findOne(1)).thenReturn(contract);
        contract.setBlockedByAdmin(true);
        e = assertThrows(ServiceException.class, () -> contractService.addOptions(1, options));
        assertEquals(e.getMessage(), "Contract is blocked");
        contract.setBlockedByAdmin(false);
        contract.setBlocked(true);
        e = assertThrows(ServiceException.class, () -> contractService.addOptions(1, options));
        assertEquals(e.getMessage(), "Contract is blocked");

        //options already in contract
        contract.getOptions().add(a);
        assertThrows(ServiceException.class, () -> contractService.addOptions(1, options));
        contract.getOptions().clear();

        //options already in tariff
        tariff.getOptions().add(a);
        assertThrows(ServiceException.class, () -> contractService.addOptions(1, options));
        tariff.getOptions().clear();

        //check if all options has its' mandatory
        contract.setBlocked(false);
        when(optionDAO.getMandatoryRelation(new Integer[]{1, 2})).thenReturn(Collections.singletonList(r));
        e = assertThrows(ServiceException.class, () -> contractService.addOptions(1, options));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + d.getName()); //a requires d

        //everything is ok
        when(optionDAO.getMandatoryRelation(new Integer[]{1, 2})).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleWithTariff(new Integer[]{1, 2}, new Integer[]{})).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleRelationInRange(new Integer[]{1, 2})).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> contractService.addOptions(1, options));
        assertTrue(contract.getOptions().containsAll(options));
        contract.getOptions().clear();

        //mandatory is in tariff
        tariff.getOptions().add(d);
        when(optionDAO.getMandatoryRelation(new Integer[]{1, 2})).thenReturn(Collections.singletonList(r)); //a requires d
        assertDoesNotThrow(() -> contractService.addOptions(1, options));
        assertTrue(contract.getOptions().containsAll(options));
        contract.getOptions().clear();
        tariff.getOptions().clear();
        tariff.getOptions().add(c);

        //mandatory is in contract
        contract.getOptions().add(d);
        assertDoesNotThrow(() -> contractService.addOptions(1, options));
        assertTrue(contract.getOptions().containsAll(options));
        contract.getOptions().clear();

        //no mandatory, incompatible with tariff
        tariff.getOptions().add(c);
        r.setAnother(c);
        r.setRelation(RELATION.INCOMPATIBLE);//a incompatible with c
        when(optionDAO.getMandatoryRelation(new Integer[]{1, 2})).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleWithTariff(new Integer[]{1, 2}, new Integer[]{3})).thenReturn(Collections.singletonList(a));
        e = assertThrows(ServiceException.class, () -> contractService.addOptions(1, options));
        assertEquals(e.getMessage(), "Option(s) " + a.getName() + " incompatible with your contract");

        //new options compatible with each other
        r.setAnother(b); // a incompatible with b
        when(optionDAO.getIncompatibleWithTariff(new Integer[]{1, 2}, new Integer[]{3})).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleRelationInRange(new Integer[]{1, 2})).thenReturn(Collections.singletonList(r));
        e = assertThrows(ServiceException.class, () -> contractService.addOptions(1, options));
        assertEquals(e.getMessage(), "Option(s) " + a.getName() + " and " + b.getName() + " incompatible with each other");

    }

    @Test
    void deleteOptionTest() {
        //set mocks
        Contract contract = new Contract();
        contract.setOptions(new HashSet<>());
        Option a = new Option();
        a.setName("a");
        a.setId(1);
        Option b = new Option();
        b.setName("b");
        b.setId(2);
        Option c = new Option();
        c.setName("c");
        c.setId(3);
        Option d = new Option();
        d.setName("d");
        d.setId(4);
        OptionRelation r = new OptionRelation(b, a, RELATION.MANDATORY); //b requires a

        Tariff tariff = new Tariff();
        contract.setTariff(tariff);
        contract.getOptions().add(a);

        //check if contract is blocked
        when(contractDAO.findOne(1)).thenReturn(contract);
        contract.setBlockedByAdmin(true);
        ServiceException e = assertThrows(ServiceException.class, () -> contractService.deleteOption(1, 1));
        assertEquals(e.getMessage(), "Contract is blocked");
        contract.setBlockedByAdmin(false);
        contract.setBlocked(true);
        e = assertThrows(ServiceException.class, () -> contractService.deleteOption(1, 1));
        assertEquals(e.getMessage(), "Contract is blocked");
        contract.setBlocked(false);

        //check if option is mandatory for some other
        contract.getOptions().add(b);
        when(optionDAO.findOne(1)).thenReturn(a);
        when(optionDAO.getMandatoryRelation(new Integer[]{2})).thenReturn(Collections.singletonList(r)); //b requires a
        e = assertThrows(ServiceException.class, () -> contractService.deleteOption(1, 1));
        assertEquals(e.getMessage(), "Option(s) " + a.getName() + " is mandatory for other options in your contract: " + b.getName() + ". Delete them first");

        //success
        when(optionDAO.getMandatoryRelation(new Integer[]{2})).thenReturn(Collections.emptyList()); //b requires a, d reuires a, d not in contract//tariff
        assertDoesNotThrow(() -> contractService.deleteOption(1, 1));
        assertFalse(contract.getOptions().contains(a));
    }

    @Test
    void setTariffTest() {
        Contract contract = new Contract();
        when(contractDAO.findOne(1)).thenReturn(contract);

        //check if contract is blocked
        when(contractDAO.findOne(1)).thenReturn(contract);
        contract.setBlockedByAdmin(true);
        ServiceException e = assertThrows(ServiceException.class, () -> contractService.setTariff(1, 1));
        assertEquals(e.getMessage(), "Contract is blocked");
        contract.setBlockedByAdmin(false);
        contract.setBlocked(true);
        e = assertThrows(ServiceException.class, () -> contractService.deleteOption(1, 1));
        assertEquals(e.getMessage(), "Contract is blocked");
        contract.setBlocked(false);

        when(tariffDAO.findOne(1)).thenReturn(new Tariff());
        doNothing().when(contractDAO).update(contract);
        assertDoesNotThrow(() -> contractService.setTariff(1, 1));
        assert contract.getOptions().isEmpty();
    }

    @Test
    void getPaginateDataTest() {
        long total = 10L;
        int perPage = 3;
        int limit = 10;

        List<Contract> contracts = Collections.singletonList(new Contract());
        assertThrows(IllegalArgumentException.class, () -> contractService.getPaginateData(0, 0));
        assertThrows(IllegalArgumentException.class, () -> contractService.getPaginateData(1, -1));
        when(contractDAO.allInRange(0, perPage)).thenReturn(contracts);
        when(contractDAO.count()).thenReturn(total);
        PaginateHelper<Contract> helper = contractService.getPaginateData(null, perPage);
        assert (helper.getItems().equals(contracts));
        assert (helper.getTotal() == limit / perPage + limit % perPage); // 10clients / 3 per page=3+1 page
    }

    @Test
    void getJsonByPhoneTest() {
        //set
        String phone = "123456789";
        Contract contract = new Contract();

        //wrong number format
        assertEquals(contractService.getJsonByPhone(phone), "{\"status\":\"error\",\"message\":\"must be 10 digits\"}");

        //number doesn't exist
        String phone1 = "1234567890";
        when(contractDAO.findByPhone(Long.parseLong(phone1))).thenReturn(null);
        assertEquals(contractService.getJsonByPhone(phone1), "{\"status\":\"error\",\"message\":\"There is no such contract\"}");

        //number exist
        contract.setId(1);
        contract.setOwner(new Client());
        contract.setTariff(new Tariff("tariff"));
        contract.setNumber(Long.parseLong(phone1));
        when(contractDAO.findByPhone(Long.parseLong(phone1))).thenReturn(contract);
        assertEquals(contractService.getJsonByPhone(phone1), "{\"status\":\"success\",\"contract\":{\"id\":1,\"number\":\"1234567890\",\"ownerId\":0,\"ownerName\":\"null null\",\"tariffName\":\"tariff\",\"tariffId\":0,\"optionsIds\":[],\"allOptions\":{},\"allTariffs\":{},\"isBlocked\":false,\"isBlockedByAdmin\":false}}");
    }

    @Test
    void getByPhoneTest() {
        //set
        String phone = "123456789";
        Contract contract = new Contract();
        contract.setId(1);
        contract.setOwner(new Client());
        contract.setTariff(new Tariff("tariff"));
        contract.setNumber(Long.parseLong(phone));
        contract.setOptions(new HashSet<>());


        //wrong number format
        assertThrows(NumberFormatException.class, () -> contractService.getByPhone(phone));

        //null result
        String phone1 = "1234567890";
        when(contractDAO.findByPhone(Long.parseLong(phone1))).thenReturn(null);
        assertNull(contractService.getByPhone(phone1));

        //not null result
        when(contractDAO.findByPhone(Long.parseLong(phone1))).thenReturn(contract);
        assertNotNull(contractService.getByPhone(phone1));
    }
}