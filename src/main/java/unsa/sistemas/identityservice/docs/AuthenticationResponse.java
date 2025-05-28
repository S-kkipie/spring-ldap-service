package unsa.sistemas.identityservice.docs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import unsa.sistemas.identityservice.dtos.Authentication;
import unsa.sistemas.identityservice.dtos.ResponseWrapper;

@AllArgsConstructor
@Schema(description = "Response returned when an error occurs")
public class AuthenticationResponse extends ResponseWrapper<Authentication> {
}
