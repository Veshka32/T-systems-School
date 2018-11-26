package services.implementations;

import dao.implementations.ContractDAO;
import dao.implementations.OptionDAO;
import dao.implementations.TariffDAO;
import model.dto.ContractDTO;
import model.entity.Contract;
import model.entity.Option;
import model.entity.OptionRelation;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ContractServiceTest {
    @InjectMocks
    private
    ContractService contractService;

    @Mock
    private
    ContractDAO contractDAO;

    @Mock
    private
    OptionDAO optionDAO;

    @Mock
    private
    TariffDAO tariffDAO;

    @BeforeEach
    void before() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void checkCompatibilityTest() {
        ContractDTO dto = new ContractDTO(1);
        dto.setTariffId(1);
        Tariff tariff = new Tariff();
        tariff.setId(1);
        dto.setOptionsIds(new HashSet<>(Arrays.asList(1, 2)));
        dto.setTariffName("test");

        OptionRelation relation = new OptionRelation();
        Option a = new Option();
        a.setName("a");
        Option b = new Option();
        b.setName("b");

        relation.setOne(a);
        relation.setAnother(b); //a requires b

        //check if all options has its' mandatory
        when(tariffDAO.findOne(1)).thenReturn(tariff);

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
        int id = 1;
        Contract contract = new Contract();
        contract.setTariff(new Tariff());
        Option a = new Option();
        a.setName("a");
        Option b = new Option();
        b.setName("b");
        List<Option> options = Arrays.asList(a, b);

        //check if contract is blocked
        contract.setBlockedByAdmin(true);
        when(contractDAO.findOne(id)).thenReturn(contract);
        assertDoesNotThrow(() -> contractService.addOptions(id, options));
        /**
         *
         */
    }

    @Test
    void deleteOptionTest() {
        int id = 1;
        Contract contract = new Contract();
        contract.setTariff(new Tariff());
        Option a = new Option();
        a.setId(1);
        a.setName("a");
        Option b = new Option();
        b.setId(2);
        b.setName("b");
        contract.getOptions().add(a);
        contract.getOptions().add(b);

        //check if contract is blocked
        contract.setBlockedByAdmin(true);
        when(contractDAO.findOne(id)).thenReturn(contract);
        assertDoesNotThrow(() -> contractService.deleteOption(id, id));
        contract.setBlockedByAdmin(false);
        /**
         * todo
         */
    }

    @Test
    void setTariffTest() {
        int id = 1;
        Contract contract = new Contract();
        when(contractDAO.findOne(id)).thenReturn(contract);

        //check if contract is blocked
        contract.setBlockedByAdmin(true);
        when(contractDAO.findOne(id)).thenReturn(contract);
        assertDoesNotThrow(() -> contractService.setTariff(id, id));
        contract.setBlockedByAdmin(false);

        when(tariffDAO.findOne(id)).thenReturn(new Tariff());
        doNothing().when(contractDAO).update(contract);
        contractService.setTariff(id, id);
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
}