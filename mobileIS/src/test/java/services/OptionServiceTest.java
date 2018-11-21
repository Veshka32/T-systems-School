package services;

import dao.interfaces.OptionDaoI;
import dao.interfaces.RelationDaoI;
import model.dto.OptionDTO;
import model.entity.Option;
import model.entity.OptionRelation;
import model.helpers.PaginateHelper;
import org.junit.jupiter.api.Assertions;
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
import services.implementations.OptionService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class OptionServiceTest {
    @InjectMocks
    private OptionService optionService;

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
    void createTest() {
        //set mocks
        OptionDTO dto = new OptionDTO();
        dto.setName(Z);

        //create option with reserved name
        when(optionDAO.findByName(dto.getName())).thenReturn(new Option());
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "name is reserved");

        //create option with free name
        when(optionDAO.findByName(dto.getName())).thenReturn(null);
        Assertions.assertDoesNotThrow(() -> optionService.create(dto));
    }

    @Test
    void checkCompatibilityTest() {
        //set mocks
        String A = "a";
        String B = "b";
        OptionDTO dto = new OptionDTO();
        dto.setName(A);
        dto.setId(2);

        OptionRelation relation = new OptionRelation();
        Option a = new Option();
        a.setId(1);
        a.setName(A);
        Option b = new Option();
        b.setId(3);
        b.setName(B);
        relation.setOne(a);
        relation.setAnother(b); //a requires b

        //check if no option at the same time are mandatory and incompatible
        dto.getMandatory().add(1);
        dto.getIncompatible().add(1);
        when(optionDAO.findByName(dto.getName())).thenReturn(null);
        when(optionDAO.getNamesByIds(new Integer[]{1})).thenReturn(Collections.singletonList(A));
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Option must not be both mandatory and incompatible: " + Collections.singletonList(A).toString());

        //check if mandatory relation is not bidirectional
        dto.getIncompatible().clear();
        when(optionDAO.getMandatoryIdsFor(new Integer[]{1})).thenReturn(Collections.singletonList(dto.getId())); //1 requires dto
        when(optionDAO.getNamesByIds(new Integer[]{dto.getId()})).thenReturn(Collections.singletonList(A));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Option " + dto.getName() + " is already mandatory itself for these options: " + Collections.singletonList(A).toString());

        //check if all from mandatory also have its' corresponding mandatory options
        when(optionDAO.getMandatoryIdsFor(new Integer[]{1})).thenReturn(Collections.singletonList(3)); //1 requires 3
        when(optionDAO.getNamesByIds(new Integer[]{3})).thenReturn(Collections.singletonList(A));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Collections.singletonList(A).toString());

        //check if mandatory options are incompatible with each other
        dto.getIncompatible().clear();
        dto.getMandatory().add(3);
        relation.setOne(a);
        relation.setAnother(b); //1 incompatible with 3
        when(optionDAO.getMandatoryIdsFor(new Integer[]{1, 3})).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleRelation(new Integer[]{1, 3})).thenReturn(Collections.singletonList(relation));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertTrue(e.getMessage().contains("a and b"));
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

        //createAccount option with reserved name
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

        //set completely new name
        when(optionDAO.findByName(dto.getName())).thenReturn(null);
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
        assertDoesNotThrow(() -> optionService.delete(id));
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