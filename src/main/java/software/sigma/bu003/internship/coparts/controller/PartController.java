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
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import software.sigma.bu003.internship.coparts.controller.exception.IncorrectRequestBodyException;
import software.sigma.bu003.internship.coparts.entity.PageParts;
import software.sigma.bu003.internship.coparts.entity.Part;
import software.sigma.bu003.internship.coparts.service.PartService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/parts")
@RequiredArgsConstructor
@SecurityRequirement(name = "CopartsUsers")
public class PartController {

    private final PartService partService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            tags = { "Parts" },
            summary = "Create new part in DB",
            description = "This method create new Part to DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "**Created** New part was created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Part.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted** You have made a valid request, but this Part has already been created before."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request** You have made a not valid request"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found** This Part was not found."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public Part createPart(@RequestBody @Valid Part part, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectRequestBodyException(bindingResult);
        }
        return partService.createPart(part);
    }

    @GetMapping
    @Operation(
            tags = { "Parts" },
            summary = "Get page with parts",
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            description = "The page to output is passed in this parameter."

                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            description = "The number of elements on the page is passed in this parameter."
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**OK** Page with parts was returned",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = PageParts.class
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
                            responseCode = "404",
                            description = "**Not found**"
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public PageParts getAllParts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return partService.getAllParts(page, size);
    }

    @GetMapping("/{brand}/{code}")
    @Operation(
            tags = { "Parts" },
            summary = "Get part from DB",
            description = "This method Response part from DB.",
            parameters = {
                    @Parameter(
                            name = "brand",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the brand of the part.",
                            required = true

                    ),
                    @Parameter(
                            name = "code",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the code of the part.",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**OK** Part was request",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Part.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted** You have made a valid request, but this part was not find."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request** You have made a not valid request"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found** This Part was not found."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public Part getPart(@PathVariable String brand, @PathVariable String code) {
        return partService.getPart(brand, code);
    }

    @PutMapping("/{brand}/{code}")
    @Operation(
            tags = { "Parts" },
            summary = "Update part in DB",
            description = "This method update part in DB.",
            parameters = {
                    @Parameter(
                            name = "brand",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the brand of the part.",
                            required = true

                    ),
                    @Parameter(
                            name = "code",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the code of the part.",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Ok** Part was updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = Part.class
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
                            description = "**Bad request** You have made a not valid request"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found** - This Part was not found."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public Part updatePart(@RequestBody @Valid Part part, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectRequestBodyException(bindingResult);
        }
        return partService.updatePart(part);
    }

    @DeleteMapping("/{brand}/{code}")
    @Operation(
            tags = { "Parts" },
            summary = "Delete part from DB",
            description = "This method delete Part from DB.",
            parameters = {
                    @Parameter(
                            name = "brand",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the brand of the part.",
                            required = true

                    ),
                    @Parameter(
                            name = "code",
                            in = ParameterIn.PATH,
                            description = "In this parameter, you need to specify the code of the part.",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**Ok** This part was deleted",
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
                            description = "*Accepted*"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request** You have made a not valid request"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "**Not found** This Part was not found."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "**Unavailable** This server was not worked."
                    )
            }
    )
    public void deletePart(@PathVariable String brand, @PathVariable String code) {
        partService.deletePart(brand, code);
    }

    @GetMapping("/synchronization")
    @Operation(
            tags = { "External API" },
            summary = "Synchronize DB with External API",
            description = "This method create new Parts in DB from External API.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "**OK** All parts was synchronized",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Part.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "202",
                            description = "**Accepted** "
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "**Bad request** "
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
    public List<Part> synchronizeParts() {
        return partService.synchronizeWithTechnomir();
    }
}
