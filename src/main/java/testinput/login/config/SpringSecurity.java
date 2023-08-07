package testinput.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/home").permitAll()
                                .requestMatchers("/about").permitAll()
                                .requestMatchers("/signup/monday").permitAll()
                                .requestMatchers("/signup/tuesday").permitAll()
                                .requestMatchers("/signup/wednesday").permitAll()
                                .requestMatchers("/signup/thursday").permitAll()
                                .requestMatchers("/signup/friday").permitAll()
                                .requestMatchers("/appointmentSucess").permitAll()
                                .requestMatchers("/appointmentsetup").permitAll()
                                .requestMatchers("/save").permitAll()
                                .requestMatchers("/maketransfer/save").permitAll()
                                .requestMatchers("/creditcardapply").permitAll()
                                .requestMatchers("/maketransfer").permitAll()
                                .requestMatchers("/customers").hasRole("ADMIN")
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/customers")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
