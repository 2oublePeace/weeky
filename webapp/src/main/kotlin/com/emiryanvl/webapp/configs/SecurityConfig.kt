package com.emiryanvl.webapp.configs

import com.emiryanvl.webapp.dto.UserDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Value("\${api.user}")
    private lateinit var userApi: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it
                .disable()
            }
            .authorizeHttpRequests { it
                .requestMatchers("/create","/{username}/edit/**","/delete/**").authenticated()
                .anyRequest().permitAll()
            }
            .formLogin { it
                .loginPage("/signin")
                .defaultSuccessUrl("/").permitAll()
            }
            .logout { it
                .logoutUrl("/logout")
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(restClient: RestClient): UserDetailsService {
        return UserDetailsService {
            val user = restClient.get()
                .uri("$userApi/$it")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body<UserDto>() ?:
                throw UsernameNotFoundException("Пользователь не найден")

            User.builder()
                .username(user.username)
                .password(user.password)
                .roles(user.role)
                .build()
        }
    }

    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ) : AuthenticationProvider {
        return DaoAuthenticationProvider().also {
            it.setUserDetailsService(userDetailsService)
            it.setPasswordEncoder(passwordEncoder)
        }
    }
}
