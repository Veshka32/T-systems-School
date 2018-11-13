package services.implementations;

import dao.interfaces.OptionDaoI;
import dao.interfaces.RelationDaoI;
import model.dto.OptionDTO;
import model.entity.Option;
import model.entity.OptionRelation;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class OptionServiceTest {
    @InjectMocks
    OptionService optionService;

    String A = "a";
    String B = "b";
    String C = "c";
    String O = "o1";

    @Mock
    OptionDaoI optionDAO;

    @Mock
    RelationDaoI relationDaoI;

    @BeforeEach
    void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpdate() {
        //set option dto
        OptionDTO dto = new OptionDTO();
        dto.setName(O);
        dto.setId(1);
        Option old = new Option();
        old.setName(O);
        old.setId(2);

        //createClient option with reserved name
        when(optionDAO.findByName(dto.getName())).thenReturn(old);
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "name is reserved");

        //set new name same as old one
        old.setId(1);
        when(optionDAO.findOne(1)).thenReturn(old);
        doNothing().when(relationDaoI).deleteAllIncompatible(1);
        doNothing().when(relationDaoI).deleteAllMandatory(1);
        doNothing().when(optionDAO).update(old);
        assertDoesNotThrow(() -> optionService.update(dto));
    }

    @Test
    void testDelete() {
        when(optionDAO.notUsed(1)).thenReturn(false);
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.delete(1));
        assertEquals(e.getMessage(), "Option is used in contracts/tariffs or is mandatory for another option");

        when(optionDAO.notUsed(1)).thenReturn(true);
        doNothing().when(relationDaoI).deleteAllIncompatible(1);
        doNothing().when(relationDaoI).deleteAllMandatory(1);
        doNothing().when(optionDAO).deleteById(1);
    }

    @Test
    void createTest() {
        //set option dto
        OptionDTO dto = new OptionDTO();

        //createClient option with reserved name
        dto.setName("t1");
        when(optionDAO.findByName(dto.getName())).thenReturn(new Option());
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "name is reserved");

        dto.setName(O);

        //check if no option at the same time are mandatory and incompatible
        dto.getMandatory().add(B);
        dto.getIncompatible().add(B);
        when(optionDAO.findByName(dto.getName())).thenReturn(null);
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Option must not be both mandatory and incompatible :b");
        dto.getIncompatible().clear();
        dto.getMandatory().clear();

        //check if all from mandatory also have its' corresponding mandatory options
        dto.getMandatory().add(A);
        when(optionDAO.getAllMandatoryNames(new String[]{A})).thenReturn(Collections.singletonList(B)); //a requires b
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Collections.singletonList(B).toString());
        dto.getMandatory().clear();

        //check if mandatory relation is not bidirectional
        dto.getMandatory().add(A);
        dto.getMandatory().add(B);
        dto.setId(1);

        when(optionDAO.getAllMandatoryNames(new String[]{A, B})).thenReturn(Arrays.asList(O, A)); //b already requires a and o1
        when(optionDAO.isMandatoryFor(dto.getId(), new String[]{A, B})).thenReturn(Collections.singletonList(B));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Option " + dto.getName() + " is already mandatory itself for these options: " + Collections.singletonList(B).toString());
        dto.getMandatory().clear();

        //check if mandatory options are incompatible with each other
        dto.getMandatory().add(B);
        dto.getMandatory().add(C);
        OptionRelation r = new OptionRelation();
        Option c = new Option();
        c.setName(C);
        Option b = new Option();
        b.setName(B);
        r.setOne(b);
        r.setAnother(c);
        when(optionDAO.getIncompatibleFor(new String[]{B, C})).thenReturn(Collections.singletonList(r));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertTrue(e.getMessage().contains("b and c"));
    }

    @Test
    void getPaginateDataTest() {
        long total = 10L;
        int perPage = 3;
        int limit = 10;

        List<Option> optionList = Collections.singletonList(new Option());
        assertThrows(IllegalArgumentException.class, () -> optionService.getPaginateData(0, 0));
        assertThrows(IllegalArgumentException.class, () -> optionService.getPaginateData(1, -1));
        when(optionDAO.allInRange(0, perPage)).thenReturn(optionList);
        when(optionDAO.count()).thenReturn(total);
        PaginateHelper<Option> helper = optionService.getPaginateData(null, perPage);
        assert (helper.getItems().equals(optionList));
        assert (helper.getTotal() == limit / perPage + limit % perPage); // 10clients / 3 per page=3+1 page
    }
}