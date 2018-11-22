package integration;

import config.AppInitializer;
import config.MyRequestListener;
import configForTest.HibernateConfigSpecial;
import configForTest.WebMvcConfigSpecial;
import dao.interfaces.OptionDaoI;
import model.entity.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = {WebMvcConfigSpecial.class, HibernateConfigSpecial.class, AppInitializer.class, MyRequestListener.class})
@WebAppConfiguration
class OptionDAOTest {

    @Autowired
    OptionDaoI optionDao;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

//    @Before
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }

    @Test
    @Sql({"/data"})
    void startupTest() {
        ServletContext servletContext = wac.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("optionDAO"));
    }

    @Test
    @Transactional
    void findByNameTest() {
        String name = "I love Pluto";
        assert ((optionDao.findByName(name).getName()).equals(name));
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