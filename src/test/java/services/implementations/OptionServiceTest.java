package services.implementations;

import entities.TariffOption;
import entities.dto.TariffOptionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import repositories.interfaces.TariffDaoI;
import repositories.interfaces.TariffOptionDaoI;
import services.ServiceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class OptionServiceTest {
    @InjectMocks
    OptionService optionService;

    TariffOption a=new TariffOption();
    TariffOption b=new TariffOption();
    TariffOption c=new TariffOption();

    @Mock
    TariffOptionDaoI optionDAO;

    @BeforeEach
    void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);

        //create sample options
        b.setName("b");
        c.setName("c");
        b.getIncompatibleOptions().add(c); //b incompatible with c
        c.getIncompatibleOptions().add(b); //c incompatible with b
    }


    @Test
    void create() {
        //set option dto
        TariffOptionDTO dto=new TariffOptionDTO();
        dto.setName("o1");

        //create option with reserved name
        dto.setName("t1");
        when(optionDAO.isNameExist(dto.getName())).thenReturn(true);
        assertThrows(ServiceException.class,()->optionService.create(dto),"name is reserved");

        //check if no option at the same time are mandatory and incompatible
        dto.getMandatory().add(b.getName());
        dto.getIncompatible().add(b.getName());
        when(optionDAO.isNameExist(dto.getName())).thenReturn(false);
        assertThrows(ServiceException.class,()->optionService.create(dto),"Option must not be both mandatory and incompatible");
        dto.getIncompatible().clear();
        dto.getMandatory().clear();

        //add incompatible options as mandatory for new one
        dto.getMandatory().add(c.getName());
        dto.getMandatory().add(b.getName());
        when(optionDAO.isNameExist(dto.getName())).thenReturn(false);
        when(optionDAO.findByName("b")).thenReturn(b);
        when(optionDAO.findByName("c")).thenReturn(c);
        assertThrows(ServiceException.class,()->optionService.create(dto),"Mandatory optionsNames are incompatible");
    }
}