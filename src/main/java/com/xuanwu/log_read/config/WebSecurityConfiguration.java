package com.xuanwu.log_read.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
			.anyRequest().authenticated();
	http
		.csrf()
			.disable()
		.formLogin()
			.defaultSuccessUrl("/index.html")
			.loginPage("/login.html")
			.failureUrl("/login.html?error")
			.permitAll()
			.and()
		.logout()
			.logoutSuccessUrl("/login.html?logout")
			.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("albert").password("$2a$10$yoN98EJ7oazDLKktwaJfNeZZ/tvD/YFtpeTq/tdizN.ykBW6u77t.").roles("USER").and().withUser("foo")
				.password("$2a$10$yoN98EJ7oazDLKktwaJfNeZZ/tvD/YFtpeTq/tdizN.ykBW6u77t.").roles("USER");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
