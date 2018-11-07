package services.implementations;

import dao.implementations.ContractDAO;
import dao.implementations.OptionDAO;
import entities.dto.ContractDTO;
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
import java.util.HashSet;
import java.util.List;

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

    @BeforeEach
    void before() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void checkCompatibilityTest() {
        ContractDTO dto = new ContractDTO();
        dto.setOptionsNames(new HashSet<>(Arrays.asList("a", "b", "c", "d")));
        List<String> optionsInTariff = Arrays.asList("a", "b");
        when(optionDAO.getOptionsInTariffNames(1)).thenReturn(optionsInTariff);


    }

    @Test
    void addOptions() {
    }

    @Test
    void deleteOption() {
    }
}