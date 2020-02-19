package com.thekuzea.experimental.core.stepdef;

import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.core.fixture.ApiConstants;
import com.thekuzea.experimental.core.fixture.DataKeys;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationStepDefinition {

    private final ScenarioContext scenarioContext;

    @Then("^authenticate as (.*) user with password (.*)$")
    public void authenticate(final String username, final String password) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final UserResource userResourceForSending = UserResource.builder()
                .username(username)
                .password(password)
                .build();

        final Response response = requestSpec.body(userResourceForSending)
                .post(ApiConstants.AUTH_API)
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @Then("^save token$")
    public void saveToken() {
        final Response response = scenarioContext.getDataByKey(DataKeys.RESPONSE, Response.class);
        final String token = response.getBody()
                .jsonPath()
                .getString("token");

        scenarioContext.save(DataKeys.TOKEN, token);
    }
}
