package unsa.sistemas.identityservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Authentication request payload with user credentials")
public class AuthenticationRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    @Schema(
            description = "User's username or email address",
            example = "john.doe"
    )
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Schema(
            description = "User's password",
            example = "MySecretPass123"
    )
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(
            description = "Organization code the user is attempting to authenticate under",
            example = "ORG-001"
    )
    private String organizationCode;
}
