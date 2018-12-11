package integration;

import config.AppInitializer;
import config.HibernateConfiguration;
import config.MyRequestListener;
import dao.implementations.PhoneNumberDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import services.implementations.PhoneNumberService;
import services.interfaces.PhoneNumberServiceI;

import javax.servlet.ServletContext;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = PhoneNumberServiceTest.WebMvcConfigSpecial.class)
@PropertySource("classpath:hibernate.properties")
@WebAppConfiguration
class PhoneNumberServiceTest {

    @Autowired
    PhoneNumberServiceI service;

    @Autowired
    private WebApplicationContext wac;

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = {"config", "model", "services", "dao"},
            useDefaultFilters = false,
            includeFilters = {@ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    value = {AppInitializer.class,
                            HibernateConfiguration.class,
                            PhoneNumberService.class,
                            MyRequestListener.class,
                            PhoneNumberDAO.class})}
    )
    public static class WebMvcConfigSpecial implements WebMvcConfigurer {
    }

    @Test
    void startupTest() {
        ServletContext servletContext = wac.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        System.out.println(Arrays.toString(wac.getBeanDefinitionNames()));
        assertNotNull(wac.getBean("phoneNumberService"));
    }

    @Test
    @Transactional
    void getTest() throws InterruptedException, java.util.concurrent.ExecutionException {
        //set up task and executor
        int threadsNumber = 50;
        Set<Long> generatedNumbers = new HashSet<>();
        Callable<Long> task = () -> service.getNext();
        List<Callable<Long>> tasks = new ArrayList<>(threadsNumber);
        IntStream.rangeClosed(1, threadsNumber).forEach(n -> tasks.add(task)); //populate tasks list
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);

        long startTime = System.currentTimeMillis();
        List<Future<Long>> results = executor.invokeAll(tasks);
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("duration= " + duration + " milliseconds");

        for (Future<Long> result : results) {
            Long aLong = result.get();
            generatedNumbers.add(aLong);
        }
        assertEquals(generatedNumbers.size(), results.size()); //mean all long values are unique
    }
}