package unsa.sistemas.identityservice.services;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.service.SecurityService;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.dtos.RegisterRequest;
import unsa.sistemas.identityservice.models.ADPrincipal;
import unsa.sistemas.identityservice.models.User;
import unsa.sistemas.identityservice.repositories.IUserRepository;
import unsa.sistemas.identityservice.security.SecurityPrincipal;

import javax.naming.Name;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LdapTemplate ldapTemplate;
    private final SecurityPrincipal securityPrincipal;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User createUser(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            log.warn("Username already exists for {}", request.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }

        try {
           ADPrincipal userLdap = dtoMapperUserToADPrincipal(request);

           ldapTemplate.create(userLdap);

           return userRepository.save(dtoMapperRequestDtoToUser(request));

        } catch (Exception e) {
            log.error("Error creating user (Not ou found): {}", request.getOrganizationCode(), e);
            throw new RuntimeException("Failed to create user");
        }
    }

/*
    TODO methods for administration


     public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }
*/
public User findCurrentUser() {
    User user = securityPrincipal.getLoggedInPrincipal();

    if (user == null) {
        throw new RuntimeException("No authenticated user found");
    }

    return user;
}



    private User dtoMapperRequestDtoToUser(RegisterRequest source) {
        User target = new User();
        target.setUsername(source.getUsername());
        target.setPassword(source.getPassword());

        return target;
    }

    private ADPrincipal dtoMapperUserToADPrincipal(RegisterRequest source) {
        ADPrincipal target = new ADPrincipal();
        target.setCn(source.getFirstName());
        target.setSn(source.getLastName());
        target.setMail(source.getUsername());
        String hashedPassword = passwordEncoder.encode(source.getPassword());
        target.setPassword(hashedPassword);
        LdapNameBuilder builder = LdapNameBuilder.newInstance()
                .add("ou", "users")
                .add("ou", "appUsers");
        if (source.getOrganizationCode() != null && !source.getOrganizationCode().isBlank()) {
            builder = builder.add("ou", source.getOrganizationCode());
        }
        builder = builder.add("cn", source.getFirstName());
        Name dn = builder.build();
        target.setDn(dn);

        return target;
    }

}