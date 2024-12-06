package com.Admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.Admin.service.impl.UserSecurityService;
import com.Admin.utility.SecurityUtility;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	
	private BCryptPasswordEncoder passwordEncoder() {
		return SecurityUtility.passwordEncoder();
			   
	}
	
	private static final String[] Accessible_URLs = {
		"/css/**",
		"/js/**",
		"/image/**",
		"/newUser",
		"/forgetPassword",
		"/login",
		"/fonts/**"
	};
	
   
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests().
		antMatchers(Accessible_URLs).
		permitAll().anyRequest().authenticated();
		
		http
		.csrf().disable().cors().disable()
		.formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
		.loginPage("/login").permitAll()
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/?logout").deleteCookies("JSESSIONID","remember-me").permitAll()
		.and()
		.rememberMe();
	}

	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authentication) throws Exception {
		authentication.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}
	
	
}
