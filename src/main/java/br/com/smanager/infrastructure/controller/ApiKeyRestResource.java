package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.ApiKeyService;
import br.com.smanager.infrastructure.dto.ApiKeyDto;
import br.com.smanager.infrastructure.dto.MemberDto;
import br.com.smanager.infrastructure.exception.RestError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = RestConstants.PATH_API_KEYS)
@RequiredArgsConstructor
@Tag(name = "ApiKey resource")
public class ApiKeyRestResource {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyDto> create(){

        var apiKey = apiKeyService.createApiKey();

        return ResponseEntity
            .created(URI.create(RestConstants.PATH_API_KEYS + "/" + apiKey.getId()))
            .body(ApiKeyDto.from(apiKey));
    }

    @Operation(description = "Revoke a apiKey by id", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ApiKey successfully revoked"),
            @ApiResponse(responseCode = "404", description = "ApiKey not found", content = @Content(schema = @Schema(implementation = RestError.class))),
            @ApiResponse(responseCode = "400", description = "ApiKey Expired", content = @Content(schema = @Schema(implementation = MemberDto.class))),
    })
    @PutMapping(value = "/{id}/revoke")
    public ResponseEntity<Void> revokeApiKey(@PathVariable String id){
        apiKeyService.revokeApiKey(id);

        return ResponseEntity.noContent().build();
    }
}
