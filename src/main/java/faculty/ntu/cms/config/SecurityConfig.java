package faculty.ntu.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll() // Cho phép truy cập không cần đăng nhập
                        .requestMatchers("/dashboard/**", "/admin/**").authenticated() // Yêu cầu đăng nhập để vào dashboard và admin
                        .requestMatchers("/menu").hasAnyAuthority("USER", "ADMIN") // Cho phép cả user và admin xem danh sách menu
                        .requestMatchers("/menu/create", "/menu/edit/**", "/menu/delete/**").hasAuthority("ADMIN") // Chỉ admin quản lý menu
                        .anyRequest().permitAll() // Các URL khác cho phép tất cả
                )
                .formLogin(form -> form
                        .loginPage("/login") // Trang đăng nhập tùy chỉnh
                        .defaultSuccessUrl("/dashboard", true) // Chuyển hướng đến dashboard sau khi đăng nhập thành công
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL để đăng xuất
                        .logoutSuccessUrl("/login?logout") // Chuyển hướng sau khi đăng xuất
                        .invalidateHttpSession(true) // Hủy session
                        .deleteCookies("JSESSIONID") // Xóa cookie phiên
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}