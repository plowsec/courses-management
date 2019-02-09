package helloworld;

import helloworld.entity.Eleve;
import helloworld.entity.Professeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import helloworld.entity.Administrator;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler
            authenticationSuccessHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users").hasRole(Administrator.ROLE)
                .antMatchers(HttpMethod.PUT, "/period").hasRole(Professeur.ROLE_HEADTEACHER)
                .antMatchers(HttpMethod.GET, "/courses/all/**").hasRole(Professeur.ROLE_HEADTEACHER)
                .antMatchers(HttpMethod.PUT, "/courses/all/**").hasRole(Professeur.ROLE_HEADTEACHER)
                .antMatchers(HttpMethod.GET, "/users/students").hasAnyRole(new String[]{Professeur.ROLE_HEADTEACHER, Professeur.ROLE_TEACHER, Administrator.ROLE})
                .antMatchers(HttpMethod.GET, "/users/students/**").hasAnyRole(new String[]{Professeur.ROLE_HEADTEACHER, Professeur.ROLE_TEACHER, Eleve.ROLE})
                .antMatchers(HttpMethod.GET, "/summary").hasAnyRole(new String[]{Professeur.ROLE_HEADTEACHER, Professeur.ROLE_TEACHER})
                .antMatchers(HttpMethod.POST, "/courses").hasAnyRole(new String[]{Professeur.ROLE_HEADTEACHER, Professeur.ROLE_TEACHER})
                .antMatchers(HttpMethod.PUT, "/courses/**").hasAnyRole(new String[]{Professeur.ROLE_HEADTEACHER, Professeur.ROLE_TEACHER})
                .antMatchers(HttpMethod.GET, "/courses/**").hasAnyRole(new String[]{Professeur.ROLE_HEADTEACHER, Professeur.ROLE_TEACHER, Eleve.ROLE})
                .antMatchers(HttpMethod.PUT, "/users/students/**").hasRole(Eleve.ROLE)
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8082", "http://localhost:8081"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler(){
        return new MySavedRequestAwareAuthenticationSuccessHandler();
    }
    @Bean
    public SimpleUrlAuthenticationFailureHandler myFailureHandler(){
        return new SimpleUrlAuthenticationFailureHandler();
    }
}