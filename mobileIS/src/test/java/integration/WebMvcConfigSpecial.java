package integration;

import config.AppInitializer;
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
                                JmsSenderI.class, AppInitializer.class, WebMvcConfigSpecial.class
                        })
        })
public class WebMvcConfigSpecial implements WebMvcConfigurer {


    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}