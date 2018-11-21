package integration;

import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Profile("test")
public class AppInitializerTest extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}