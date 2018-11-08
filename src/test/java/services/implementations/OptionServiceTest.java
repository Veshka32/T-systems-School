package services.implementations;

import dao.interfaces.OptionDaoI;
import entities.Option;
import entities.dto.OptionDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    public void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        //set option dto
        OptionDTO dto = new OptionDTO();
        dto.setName(O);

        //create option with reserved name
        dto.setName("t1");
        when(optionDAO.findByName(dto.getName())).thenReturn(new Option());
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "name is reserved");
    }

    @Test
    public void testCheckCompatibility() {
        OptionDTO dto = new OptionDTO();
        dto.setName(O);

        //check if no option at the same time are mandatory and incompatible
        dto.getMandatory().add(B);
        dto.getIncompatible().add(B);
        when(optionDAO.findByName(dto.getName())).thenReturn(null);
        ServiceException e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Option must not be both mandatory and incompatible :b");
        dto.getIncompatible().clear();
        dto.getMandatory().clear();

        //check if all from mandatory also have its' corresponding mandatory options
        dto.getMandatory().add(A);
        when(optionDAO.findByName(dto.getName())).thenReturn(null);
        when(optionDAO.getAllMandatoryNames(new String[]{A})).thenReturn(Arrays.asList(B)); //a requires b
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "More options are required as mandatory: " + Arrays.asList(B).toString());
        dto.getMandatory().clear();

        //check if mandatory relation is not bidirectional
        dto.getMandatory().add(A);
        dto.setId(1);
        when(optionDAO.findByName(A)).thenReturn(null);
        when(optionDAO.getAllMandatoryNames(new String[]{"a"})).thenReturn(Arrays.asList(O)); //b already requires "o1
        when(optionDAO.getMandatoryFor(dto.getId())).thenReturn(Arrays.asList(B));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Option " + dto.getName() + " is already mandatory itself for these options: " + Arrays.asList("b").toString());
        dto.getMandatory().clear();

        //check if mandatory options are incompatible with each other
        dto.getMandatory().add(B);
        dto.getMandatory().add(C);
        when(optionDAO.findByName(dto.getName())).thenReturn(null);
        when(optionDAO.getAllMandatoryNames(new String[]{})).thenReturn(Arrays.asList());
        when(optionDAO.getAllIncompatibleNames(new String[]{B, C})).thenReturn(Arrays.asList(B, C));
        e = assertThrows(ServiceException.class, () -> optionService.create(dto));
        assertEquals(e.getMessage(), "Mandatory options are incompatible with each other");
        dto.getIncompatible().clear();
        dto.getMandatory().clear();
    }
}