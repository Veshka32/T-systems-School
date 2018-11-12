package services.implementations;

import dao.interfaces.OptionDaoI;
import dao.interfaces.TariffDaoI;
import model.dto.TariffDTO;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class TariffServiceTest {

    @InjectMocks
    TariffService tariffService;

    @Mock
    TariffDaoI tariffDAO;

    @Mock
    OptionDaoI optionDAO;

    @BeforeEach
    void createMocks() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreate() {
        TariffDTO dto = new TariffDTO();

        //createClient tariff with reserved name
        dto.setName("t1");
        when(tariffDAO.isNameExist(dto.getName())).thenReturn(true);
        assertThrows(ServiceException.class, () -> tariffService.create(dto), "name is reserved");
    }

    @Test
    void testCheckCompatibility() {
        TariffDTO dto = new TariffDTO();

        //add option a without its' mandatory options
        dto.getOptions().add("a");
        when(tariffDAO.isNameExist(dto.getName())).thenReturn(false);
        when(optionDAO.getAllMandatoryNames(new String[]{"a"})).thenReturn(Collections.singletonList("b"));
        ServiceException e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Collections.singletonList("b"));
        dto.getOptions().clear();

        //add a with incompatible options b
        dto.getOptions().add("a");
        dto.getOptions().add("b");
        when(optionDAO.getAllMandatoryNames(new String[]{"a", "b"})).thenReturn(Collections.emptyList());
        when(optionDAO.getAllIncompatibleNames(new String[]{"a", "b"})).thenReturn(Arrays.asList("a", "b"));
        e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertEquals(e.getMessage(), "Selected options are incompatible with each other");

        //add only b
        dto.getOptions().clear();
        dto.getOptions().add("b");
        when(optionDAO.getAllMandatoryNames(new String[]{"b"})).thenReturn(Collections.emptyList());
        when(optionDAO.getAllIncompatibleNames(new String[]{"b"})).thenReturn(Collections.emptyList());
        when(optionDAO.findByName("b")).thenReturn(new Option());
        assertDoesNotThrow(() -> tariffService.create(dto));
    }

    @Test
    void updateTest() {
        Tariff t = new Tariff();
        t.setId(2);
        TariffDTO dto = new TariffDTO();
        dto.setId(1);
        dto.setName("t");

        //check if name is reserved
        when(tariffDAO.findByName("t")).thenReturn(t);
        assertThrows(ServiceException.class, () -> tariffService.update(dto));
    }

    @Test
    void delete() {
        when(tariffDAO.isUsed(1)).thenReturn(true);
        assertThrows(ServiceException.class, () -> tariffService.delete(1));
    }

    @Test
    void getPaginateData() {
        long total = 10L;
        int perPage = 3;
        int limit = 10;

        List<Tariff> tariffs = Collections.singletonList(new Tariff());
        assertThrows(IllegalArgumentException.class, () -> tariffService.getPaginateData(0, 0));
        assertThrows(IllegalArgumentException.class, () -> tariffService.getPaginateData(1, -1));
        when(tariffDAO.allInRange(0, perPage)).thenReturn(tariffs);
        when(tariffDAO.count()).thenReturn(total);
        PaginateHelper<Tariff> helper = tariffService.getPaginateData(null, perPage);
        assert (helper.getItems().equals(tariffs));
        assert (helper.getTotal() == limit / perPage + limit % perPage); // 10clients / 3 per page=3+1 page
    }
}