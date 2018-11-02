//package services.implementations;
//
//import entities.Tariff;
//import entities.TariffOption;
//import entities.dto.TariffDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import repositories.interfaces.TariffDaoI;
//import repositories.interfaces.TariffOptionDaoI;
//import services.ServiceException;
//
//import java.util.HashSet;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.WARN)
//public class TariffServiceTest {
//
//    @InjectMocks
//    TariffService tariffService;
//
//    TariffOption a=new TariffOption();
//    TariffOption b=new TariffOption();
//    TariffOption c=new TariffOption();
//
//    @Mock
//    TariffDaoI tariffDAO;
//
//    @Mock
//    TariffOptionDaoI optionDAO;
//
//    @BeforeEach
//    public void createMocks(){
//        //enable mocks
//        MockitoAnnotations.initMocks(this);
//
//        //create sample options
//        a.setId(1); a.setName("a");
//        b.setId(2); b.setName("b");
//        c.setId(3); c.setName("c");
////        a.getMandatoryOptions().add(b); //a requires  b
////        a.getMandatoryOptions().add(c); // a requires c
////        b.getIncompatibleOptions().add(c); //b incompatible with c
////        c.getIncompatibleOptions().add(b); //c incompatible with b
//    }
//
//    @Test
//    public void testCreate(){
//        Tariff tariff=new Tariff();
//        TariffDTO dto=new TariffDTO();
//
//        //create tariff with reserved name
//        dto.setName("t1");
//        when(tariffDAO.isNameExist(dto.getName())).thenReturn(true);
//        assertThrows(ServiceException.class,()->tariffService.create(dto),"name is reserved");
//
//        //add option a without its mandatory options
//        dto.getOptions().add(a.getName());
//        when(tariffDAO.isNameExist(dto.getName())).thenReturn(false);
//        when(optionDAO.findByName("a")).thenReturn(a);
//        assertThrows(ServiceException.class,()->tariffService.create(dto));
//
//        dto.getOptions().add(b.getName());
//        when(tariffDAO.isNameExist("t2")).thenReturn(false);
//        when(optionDAO.findByName("a")).thenReturn(a);
//        when(optionDAO.findByName("b")).thenReturn(b);
//        assertThrows(ServiceException.class,()->tariffService.create(dto));
//
//        //add a with incompatible options b and c
//        dto.getOptions().add(c.getName());
//        when(tariffDAO.isNameExist("t2")).thenReturn(false);
//        when(optionDAO.findByName("a")).thenReturn(a);
//        when(optionDAO.findByName("b")).thenReturn(b);
//        when(optionDAO.findByName("c")).thenReturn(b);
//        assertThrows(ServiceException.class,()->tariffService.create(dto));
//
//        //add  incompatible options b and c
//        dto.getOptions().clear();
//        dto.getOptions().add(b.getName());
//        dto.getOptions().add(c.getName());
//        when(tariffDAO.isNameExist("t2")).thenReturn(false);
//        when(optionDAO.findByName("a")).thenReturn(a);
//        when(optionDAO.findByName("b")).thenReturn(b);
//        when(optionDAO.findByName("c")).thenReturn(b);
//        assertThrows(ServiceException.class,()->tariffService.create(dto));
//
//        //add only b
//        dto.getOptions().clear();
//        dto.getOptions().add(b.getName());
//        when(tariffDAO.isNameExist("t2")).thenReturn(false);
//        when(optionDAO.findByName("a")).thenReturn(a);
//        when(optionDAO.findByName("b")).thenReturn(b);
//        when(optionDAO.findByName("c")).thenReturn(b);
//        when(tariffDAO.save(tariff)).thenReturn(1);
//    }
//}