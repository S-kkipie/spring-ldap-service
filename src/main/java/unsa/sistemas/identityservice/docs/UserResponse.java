package unsa.sistemas.identityservice.docs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import unsa.sistemas.identityservice.dtos.ResponseWrapper;
import unsa.sistemas.identityservice.models.User;

@AllArgsConstructor
@Schema(description = "Contains a User in the response")
public class UserResponse extends ResponseWrapper<User> {
}
