package org.example.fileshibernate.config;

import org.example.fileshibernate.service.UserService;
import org.example.fileshibernate.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().addHeaderWriter(new StaticHeadersWriter("X-Robots-Tag","noindex, nofollow"))
                    .and()
                .authorizeRequests()
                    .antMatchers("/register")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/sendsecret")
                    .permitAll()
                    .and()
                .authorizeRequests()
                  .antMatchers("/**").access("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') ")
                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .and()
                .authorizeRequests()
                    .antMatchers("/css/styles.css")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(loginSuccessHandler())
                    .failureHandler(loginFailureHandler())
                    .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/login")
                    .and()
                .csrf();
//                .authorizeRequests().antMatchers("/console/**").permitAll();;



//        http.csrf().disable();
//        http.headers().frameOptions().disable();


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/**/favicon.ico");


    }

    private AuthenticationSuccessHandler loginSuccessHandler() {



        return ((request, response, authentication) -> {
            DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (savedRequest!= null) {
                response.sendRedirect(savedRequest.getRequestURL());
            } else {
                response.sendRedirect("/");
            }
        });
    }

    private AuthenticationFailureHandler loginFailureHandler() {
        return ((request, response, authentication) -> {
            request.getSession().setAttribute("flash", new FlashMessage("Niestety, niewłaściwe dane. Próbuj dalej. ", FlashMessage.Status.FAILURE));
            response.sendRedirect("/login");
        });
    }


}
