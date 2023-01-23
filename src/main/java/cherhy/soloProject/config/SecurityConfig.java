package cherhy.soloProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .headers()
                .frameOptions()
                .disable();

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/**").permitAll();

        http.formLogin()
                .loginPage("/member/signIn")
                .defaultSuccessUrl("/")
                .permitAll();


        http.exceptionHandling()
                .accessDeniedPage("/denied");

    }

}
