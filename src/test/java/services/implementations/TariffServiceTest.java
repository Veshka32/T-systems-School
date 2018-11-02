package services.implementations;

import entities.Tariff;
import entities.TariffOption;
import entities.dto.TariffDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import repositories.interfaces.TariffDaoI;
import repositories.interfaces.TariffOptionDaoI;
import services.ServiceException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class TariffServiceTest {

    @InjectMocks
    TariffService tariffService;

    @Mock
    TariffDaoI tariffDAO;

    @Mock
    TariffOptionDaoI optionDAO;

    @BeforeEach
    public void createMocks() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        TariffDTO dto = new TariffDTO();

        //create tariff with reserved name
        dto.setName("t1");
        when(tariffDAO.isNameExist(dto.getName())).thenReturn(true);
        assertThrows(ServiceException.class, () -> tariffService.create(dto), "name is reserved");
    }

    @Test
    public void testCheckCompatibility() {
        TariffDTO dto = new TariffDTO();

        //add option a without its mandatory options
        dto.getOptions().add("a");
        when(tariffDAO.isNameExist(dto.getName())).thenReturn(false);
        when(optionDAO.getAllMandatoryNames(new String[]{"a"})).thenReturn(Arrays.asList("b"));
        ServiceException e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Arrays.asList("b"));
        dto.getOptions().clear();

        //add a with incompatible options b
        dto.getOptions().add("a");
        dto.getOptions().add("b");
        when(tariffDAO.isNameExist("t2")).thenReturn(false);
        when(optionDAO.getAllMandatoryNames(new String[]{"a", "b"})).thenReturn(Arrays.asList());
        when(optionDAO.getAllIncompatibleNames(new String[]{"a", "b"})).thenReturn(Arrays.asList("a", "b"));
        e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertEquals(e.getMessage(), "Selected options are incompatible with each other");

        //add only b
        dto.getOptions().clear();
        dto.getOptions().add("b");
        when(tariffDAO.isNameExist("t2")).thenReturn(false);
        when(optionDAO.getAllMandatoryNames(new String[]{"b"})).thenReturn(Arrays.asList());
        when(optionDAO.getAllIncompatibleNames(new String[]{"b"})).thenReturn(Arrays.asList());
        when(optionDAO.findByName("b")).thenReturn(new TariffOption());
        when(tariffDAO.save(new Tariff())).thenReturn(1);
    }
}