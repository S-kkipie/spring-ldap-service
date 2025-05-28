package unsa.sistemas.identityservice.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class LdapAuthenticationToken extends AbstractAuthenticationToken {
    private final String username;
    private final String password;
    private final String organization;

    public LdapAuthenticationToken(String username, String password, String organization) {
        super(null);
        this.username = username;
        this.password = password;
        this.organization = organization;
        setAuthenticated(false);
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

}
