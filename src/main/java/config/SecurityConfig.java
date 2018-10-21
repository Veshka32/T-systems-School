package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("pass")).roles("ADMIN"); //temp in-memory auth
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/login*").anonymous() //allow anonymous access to login
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                    .loginPage("/login.html") //custom login page
//                    .loginProcessingUrl("/perform_login") //hide the fact that the application is actually secured with Spring Security
//                    .defaultSuccessUrl("/index.html",true) //after successful login, root by default, if true - to the spec utl,if false - on the previous page
//                    .failureUrl("/login.html?error=true") //after bad login
//                .and()
//                    .logout().logoutSuccessUrl("/login.html?error=true");
        http
                .formLogin().loginPage("/login")
                    .defaultSuccessUrl("/manager",true)
                    .failureUrl("/login?error")
                    .and()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/manager","/editTariff","/createTariff").hasRole("ADMIN")
                    .anyRequest().authenticated() //all remaining must be auth
                    .and()
                    .logout().logoutSuccessUrl("/login?logout");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
