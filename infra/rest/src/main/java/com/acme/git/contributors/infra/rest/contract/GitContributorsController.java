package com.acme.git.contributors.infra.rest.contract;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.exception.APIRateLimitExceededException;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api
public interface GitContributorsController {

    @ApiOperation(value = "Get contributors by city")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 204, message = "City not found", response = String.class),
            @ApiResponse(code = 400, message = "Request not properly formed", response = String.class)})
    ResponseEntity<List<Contributor>> getContributors(String city, Integer top) throws IncorrectValuesException;
}
