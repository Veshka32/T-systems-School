package integration;

import dao.interfaces.OptionDaoI;
import model.entity.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import services.interfaces.JmsSenderI;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@ContextConfiguration
@WebAppConfiguration
class OptionDaoIT {

    @Autowired
    OptionDaoI optionDao;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    JmsSenderI jmsSenderI;

    @Configuration
    @ComponentScan("config")
    public static class SpringConfig {
        //set up package for context configuration classes scanning
    }

    @Test
    void startupTest() {
        ServletContext servletContext = wac.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        System.out.println(Arrays.toString(wac.getBeanDefinitionNames()));
        assertNotNull(wac.getBean("optionDAO"));
    }

    @Test
    @Transactional
    void findByNameTest() {
        String name = "I love Pluto";
        Optional<Option> option = optionDao.findByName(name);
        assertTrue(option.isPresent());
        assertEquals(option.get().getName(), name);
    }

    @Test
    @Transactional
    void saveTest() {
        Option option = new Option();
        option.setName("test");
        option.setPrice(new BigDecimal(1));
        Integer id = optionDao.save(option);
        assertNotNull(id);
        assertNotNull(optionDao.findOne(id));
    }
}