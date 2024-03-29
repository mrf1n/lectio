package by.mrf1n.lectio.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class LectioWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LectioBasicAuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public LectioWebSecurityConfig(LectioBasicAuthenticationEntryPoint authenticationEntryPoint,
                                   @Qualifier("lectioUserDetailsService") UserDetailsService userDetailsService,
                                   BCryptPasswordEncoder passwordEncoder) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(
                        "/",
                        "/auth/**",
                        "/resources/**").permitAll()
                .antMatchers(
                        "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
        http.formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/spring_security_check")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
//                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true);
        http.httpBasic().authenticationEntryPoint(authenticationEntryPoint);
    }
}