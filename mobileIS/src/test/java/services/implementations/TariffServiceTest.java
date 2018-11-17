//package services.implementations;
//
//import dao.interfaces.OptionDaoI;
//import dao.interfaces.TariffDaoI;
//import model.dto.TariffDTO;
//import model.entity.Option;
//import model.entity.OptionRelation;
//import model.entity.Tariff;
//import model.helpers.PaginateHelper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import services.exceptions.ServiceException;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.WARN)
//class TariffServiceTest {
//
//    @InjectMocks
//    private
//    TariffService tariffService;
//
//    @Mock
//    private
//    TariffDaoI tariffDAO;
//
//    @Mock
//    private
//    OptionDaoI optionDAO;
//
//    @BeforeEach
//    void createMocks() {
//        //enable mocks
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testCreate() {
//        TariffDTO dto = new TariffDTO();
//
//        //createAccount tariff with reserved name
//        dto.setName("t1");
//        when(tariffDAO.findByName(dto.getName())).thenReturn(new Tariff());
//        assertThrows(ServiceException.class, () -> tariffService.create(dto), "name is reserved");
//    }
//
//    @Test
//    void testCheckCompatibility() {
//        String A = "a";
//        String B = "b";
//        String[] params = new String[]{A, B};
//        String[] params1 = new String[]{A};
//        TariffDTO dto = new TariffDTO();
//        OptionRelation relation = new OptionRelation();
//        Option a = new Option();
//        a.setName(A);
//        Option b = new Option();
//        b.setName(B);
//        relation.setOne(a);
//        relation.setAnother(b); //a requires b
//
//        //add option a without its' mandatory options
//        dto.getOptions().add(A);
//        when(tariffDAO.findByName(dto.getName())).thenReturn(null);
//        when(optionDAO.getMandatoryRelation(params1)).thenReturn(Collections.singletonList(relation));
//        ServiceException e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
//        assertEquals(e.getMessage(), "More options are required as mandatory: " + Collections.singletonList(B));
//
//        //add a with incompatible options b
//        dto.getOptions().add(B);
//        when(optionDAO.getMandatoryRelation(params)).thenReturn(Collections.emptyList());
//        when(optionDAO.getIncompatibleRelation(params)).thenReturn(Collections.singletonList(relation));
//        e = assertThrows(ServiceException.class, () -> tariffService.create(dto));
//        assertTrue(e.getMessage().contains(A + " and " + B));
//    }
//
//    @Test
//    void updateTest() {
//        Tariff t = new Tariff();
//        t.setId(2);
//        TariffDTO dto = new TariffDTO();
//        dto.setId(1);
//        dto.setName("t");
//
//        //check if name is reserved
//        when(tariffDAO.findByName("t")).thenReturn(t);
//        assertThrows(ServiceException.class, () -> tariffService.update(dto));
//    }
//
//    @Test
//    void delete() {
//        when(tariffDAO.isUsed(1)).thenReturn(true);
//        assertThrows(ServiceException.class, () -> tariffService.delete(1));
//    }
//
//    @Test
//    void getPaginateData() {
//        long total = 10L;
//        int perPage = 3;
//        int limit = 10;
//
//        List<Tariff> tariffs = Collections.singletonList(new Tariff());
//        assertThrows(IllegalArgumentException.class, () -> tariffService.getPaginateData(0, 0));
//        assertThrows(IllegalArgumentException.class, () -> tariffService.getPaginateData(1, -1));
//        when(tariffDAO.allInRange(0, perPage)).thenReturn(tariffs);
//        when(tariffDAO.count()).thenReturn(total);
//        PaginateHelper<Tariff> helper = tariffService.getPaginateData(null, perPage);
//        assert (helper.getItems().equals(tariffs));
//        assert (helper.getTotal() == limit / perPage + limit % perPage); // 10clients / 3 per page=3+1 page
//    }
//}