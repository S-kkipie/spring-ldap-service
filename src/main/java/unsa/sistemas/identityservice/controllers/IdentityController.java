package unsa.sistemas.identityservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import unsa.sistemas.identityservice.docs.AuthenticationResponse;
import unsa.sistemas.identityservice.docs.BadResponse;
import unsa.sistemas.identityservice.docs.UserResponse;
import unsa.sistemas.identityservice.dtos.Authentication;
import unsa.sistemas.identityservice.dtos.AuthenticationRequest;
import unsa.sistemas.identityservice.dtos.RegisterRequest;
import unsa.sistemas.identityservice.dtos.ResponseWrapper;
import unsa.sistemas.identityservice.models.User;
import unsa.sistemas.identityservice.security.JWTUtil;
import unsa.sistemas.identityservice.security.LdapAuthenticationProvider;
import unsa.sistemas.identityservice.security.LdapAuthenticationToken;
import unsa.sistemas.identityservice.services.UserService;
import unsa.sistemas.identityservice.utils.ResponseHandler;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("user")
public class IdentityController {

    private final UserService userService;
    private final JWTUtil jwtTokenUtil;
    private final LdapAuthenticationProvider ldapAuthenticationProvider;


    @Operation(summary = "Auth a user")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authentication successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User authentication failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BadResponse.class)
                            )
                    )
            }
    )
    @PostMapping("login")
    public ResponseEntity<ResponseWrapper<Object>> createAuthenticationToken(@Valid  @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(), authenticationRequest.getOrganizationCode());
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Authentication Failed", HttpStatus.UNAUTHORIZED,
                    "Invalid credentials, please check details and try again");
        }

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        return ResponseHandler.generateResponse("Authentication successfully", HttpStatus.OK,
                new Authentication(token, refreshToken));
    }

    @Operation(summary = "Register a user")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registration successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User registration failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BadResponse.class)
                            )
                    )
            }
    )
    @PostMapping("register")
    public ResponseEntity<ResponseWrapper<Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User newUser = userService.createUser(request);

            final String token = jwtTokenUtil.generateToken(newUser);

            final String refreshToken = jwtTokenUtil.generateRefreshToken(newUser);

            return ResponseHandler.generateResponse("User created successfully", HttpStatus.CREATED,
                    new Authentication(token, refreshToken));

        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse("Failed to register a user", HttpStatus.BAD_REQUEST,
                    "The username is already taken");

        } catch (Exception e) {
            return ResponseHandler.generateResponse("Failed to register a user", HttpStatus.BAD_REQUEST,
                    "A error occurred while trying to register a user");
        }
    }

    @Operation(summary = "Retrieves a user's profile")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User profile generated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User profile generation failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BadResponse.class)
                            )
                    )
            }
    )
    @GetMapping("profile")
    public ResponseEntity<ResponseWrapper<Object>> retrieveUserProfile() {
       try{
           return ResponseHandler.generateResponse("User Profile generated", HttpStatus.OK, userService.findCurrentUser());
       }catch (Exception e) {
           return ResponseHandler.generateResponse("Failed to retrieve a user profile", HttpStatus.BAD_REQUEST,
                   "No profile found");
       }
    }

    private void authenticate(String username, String password, String organization) throws Exception {
        try {
            ldapAuthenticationProvider.authenticate(
                    new LdapAuthenticationToken(username, password, organization)
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED" + e.getMessage());
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("INVALID_CREDENTIALS", e.getCause());

        }
    }
}