package services;

import dao.interfaces.OptionDaoI;
import dao.interfaces.TariffDaoI;
import model.dto.TariffDTO;
import model.entity.Option;
import model.entity.OptionRelation;
import model.entity.Tariff;
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
import services.implementations.TariffService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    void getDto() {
        //set mocks
        Tariff t = new Tariff();
        t.setId(1);
        t.setName("test");
        t.setPrice(BigDecimal.ZERO);
        t.setDescription("description");
        Option a = new Option();
        a.setId(1);
        a.setName("a");
        Option b = new Option();
        b.setId(2);
        b.setName("b");
        t.getOptions().add(a);
        t.getOptions().add(b);

        when(tariffDAO.findOne(1)).thenReturn(t);
        TariffDTO dto = tariffService.getDto(1);
        assertEquals(t.getId(), (int) dto.getId());
        assertEquals(t.getName(), dto.getName());
        assertEquals(t.getDescription(), dto.getDescription());
        assertEquals(dto.getOptions(), new HashSet<>(Arrays.asList(1, 2)));
        assertEquals(dto.getOptionsNames(), Stream.of(a.getName(), b.getName()).collect(Collectors.joining(", ")));
    }

    @Test
    void testCheckCompatibility() {
        //set mocks
        Tariff self = new Tariff();
        self.setId(1);
        self.setName("dto");
        TariffDTO dto = new TariffDTO();
        dto.setId(1);
        dto.setName(self.getName());

        Option a = new Option();
        a.setId(1);
        a.setName("a");
        Option b = new Option();
        b.setId(2);
        b.setName("b");
        Option c = new Option();
        c.setId(3);
        c.setName("c");

        dto.getOptions().add(a.getId());
        dto.getOptions().add(b.getId());

        OptionRelation relation = new OptionRelation(a, c, RELATION.MANDATORY);  //a requires c

        //check if all from mandatory also have its' corresponding mandatory options
        when(tariffDAO.findByName(dto.getName())).thenReturn(null);
        when(optionDAO.getMandatoryRelation((dto.getOptions().toArray(new Integer[]{})))).thenReturn(Collections.singletonList(relation)); //a requires c
        ServiceException e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + c.getName());

        //check if mandatory options are incompatible with each other
        relation.setOne(a);
        relation.setAnother(b);
        relation.setRelation(RELATION.INCOMPATIBLE); //a incompatible with b
        when(optionDAO.getMandatoryRelation((dto.getOptions().toArray(new Integer[]{})))).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleRelationInRange((dto.getOptions().toArray(new Integer[]{})))).thenReturn(Collections.singletonList(relation));
        e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertTrue(e.getMessage().contains("a and b"));

        //check if mandatory options are incompatible with each other
        relation.setOne(b);
        relation.setAnother(a);
        e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
        assertTrue(e.getMessage().contains("b and a"));
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