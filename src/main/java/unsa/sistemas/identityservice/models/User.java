package unsa.sistemas.identityservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Transient
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}