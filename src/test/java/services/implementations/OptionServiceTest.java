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
class OptionServiceTest {
    @InjectMocks
    private
    OptionService optionService;

    private String Z = "z";
    private int id = 1;

    @Mock
    private
    OptionDaoI optionDAO;

    @Mock
    private
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
        dto.setName(Z);
        dto.setId(id);
        Option old = new Option();
        old.setName(Z);
        old.setId(2);

        //createClient option with reserved name
        when(optionDAO.findByName(dto.getName())).thenReturn(old);
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "name is reserved");

        //set new name same as old one
        old.setId(id);
        when(optionDAO.findOne(id)).thenReturn(old);
        doNothing().when(relationDaoI).deleteAllIncompatible(id);
        doNothing().when(relationDaoI).deleteAllMandatory(id);
        doNothing().when(optionDAO).update(old);
        assertDoesNotThrow(() -> optionService.update(dto));
    }

    @Test
    void testDelete() {
        when(optionDAO.notUsed(id)).thenReturn(false);
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.delete(id));
        assertEquals(e.getMessage(), "Option is used in contracts/tariffs or is mandatory for another option");

        when(optionDAO.notUsed(id)).thenReturn(true);
        doNothing().when(relationDaoI).deleteAllIncompatible(id);
        doNothing().when(relationDaoI).deleteAllMandatory(id);
        doNothing().when(optionDAO).deleteById(id);
    }

    @Test
    void createTest() {
        //set mocks
        String A = "a";
        String B = "b";
        OptionDTO dto = new OptionDTO();
        OptionRelation relation = new OptionRelation();
        Option a = new Option();
        a.setName("a");
        Option b = new Option();
        b.setName("b");
        Option z = new Option();
        z.setName("z");
        relation.setOne(a);
        relation.setAnother(b); //a requires b
        OptionRelation relation1 = new OptionRelation();
        relation1.setOne(a);
        relation1.setAnother(z); //a requires z

        //createClient option with reserved name
        dto.setName(Z);
        when(optionDAO.findByName(dto.getName())).thenReturn(new Option());
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "name is reserved");

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
        when(optionDAO.getMandatoryFor(new String[]{A})).thenReturn(Collections.singletonList(relation)); //a requires b
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Collections.singletonList(B).toString());
        dto.getMandatory().clear();

        //check if mandatory relation is not bidirectional
        dto.getMandatory().add(A);
        dto.getMandatory().add(B);

        when(optionDAO.getMandatoryFor(new String[]{A, B})).thenReturn(Arrays.asList(relation, relation1)); //a requires b and z
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Option " + dto.getName() + " is already mandatory itself for these options: " + Collections.singletonList(A).toString());
        dto.getMandatory().clear();

        //check if mandatory options are incompatible with each other
        dto.getMandatory().add(A);
        dto.getMandatory().add(B);
        relation.setOne(a);
        relation.setAnother(b); //a incompatible with b
        when(optionDAO.getMandatoryFor(new String[]{A, B})).thenReturn(Collections.emptyList()); //a requires b and z
        when(optionDAO.getIncompatibleFor(new String[]{A, B})).thenReturn(Collections.singletonList(relation));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertTrue(e.getMessage().contains("a and b"));
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