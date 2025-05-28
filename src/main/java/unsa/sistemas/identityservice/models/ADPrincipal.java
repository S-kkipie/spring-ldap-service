package unsa.sistemas.identityservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entry(objectClasses = { "inetOrgPerson", "top", "person"})
public final class ADPrincipal {

    @Id
    private Name dn;

    @Attribute(name = "cn")
    private String cn;

    @Attribute(name = "sn")
    private String sn;

    @Attribute(name="mail")
    private String mail;

    @Attribute(name = "userPassword")
    private String password;
}