package software.sigma.bu003.internship.coparts.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import software.sigma.bu003.internship.coparts.security.model.GoogleUserInfo;
import software.sigma.bu003.internship.coparts.security.model.User;
import software.sigma.bu003.internship.coparts.security.model.UserRole;
import software.sigma.bu003.internship.coparts.security.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeRequests()
                    .antMatchers("/users/roles","users/*/roles/**")
                        .hasAuthority("SCOPE_ADMIN")
                    .antMatchers("/users","/users/**")
                        .hasAnyAuthority("SCOPE_MANAGER", "SCOPE_ADMIN")
                    .anyRequest()
                        .authenticated()
                .and()
                    .oauth2Login()
                .and()
                .csrf().disable()
                .build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return this::mapAuthorities;
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(authorities);

        Map<String, Object> userAttributes = authorities.stream()
                .filter(OidcUserAuthority.class::isInstance)
                .map(OidcUserAuthority.class::cast)
                .map(OAuth2UserAuthority::getAttributes)
                .findFirst()
                .orElseThrow();

        GoogleUserInfo googleUserInfo = new GoogleUserInfo(userAttributes);

        synchronizeUserInfo(googleUserInfo);

        String userEmail = userAttributes.get("email").toString();
        Set<UserRole> userRoles = userService.getAllUserRoles(userEmail);
        mappedAuthorities.addAll(userRoles);

        return mappedAuthorities;
    }

    private void synchronizeUserInfo(GoogleUserInfo googleUserInfo){
        User user = userService.getOptionalUserByEmail(googleUserInfo.getEmail())
                .map(copartsUser -> updateUser(copartsUser, googleUserInfo))
                .orElseGet(() -> createNewUser(googleUserInfo));

        userService.saveUser(user);
    }

    private User createNewUser(GoogleUserInfo googleUserInfo){
        return User.builder()
                .googleId(googleUserInfo.getGoogleId())
                .email(googleUserInfo.getEmail())
                .firstName(googleUserInfo.getFirstName())
                .lastName(googleUserInfo.getLastName())
                .imageUrl(googleUserInfo.getPictureUrl())
                .locale(googleUserInfo.getLocale())
                .lastVisit(LocalDateTime.now())
                .build();
    }

    private User updateUser(User userFromDB, GoogleUserInfo googleUserInfo) {
        if(userFromDB.getGoogleId() == null ||
                !userFromDB.getGoogleId().equals(googleUserInfo.getGoogleId())) {
            userFromDB.setGoogleId(googleUserInfo.getGoogleId());
        }
        if(userFromDB.getFirstName() == null ||
                !userFromDB.getFirstName().equals(googleUserInfo.getFirstName())) {
            userFromDB.setFirstName(googleUserInfo.getFirstName());
        }
        if(userFromDB.getLastName() == null ||
                !userFromDB.getLastName().equals(googleUserInfo.getLastName())) {
            userFromDB.setLastName(googleUserInfo.getLastName());
        }
        if(userFromDB.getLocale() == null ||
                !userFromDB.getLocale().equals(googleUserInfo.getLocale())) {
            userFromDB.setLocale(googleUserInfo.getLocale());
        }
        if(userFromDB.getImageUrl() == null ||
                !userFromDB.getImageUrl().equals(googleUserInfo.getPictureUrl())) {
            userFromDB.setImageUrl(googleUserInfo.getPictureUrl());
        }

        userFromDB.setLastVisit(LocalDateTime.now());
        return userFromDB;
    }
}
