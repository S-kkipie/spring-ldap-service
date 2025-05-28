package unsa.sistemas.identityservice.repositories.ldap;

import org.springframework.data.ldap.repository.LdapRepository;
import unsa.sistemas.identityservice.models.ADPrincipal;


public interface ILDAPPrincipalRepository extends LdapRepository<ADPrincipal>{
}