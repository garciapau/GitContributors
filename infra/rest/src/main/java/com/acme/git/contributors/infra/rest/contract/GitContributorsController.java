package com.acme.git.contributors.infra.rest.contract;

import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.infra.rest.model.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

@Api
public interface GitContributorsController {

    @ApiOperation(value = "Get contributors by city")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ContributorsOfCity.class),
            @ApiResponse(code = 204, message = "City not found", response = ApiError.class),
            @ApiResponse(code = 400, message = "Request not properly formed", response = ApiError.class),
            @ApiResponse(code = 503, message = "Remote service not available", response = ApiError.class)
    })
    ResponseEntity<ContributorsOfCity> getContributors(String city, Integer top) throws IncorrectValuesException;
}
