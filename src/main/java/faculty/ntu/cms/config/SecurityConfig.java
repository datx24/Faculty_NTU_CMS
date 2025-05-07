package faculty.ntu.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Matcher để phát hiện URL sai (/admin/admin/**)
        RequestMatcher wrongAdminUrlMatcher = new AntPathRequestMatcher("/admin/admin/**");

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/dashboard/**", "/admin/**").authenticated()
                        .requestMatchers("/menu").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/menu/create", "/menu/edit/**", "/menu/delete/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // Xử lý URL sai bằng cách redirect
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> {
                                    String requestUri = request.getRequestURI();
                                    if (wrongAdminUrlMatcher.matches(request)) {
                                        // Redirect từ /admin/admin/... về /admin/...
                                        String correctedUri = requestUri.replaceFirst("/admin/admin", "/admin");
                                        response.sendRedirect(correctedUri);
                                    } else {
                                        response.sendRedirect("/login");
                                    }
                                },
                                wrongAdminUrlMatcher
                        )
                        .accessDeniedPage("/403") // Trang lỗi 403 nếu không có quyền
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}