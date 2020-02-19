package com.thekuzea.experimental.core.stepdef;

import com.thekuzea.experimental.core.fixture.ApiConstants;
import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@RequiredArgsConstructor
public class BaseStepDefinition {

    private static final String ERRORS = "errors";

    private final ScenarioContext scenarioContext;

    @Then("^response status code should be (.+)$")
    public void checkResponseStatusCode(final Integer statusCode) {
        final Response response = scenarioContext.getDataByKey(DataKeys.RESPONSE, Response.class);

        assertThat("Status codes aren't equal!", response.getStatusCode(), equalTo(statusCode));
    }

    @Then("^response body error (.+) should have (.+)$")
    public void checkResponseBodyValidationMessages(final String location, final String message) {
        final Response response = scenarioContext.getDataByKey(DataKeys.RESPONSE, Response.class);
        final List<String> actualMessages = response.getBody()
                .jsonPath()
                .get(ERRORS + ApiConstants.JSON_PATH_SEPARATOR + location);

        assertThat("Error messages aren't equal!", actualMessages, hasItem(message));
    }
}
