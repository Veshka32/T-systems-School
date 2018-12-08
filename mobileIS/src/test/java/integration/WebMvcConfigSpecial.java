package integration;

import config.AppInitializer;
import config.HibernateConfiguration;
import config.MyRequestListener;
import dao.implementations.PhoneNumberDAO;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import services.implementations.PhoneNumberService;

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
class WebMvcConfigSpecial implements WebMvcConfigurer {

}