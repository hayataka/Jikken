package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration

public class SecurityConfig extends WebSecurityConfigurerAdapter {
//パスワードエンコーダーのBean定義
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// データソース
	@Autowired
	private DataSource dataSource;

	// ユーザIDとパスワードを取得するSQL
	private static final String USER_SQL = "SELECT  user_id ,password,  true FROM m_user WHERE user_id=?";

	// ユーザのロールを取得するSQL
	private static final String ROLE_SQL = "SELECT user_id ,role FROM m_user WHERE user_id=?";

	@Override
	public void configure(WebSecurity web) {
		// 静的リソースへのアクセスには、セキュリティを適用しない
		web.ignoring().antMatchers("/webjars/**", "/css/**", "/js/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// ポイント３：直リンクの禁止
		// ログイン不要ページの設定
		http.authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll().antMatchers("/login").permitAll().antMatchers("/signup").permitAll()
				.anyRequest().authenticated();

		// ログイン処理
		http.formLogin().loginProcessingUrl("/login").loginPage("/login").failureUrl("/login")
				.usernameParameter("userId").passwordParameter("password").defaultSuccessUrl("/home", true);

		// CSRF対策を無効に設定
		http.csrf().disable();

		boolean alwaysUse = true;
		http.formLogin().loginProcessingUrl("/login").loginPage("/login").failureUrl("/login")
				.usernameParameter("userId").passwordParameter("password").defaultSuccessUrl("/home", alwaysUse);

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(USER_SQL)
				.authoritiesByUsernameQuery(ROLE_SQL).passwordEncoder(passwordEncoder());
	}

}
