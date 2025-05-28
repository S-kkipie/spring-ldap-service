package unsa.sistemas.identityservice.security;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private final LdapTemplate ldapTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LdapAuthenticationToken token = (LdapAuthenticationToken) authentication;

        String username = token.getUsername();
        String password = token.getPassword();
        String organization = token.getOrganization();

        String base = "ou=" + organization + ",ou=users";
        String filter = "(mail=" + username + ")";

        List<DirContextOperations> result = ldapTemplate.search(
                base,
                filter,
                (ContextMapper<DirContextOperations>) ctx -> (DirContextOperations) ctx);

        if (result.isEmpty()) {
            throw new BadCredentialsException("User not found");
        }

        DirContextOperations userData = result.get(0);

        byte[] pwdBytes = (byte[]) userData.getObjectAttribute("userPassword");
        String hashedPassword = new String(pwdBytes, StandardCharsets.UTF_8);

        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new BadCredentialsException("Bad Password");
        }

        log.info("User authenticated successfully :{}", username);
        return new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LdapAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
