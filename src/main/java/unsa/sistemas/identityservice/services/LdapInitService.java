package unsa.sistemas.identityservice.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

@Slf4j
@Service
@AllArgsConstructor
public class LdapInitService {

    private LdapTemplate ldapTemplate;

    @PostConstruct
    public void createStaticOUs() {
        createOU("users", null);
        createOU("appUsers", "users");
        log.info("OUs created");
    }

    private void createOU(String ouName, String parentOu) {
        try {
            Name dn;
            if (parentOu != null) {
                // OU hijo: ou=appUsers,ou=users,dc=company,dc=com
                dn = LdapNameBuilder.newInstance()
                        .add("ou", parentOu)
                        .add("ou", ouName)
                        .build();
            } else {
                dn = LdapNameBuilder.newInstance()
                        .add("ou", ouName)
                        .build();
            }

            if (ouExists(dn)) {
                return;
            }

            Attributes attributes = new BasicAttributes();
            BasicAttribute objectClass = new BasicAttribute("objectClass");
            objectClass.add("organizationalUnit");
            objectClass.add("top");
            attributes.put(objectClass);
            attributes.put("ou", ouName);

            // Crear en LDAP
            ldapTemplate.bind(dn, null, attributes);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean ouExists(Name dn) {
        try {
            ldapTemplate.lookup(dn);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}