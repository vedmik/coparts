package software.sigma.bu003.internship.coparts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.sigma.bu003.internship.coparts.security.model.User;
import software.sigma.bu003.internship.coparts.security.model.UserRole;
import software.sigma.bu003.internship.coparts.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            tags = { "Users" },
            summary = "Get all users",
            security = @SecurityRequirement(
                    name = "Manager",
                    scopes = {
                            "SCOPE_MANAGER"
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**OK** All user was show",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = User.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted**"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request**"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "**Forbidden**"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found**"
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    @Operation(
            tags = { "Users" },
            summary = "Get user by email",
            description = "This method take user from DB.",
            security = @SecurityRequirement(
                    name = "Manager",
                    scopes = {
                            "SCOPE_MANAGER"
                    }
            ),
            parameters = {
                    @Parameter(
                            name = "email",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the user.",
                            required = true

                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Ok** Load User from DB by email",
                            content = {
                                    @Content(
                                            schema = @Schema(
                                                    implementation = User.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted**"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request**"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "**Forbidden**"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found**"
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public User getUser(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/roles")
    @Operation(
            tags = { "Users" },
            summary = "Get all existed roles",
            description = "This method take all existed .",
            security = @SecurityRequirement(
                    name = "Manager",
                    scopes = {
                            "SCOPE_MANAGER"
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Ok** Load User from DB by email",
                            content = {
                                    @Content(
                                            schema = @Schema(
                                                    implementation = UserRole.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted**"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request**"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "**Forbidden**"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found**"
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public List<UserRole> getAllRoles() {
        return List.of(UserRole.values());
    }

    @PostMapping("/{email}")
    @Operation(
            tags = { "Users" },
            summary = "Add new role to User",
            description = "This method add new UserRole to User.",
            security = @SecurityRequirement(
                    name = "Admin",
                    scopes = {
                            "SCOPE_ADMIN"
                    }
            ),
            parameters = {
                    @Parameter(
                            name = "email",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the user.",
                            required = true

                    )
            },
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = UserRole.class
                                            )
                                    )
                            )
                    ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Ok** Load User from DB by email",
                            content = {
                                    @Content(
                                            schema = @Schema(
                                                    implementation = Integer.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted**"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request**"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "**Forbidden**"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found**"
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public void addUserRoles(@PathVariable String email, @RequestBody List<UserRole> roles) {
        userService.addUserRoles(email, roles);
    }

    @DeleteMapping("/{email}/{role}")
    @Operation(
            tags = { "Users" },
            summary = "Delete UserRole from User",
            description = "This method delete UserRole from User.",
            security = @SecurityRequirement(
                    name = "Admin",
                    scopes = {
                            "SCOPE_ADMIN"
                    }
            ),
            parameters = {
                    @Parameter(
                            name = "email",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the user.",
                            required = true

                    ),
                    @Parameter(
                            name = "role",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the role.",
                            required = true

                    ),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Ok** Role was deleted.",
                            content = {
                                    @Content(
                                            schema = @Schema(
                                                    implementation = Integer.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted**"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request**"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "**Forbidden**"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found**"
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public void deleteRoleToUser(@PathVariable String email, @PathVariable UserRole role) {
        userService.deleteUserRole(email, role);
    }
}
