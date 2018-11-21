package services.implementations;

import dao.interfaces.OptionDaoI;
import dao.interfaces.TariffDaoI;
import model.dto.TariffDTO;
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
import services.exceptions.ServiceException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class TariffServiceTest {

    @InjectMocks
    private
    TariffService tariffService;

    @Mock
    private
    TariffDaoI tariffDAO;

    @Mock
    private
    OptionDaoI optionDAO;

    @BeforeEach
    void createMocks() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreate() {
        TariffDTO dto = new TariffDTO();
        dto.setName("t1");

        //create tariff with reserved name
        when(tariffDAO.findByName(dto.getName())).thenReturn(new Tariff());
        assertThrows(ServiceException.class, () -> tariffService.create(dto), "name is reserved");

        //create tariff with free name
        when(tariffDAO.findByName(dto.getName())).thenReturn(null);
        assertDoesNotThrow(() -> tariffService.create(dto));
    }

    @Test
    void testCheckCompatibility() {
        //set mocks
        String A = "a";
        String B = "b";

        TariffDTO dto = new TariffDTO();
        OptionRelation relation = new OptionRelation();
        Option a = new Option();
        a.setId(1);
        a.setName(A);
        Option b = new Option();
        b.setId(3);
        b.setName(B);
        relation.setOne(a);
        relation.setAnother(b); //a requires b

        //check if all from mandatory also have its' corresponding mandatory options
        dto.getOptions().add(1);
        when(optionDAO.getMandatoryIdsFor(new Integer[]{1})).thenReturn(Collections.singletonList(3)); //1 requires 3
        when(optionDAO.getNamesByIds(new Integer[]{3})).thenReturn(Collections.singletonList(A));
        ServiceException e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Collections.singletonList(A).toString());

        //check if mandatory options are incompatible with each other
        dto.getOptions().add(3);
        relation.setOne(a);
        relation.setAnother(b); //1 incompatible with 3
        when(optionDAO.getMandatoryIdsFor(new Integer[]{1, 3})).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleRelation(new Integer[]{1, 3})).thenReturn(Collections.singletonList(relation));
        e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertTrue(e.getMessage().contains("a and b"));
    }

    @Test
    void updateTest() {
        //set option dto
        TariffDTO dto = new TariffDTO();
        dto.setName("z");
        dto.setId(1);
        Tariff old = new Tariff();
        old.setName("z");
        old.setId(2);

        //create tariff with reserved name
        when(tariffDAO.findByName(dto.getName())).thenReturn(old);
        ServiceException e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertEquals(e.getMessage(), "name is reserved");

        //set new name same as old one
        old.setId(1);
        when(tariffDAO.findOne(1)).thenReturn(old);
        doNothing().when(tariffDAO).update(old);
        assertDoesNotThrow(() -> tariffDAO.update(old));

        //set completely new name
        when(tariffDAO.findByName(dto.getName())).thenReturn(null);
        when(tariffDAO.findOne(1)).thenReturn(old);
        doNothing().when(tariffDAO).update(old);
        assertDoesNotThrow(() -> tariffService.update(dto));
    }

    @Test
    void delete() {
        when(tariffDAO.isUsed(1)).thenReturn(true);
        assertThrows(ServiceException.class, () -> tariffService.delete(1));
    }

    @Test
    void getInRange() {
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