package unsa.sistemas.identityservice.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users_roles")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = true, updatable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = true, updatable = true)
    private Role role;
}