package unsa.sistemas.identityservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.ldap")
public class LdapProperties {
    private String url;
    private String username;
    private String password;
    private String base;
}
