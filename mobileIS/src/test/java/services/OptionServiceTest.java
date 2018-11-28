package services;

import dao.interfaces.OptionDaoI;
import dao.interfaces.RelationDaoI;
import model.dto.OptionDTO;
import model.entity.Option;
import model.entity.OptionRelation;
import model.enums.RELATION;
import model.helpers.PaginateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import services.implementations.OptionService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    private String Z = "z";
    private int id = 1;

    @InjectMocks
    private OptionService optionService;

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
    void getDto() {
        //set mocks
        Option a = new Option();
        a.setId(1);
        a.setName("a");
        Option b = new Option();
        b.setId(2);
        b.setName("b");
        Option c = new Option();
        c.setId(3);
        c.setName("c");
        Option d = new Option();
        d.setId(4);
        d.setName("d");

        OptionRelation m = new OptionRelation(a, b, RELATION.MANDATORY);
        OptionRelation i = new OptionRelation(c, a, RELATION.INCOMPATIBLE);
        OptionRelation i1 = new OptionRelation(a, d, RELATION.INCOMPATIBLE);

        when(optionDAO.findOne(1)).thenReturn(a);
        when(optionDAO.getMandatoryRelation(new Integer[]{1})).thenReturn(Collections.singletonList(m));
        when(optionDAO.getIncompatibleRelationInRange(new Integer[]{1})).thenReturn(Arrays.asList(i, i1));
        OptionDTO dto = optionService.getDto(1);
        assertEquals(dto.getName(), a.getName());
        assertEquals((int) dto.getId(), a.getId());
        assertEquals(dto.getMandatory(), new HashSet<>(Collections.singletonList(b.getId())));
        assertEquals(dto.getMandatoryNames(), Stream.of(b.getName()).collect(Collectors.joining(", ")));
        assertEquals(dto.getIncompatible(), new HashSet<>(Arrays.asList(c.getId(), d.getId())));
        assertEquals(dto.getIncompatibleNames(), Stream.of(c.getName(), d.getName()).collect(Collectors.joining(", ")));
    }


    @Test
    void createTest() {
        //set mocks
        OptionDTO dto = new OptionDTO();
        dto.setName(Z);

        //create option with reserved name
        when(optionDAO.findByName(dto.getName())).thenReturn(Optional.of(new Option()));
        Optional<String> e = optionService.create(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "name is reserved");

        //create option with free name
        when(optionDAO.findByName(dto.getName())).thenReturn(Optional.empty());
        assertFalse(optionService.create(dto).isPresent());
    }

    @Test
    void checkCompatibilityTest() {
        //set mocks
        Option self = new Option();
        self.setId(2);
        self.setName("dto");
        OptionDTO dto = new OptionDTO(self);

        Option a = new Option();
        a.setId(1);
        a.setName("a");
        Option b = new Option();
        b.setId(3);
        b.setName("b");
        OptionRelation relation = new OptionRelation(a, b, RELATION.MANDATORY);  //a requires b
        OptionRelation bi = new OptionRelation(a, self, RELATION.MANDATORY); //a requires dto

        //check if no option at the same time are mandatory and incompatible
        dto.getMandatory().add(1);
        dto.getIncompatible().add(1);
        when(optionDAO.findByName(dto.getName())).thenReturn(Optional.empty());
        when(optionDAO.findOne(1)).thenReturn(a);
        Optional<String> e = optionService.create(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "Option must not be both mandatory and incompatible: " + a.getName());

        //check if mandatory relation is not bidirectional
        dto.getIncompatible().clear();
        when(optionDAO.getMandatoryRelation((dto.getMandatory().toArray(new Integer[]{})))).thenReturn(Collections.singletonList(bi)); //a requires dto
        e = optionService.create(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "Option " + dto.getName() + " is already mandatory itself for these options: " + a.getName());

        //check if all from mandatory also have its' corresponding mandatory options
        when(optionDAO.getMandatoryRelation((dto.getMandatory().toArray(new Integer[]{})))).thenReturn(Collections.singletonList(relation)); //1 requires 3
        e = optionService.create(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "More options are required as mandatory: " + b.getName());

        //check if mandatory options are incompatible with each other
        dto.getIncompatible().clear();
        dto.getMandatory().add(3);
        when(optionDAO.getMandatoryRelation((dto.getMandatory().toArray(new Integer[]{})))).thenReturn(Collections.emptyList());
        when(optionDAO.getIncompatibleRelationInRange((dto.getMandatory().toArray(new Integer[]{})))).thenReturn(Collections.singletonList(relation));
        e = optionService.create(dto);
        assertTrue(e.isPresent());
        assertTrue(e.get().contains("a and b"));
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
        when(optionDAO.findByName(dto.getName())).thenReturn(Optional.of(old));
        Optional<String> e = optionService.create(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "name is reserved");

        //set new name same as old one
        old.setId(id);
        when(optionDAO.findOne(id)).thenReturn(old);
        doNothing().when(relationDaoI).deleteAllIncompatible(id);
        doNothing().when(relationDaoI).deleteAllMandatory(id);
        doNothing().when(optionDAO).update(old);
        assertFalse(optionService.update(dto).isPresent());

        //set completely new name
        when(optionDAO.findByName(dto.getName())).thenReturn(Optional.empty());
        when(optionDAO.findOne(id)).thenReturn(old);
        doNothing().when(relationDaoI).deleteAllIncompatible(id);
        doNothing().when(relationDaoI).deleteAllMandatory(id);
        doNothing().when(optionDAO).update(old);
        assertFalse(optionService.update(dto).isPresent());
    }

    @Test
    void testDelete() {
        when(optionDAO.notUsed(id)).thenReturn(false);
        Optional<String> e = optionService.delete(id);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "Option is used in contracts/tariffs or is mandatory for another option");

        when(optionDAO.notUsed(id)).thenReturn(true);
        doNothing().when(relationDaoI).deleteAllIncompatible(id);
        doNothing().when(relationDaoI).deleteAllMandatory(id);
        doNothing().when(optionDAO).deleteById(id);
        assertFalse(optionService.delete(id).isPresent());
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