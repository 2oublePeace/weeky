package com.emiryanvl.persistence.configs

import com.emiryanvl.persistence.services.impl.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it
                .disable()
            }
            .authorizeHttpRequests { it
                .requestMatchers(HttpMethod.POST,"/user").permitAll()
                .anyRequest().authenticated()
            }
            .formLogin {  }
            .logout { it
                .logoutUrl("/logout")
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsServiceImpl,
        passwordEncoder: PasswordEncoder
    ) : AuthenticationProvider {
        return DaoAuthenticationProvider().also {
            it.setUserDetailsService(userDetailsService)
            it.setPasswordEncoder(passwordEncoder)
        }
    }
}