package services.implementations;

import entities.TariffOption;
import entities.dto.TariffOptionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import repositories.interfaces.TariffOptionDaoI;
import services.ServiceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class OptionServiceTest {
    @InjectMocks
    OptionService optionService;

    TariffOption a=new TariffOption();
    TariffOption b=new TariffOption();
    TariffOption c=new TariffOption();

    @Mock
    TariffOptionDaoI optionDAO;

    @BeforeEach
    public void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);

        //create sample options
        a.setName("a");
        b.setName("b");
        c.setName("c");
        b.getIncompatibleOptions().add(c); //b incompatible with c
        c.getIncompatibleOptions().add(b); //c incompatible with b
    }

    @Test
    public void testCreate() {
        //set option dto
        TariffOptionDTO dto=new TariffOptionDTO();
        dto.setName("o1");

        //create option with reserved name
        dto.setName("t1");
        when(optionDAO.isNameExist(dto.getName())).thenReturn(true);
        ServiceException e=assertThrows(ServiceException.class,()->optionService.create(dto));
        assertEquals(e.getMessage(),"name is reserved");

        //check if no option at the same time are mandatory and incompatible
        dto.getMandatory().add(b.getName());
        dto.getIncompatible().add(b.getName());
        when(optionDAO.isNameExist(dto.getName())).thenReturn(false);
        e=assertThrows(ServiceException.class,()->optionService.create(dto));
        assertEquals(e.getMessage(),"Option must not be both mandatory and incompatible");
        dto.getIncompatible().clear();
        dto.getMandatory().clear();

        //add incompatible options as mandatory for new one
        dto.getMandatory().add(c.getName());
        dto.getMandatory().add(b.getName());
        when(optionDAO.isNameExist(dto.getName())).thenReturn(false);
        when(optionDAO.findByName("b")).thenReturn(b);
        when(optionDAO.findByName("c")).thenReturn(c);
        e=assertThrows(ServiceException.class,()->optionService.create(dto));
        assertEquals(e.getMessage(),"Mandatory optionsNames are incompatible");
        dto.getIncompatible().clear();
        dto.getMandatory().clear();

        //find second level mandatory
        c.getMandatoryOptions().add(a);
        dto.getMandatory().add(c.getName());
        when(optionDAO.isNameExist(dto.getName())).thenReturn(false);
        when(optionDAO.findByName("a")).thenReturn(a);
        when(optionDAO.findByName("c")).thenReturn(c);
        e=assertThrows(ServiceException.class,()->optionService.create(dto));
        assertEquals(e.getMessage(),"With this mandatory options also must set up "+a.getName());
        dto.getIncompatible().clear();
        dto.getMandatory().clear();
    }
}