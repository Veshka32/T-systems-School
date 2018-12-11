package integration;

import config.SecurityConfig;
import model.dto.ContractDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import services.aspects.HotTariffAspect;
import services.aspects.LoggAspect;
import services.implementations.JmsSender;
import services.interfaces.ContractServiceI;
import services.interfaces.TelegramBotI;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = ContractServiceTest.WebMvcConfigSpecial.class)
@PropertySource("classpath:hibernate.properties")
@WebAppConfiguration
class ContractServiceTest {

    @Autowired
    ContractServiceI service;

    @Autowired
    private WebApplicationContext wac;

    @Test
    void startupTest() {
        ServletContext servletContext = wac.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        System.out.println(Arrays.toString(wac.getBeanDefinitionNames()));
        assertNotNull(wac.getBean("contractService"));
    }

    @Test
    @Transactional
    void createContractTest() {

        //check if all options has its' mandatory
        ContractDTO dto = new ContractDTO();
        dto.setTariffId(1); //set tariff Mother Earth
        dto.getOptionsIds().addAll(Arrays.asList(3, 6)); //try to add option Spoiler Ban and Time Paradox protection
        Optional<String> error = service.create(dto);
        assertTrue(error.isPresent());
        assertTrue(error.get().contains("protocol"));


        //check if all options are compatible with each other
        dto.setTariffId(2); //set tariff Solar System
        dto.getOptionsIds().clear();
        dto.getOptionsIds().addAll(Arrays.asList(1, 2, 7, 8, 4));
        error = service.create(dto);
        assertTrue(error.isPresent());
        assertTrue(error.get().contains("Pluto"));
        assertTrue(error.get().contains("Local"));

        //check if all options are compatible with options in tariff
        dto.getOptionsIds().clear();
        dto.getOptionsIds().addAll(Arrays.asList(1, 2, 7, 4)); //7  incompatible with tariff
        error = service.create(dto);
        assertTrue(error.get().contains("tariff"));
        assertTrue(error.get().contains("Local"));
    }

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = {"config", "model", "services", "dao"},
            lazyInit = true,
            excludeFilters = {@ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    value = {WebMvcConfigurer.class, TelegramBotI.class, JmsSender.class, SecurityConfig.class, HotTariffAspect.class, LoggAspect.class, UserDetailsService.class})}
    )
    public static class WebMvcConfigSpecial implements WebMvcConfigurer {
    }
}