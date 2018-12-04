//package UItest;
//
//import model.entity.Option;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mock.web.MockServletContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//import services.interfaces.JmsSenderI;
//import services.interfaces.OptionServiceI;
//
//import javax.servlet.ServletContext;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith({SpringExtension.class})
//@ActiveProfiles("test")
//@ContextConfiguration
//@WebAppConfiguration
//class OptionDao {
//
//    @Autowired
//    OptionServiceI optionService;
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    @MockBean
//    JmsSenderI jmsSenderI;
//
//    @Configuration
//    @ComponentScan("config")
//    public static class SpringConfig {
//        //set up package for context configuration classes scanning
//    }
//
//    @Test
//    void startupTest() {
//        ServletContext servletContext = wac.getServletContext();
//        assertNotNull(servletContext);
//        assertTrue(servletContext instanceof MockServletContext);
//        System.out.println(Arrays.toString(wac.getBeanDefinitionNames()));
//        assertNotNull(wac.getBean("optionService"));
//    }
//
//    @Test
//    @Transactional
//    void getTest() {
//        Option option = optionService.get(1);
//        assertNotNull(option);
//        assertEquals(option.getId(), 1);
//        assertEquals(option.getName(), "Deep space");
//        option = optionService.get(-1);
//        assertNull(option);
//    }
//
//    @Test
//    @Transactional
//    void deleteTest() {
//        //delete with error
//        Optional<String> error = optionService.delete(1);
//        assertTrue(error.isPresent());
//        assertNotNull(optionService.get(1));
//
//        error = optionService.delete(2);
//        assertFalse(error.isPresent());
//        assertNull(optionService.get(2));
//    }
//}