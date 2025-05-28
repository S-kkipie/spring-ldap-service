package unsa.sistemas.identityservice.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.LdapTemplate;

@Configuration
@AllArgsConstructor
public class LdapConfig {

    private final LdapProperties ldapProperties;

    @Bean
    ContextSource contextSource() {

        LdapContextSource ldapContextSource = new LdapContextSource();

        ldapContextSource.setUserDn(ldapProperties.getUsername());
        ldapContextSource.setPassword(ldapProperties.getPassword());
        ldapContextSource.setUrl(ldapProperties.getUrl());
        ldapContextSource.setBase(ldapProperties.getBase());

        return ldapContextSource;
    }

    @Bean
    LdapTemplate ldapTemplate(ContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }


}
