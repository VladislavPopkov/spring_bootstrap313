package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@EnableWebSecurity //аннотация добавила делегатионфильтерпрокси, вызываются теперь фильтры из спринг секурити
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;

    private final UserServiceImpl userService;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImpl userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //настраивает сам спринг секурити + авторизацию
        http.authorizeRequests()//запрос авторизации на опр url конкретным ролям
                .antMatchers("/admin/**").hasRole("ADMIN")//видит url Одна роль админ(какой запрос пришел в прил)
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") //видят url ток наши роли
                .antMatchers("/", "/index").permitAll()//все видят всех пустим
                .anyRequest().authenticated()//для других запросов необх аутентиф
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(successUserHandler)//настройка логина..
                .usernameParameter("email")
                .permitAll()
                .and()
                .logout()//настройка выхода (из сессии удал. польз. + куки удал. у польз)
                .permitAll();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(20);
    }
    //зашифрованный в одностороннем порядке по хешу пароль(объект занимается шифрованием паролей)

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception { //настраиваем аутентификацию
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
        //автомат. при аутентификации пароль прогонять будет
        //просто строка которая по-любому нужна для аутентификации польз.
    }

}