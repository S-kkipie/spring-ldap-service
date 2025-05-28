package unsa.sistemas.identityservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsa.sistemas.identityservice.models.Role;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}