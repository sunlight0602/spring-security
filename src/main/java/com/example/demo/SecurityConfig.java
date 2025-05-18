package com.example.demo;

import com.example.demo.controller.SecurityController;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.PrintWriter;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        // 密碼的加密方式
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // 創造角色
        UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("123")).roles("role1", "role2", "role3").build();
        UserDetails user1 = User.withUsername("user1").password(passwordEncoder().encode("123")).roles("role1").build();
        UserDetails user2 = User.withUsername("user2").password(passwordEncoder().encode("123")).roles("role2").build();
        UserDetails user3 = User.withUsername("user3").password(passwordEncoder().encode("123")).roles("role3").build();

        manager.createUser(admin);
        manager.createUser(user1);
        manager.createUser(user2);
        manager.createUser(user3);

        return manager;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 靜態資源可以自由訪問
        return (web) -> web.ignoring().requestMatchers("/js/**", "/css/**", "/images/**");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index").permitAll()
                        .requestMatchers("/menu1/**").hasRole("role1")
                        .requestMatchers("/menu2/**").hasRole("role2")
                        .requestMatchers("/menu3/**").hasRole("role3")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/toLogin")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .successHandler(loginSuccessHandler())  // 登入成功處理
                        .failureHandler(loginFailureHandler())  // 登入失敗處理
                        // .failureUrl("/toLogin/error")  // 不用重新導向哪裡了
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(logoutSuccessHandler())
                        // .logoutSuccessUrl("/index")  // 不用重新導向哪裡了
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        // 登入成功後的處理邏輯
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            String json = "{\"status\": \"ok\", \"msg\": \"登錄成功\"}";
            out.write(json);
        };
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        // 登入失敗後的處理邏輯
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            String json = "{\"status\": \"error\", \"msg\": \"error\"}";
            out.write(json);
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        // 退出登入成功後的處理邏輯
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            String json = "{\"status\": \"ok\", \"msg\": \"退出登錄\"}";
            out.write(json);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            String json = "{\"status\": \"error\", \"msg\": \"權限不足\"}";
            out.write(json);
        };
    }
}
