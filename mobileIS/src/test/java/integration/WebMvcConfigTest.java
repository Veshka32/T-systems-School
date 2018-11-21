package integration;

import config.AppInitializer;
import config.WebMvcConfig;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import services.interfaces.JmsSenderI;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@Profile("test")
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"dao", "services", "model"},
        excludeFilters = {
                @ComponentScan.Filter(type = ASSIGNABLE_TYPE,
                        value = {
                                JmsSenderI.class, AppInitializer.class, WebMvcConfig.class
                        })
        })
public class WebMvcConfigTest implements WebMvcConfigurer {
    private static final Logger logger = Logger.getLogger(WebMvcConfigTest.class);

    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        logger.info("Set view resolver");
        return resolver;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
        logger.info("register resourceHandler");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}