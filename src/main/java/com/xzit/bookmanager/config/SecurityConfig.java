package com.xzit.bookmanager.config;

import com.xzit.bookmanager.dao.mapper.UserMapper;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.service.Impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class SecurityConfig {
    @Autowired
    UserMapper userMapper;
    @Autowired
    AuthService authService;
    @Resource
    PersistentTokenRepository repository;

    @Bean
    public PersistentTokenRepository jdbcRepository(@Autowired DataSource dataSource){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/static/**","/login").permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/dologin")
                .successHandler(this::onAuthenticationSuccess)
                .and()
                .logout().logoutUrl("/logout")
                .and()
                .csrf().disable()
                .rememberMe().rememberMeParameter("remember")
                .tokenValiditySeconds(60*60*24*7)
                .tokenRepository(repository)
                .userDetailsService(authService)
                .and()
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authService)
                .passwordEncoder(new BCryptPasswordEncoder())
                .and().build();
    }
    private void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        HttpSession session = httpServletRequest.getSession();
        AuthUser user = userMapper.getUserByUsername(authentication.getName());
        session.setAttribute("user", user);
        if("admin".equals(user.getRole())){
            httpServletResponse.sendRedirect("/admin/index");
        }else {
            httpServletResponse.sendRedirect("/user/index");
        }
    }
}
