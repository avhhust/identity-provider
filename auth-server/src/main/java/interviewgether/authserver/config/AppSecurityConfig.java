package interviewgether.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable) // ToDo: Find out whether csrf protection required
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/register").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(customize -> customize
                        .loginPage("http://localhost:9000/login") //url to login page
                        .loginProcessingUrl("/login")
                        .successHandler(
                                authenticationSuccessHandler()
                        )
                        .failureHandler((req, res, ex) ->
                                res.setStatus(HttpStatus.UNAUTHORIZED.value())
                        )
                )
                .exceptionHandling(customize -> customize
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("http://localhost:9000/login")
                        )
                )
//                .oauth2Login(Customizer.withDefaults()) ToDo: set login page with social login
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        // Returns OK 200 and includes redirect url in response body
        return (request, response, authentication) -> {
            response.resetBuffer();
            response.setStatus(HttpStatus.OK.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            var savedReq = new HttpSessionRequestCache().getRequest(request, response);
            response.getWriter()
                    .append("{\"redirectUrl\": \"")
                    .append(savedReq == null ? "" : savedReq.getRedirectUrl())
                    .append("\"}");
            response.flushBuffer();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
