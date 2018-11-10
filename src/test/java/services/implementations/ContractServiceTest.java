package services.implementations;

import dao.implementations.ContractDAO;
import dao.implementations.OptionDAO;
import dao.implementations.TariffDAO;
import model.dto.ContractDTO;
import model.entity.Contract;
import model.entity.Option;
import model.entity.Tariff;
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
import services.ServiceException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ContractServiceTest {
    @InjectMocks
    ContractService contractService;

    @Mock
    ContractDAO contractDAO;

    @Mock
    OptionDAO optionDAO;

    @Mock
    TariffDAO tariffDAO;

    @BeforeEach
    void before() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void updateTest() {
        ContractDTO dto = new ContractDTO();
        Tariff tariff = new Tariff();
        tariff.setId(1);
        dto.setOptionsNames(new HashSet<>(Arrays.asList("a", "b", "c")));
        dto.setTariffName("test");
        List<String> optionsInTariff = Arrays.asList("a", "b", "d");

        //check if all options has its' mandatory
        when(tariffDAO.findByName("test")).thenReturn(tariff);
        when(optionDAO.getOptionsInTariffNames(1)).thenReturn(optionsInTariff);
        when(optionDAO.getAllMandatoryNames(new String[]{"c"})).thenReturn(Collections.singletonList("e")); // d is mandatory for someone
        ServiceException e = assertThrows(ServiceException.class, () -> contractService.update(dto));
        assertTrue(e.getMessage().contains("More options are required as mandatory: "));

        //check if all options are compatible with each other
        when(optionDAO.getAllMandatoryNames(new String[]{"c"})).thenReturn(Collections.emptyList());
        when(optionDAO.getAllIncompatibleNames(new String[]{"c"})).thenReturn(Collections.singletonList("d")); //someone are incompatible with d
        e = assertThrows(ServiceException.class, () -> contractService.update(dto));
        assertTrue(e.getMessage().contains(" are incompatible with each other or with tariff"));
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
    void unblock() {
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
        int id = 1;
        Contract contract = new Contract();
        contract.setTariff(new Tariff());
        Option a = new Option();
        a.setName("a");
        Option b = new Option();
        b.setName("b");
        List<Option> options = Arrays.asList(a, b);

        //check if all options has its' mandatory
        when(contractDAO.findOne(id)).thenReturn(contract);
        when(optionDAO.getAllMandatoryNames(new String[]{"a", "b"})).thenReturn(Collections.singletonList("c")); // one of the options requires "c";
        ServiceException e = assertThrows(ServiceException.class, () -> contractService.addOptions(id, options));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Collections.singletonList("c").toString());

        //check if all options are compatible with each other
        Option c = new Option();
        c.setName("c");
        contract.getOptions().add(c);
        when(optionDAO.getAllMandatoryNames(new String[]{"a", "b"})).thenReturn(Collections.emptyList());
        when(optionDAO.getAllIncompatibleNames(new String[]{"a", "b"})).thenReturn(Collections.singletonList("c")); //one of the option incompatible with "c";
        assertThrows(ServiceException.class, () -> contractService.addOptions(id, options));
    }

    @Test
    void deleteOption() {
        int id = 1;
        Contract contract = new Contract();
        contract.setTariff(new Tariff());
        Option a = new Option();
        a.setName("a");
        Option b = new Option();
        b.setName("b");
        contract.getOptions().add(a);
        contract.getOptions().add(b);

        //check if option is mandatory for some other
        when(contractDAO.findOne(id)).thenReturn(contract);
        when(optionDAO.findOne(id)).thenReturn(a); //try to delete a
        when(optionDAO.getMandatoryFor(1)).thenReturn(Collections.singletonList("b")); //a requires b
        assertThrows(ServiceException.class, () -> contractService.deleteOption(id, id));
    }

    @Test
    void setTariffTest() {
        Contract contract = new Contract();
        when(contractDAO.findOne(1)).thenReturn(contract);
        when(tariffDAO.findOne(1)).thenReturn(new Tariff());
        doNothing().when(contractDAO).update(contract);
        contractService.setTariff(1, 1);
        assert contract.getOptions().isEmpty();
    }

    @Test
    void getPaginateData() {
        List<Contract> contracts = Collections.singletonList(new Contract());
        assertThrows(IllegalArgumentException.class, () -> contractService.getPaginateData(0, 0));
        assertThrows(IllegalArgumentException.class, () -> contractService.getPaginateData(1, -1));
        when(contractDAO.allInRange(0, 3)).thenReturn(contracts);
        when(contractDAO.count()).thenReturn(10L);
        PaginateHelper<Contract> helper = contractService.getPaginateData(null, 3);
        assert (helper.getItems().equals(contracts));
        assert (helper.getTotal() == 4);
    }
}