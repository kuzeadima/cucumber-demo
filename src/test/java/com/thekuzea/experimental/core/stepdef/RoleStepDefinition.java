package com.thekuzea.experimental.core.stepdef;

import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dto.RoleResource;
import com.thekuzea.experimental.domain.model.Role;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.thekuzea.experimental.core.fixture.ApiConstants.AUTHORIZATION;
import static com.thekuzea.experimental.core.fixture.ApiConstants.BEARER;
import static com.thekuzea.experimental.core.fixture.ApiConstants.ROLE_API;
import static com.thekuzea.experimental.core.fixture.ApiConstants.URI_SEPARATOR;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor
public class RoleStepDefinition {

    private final ScenarioContext scenarioContext;

    private final RoleRepository roleRepository;

    @Given("^create new role with (.+) name$")
    public void createNewRole(final String name) {
        final RoleResource roleResource = RoleResource.builder()
                .name(name)
                .build();

        scenarioContext.save(DataKeys.ROLE, roleResource);
    }

    @When("^send created role$")
    public void sendRole() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final RoleResource roleResource = scenarioContext.getDataByKey(DataKeys.ROLE, RoleResource.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, BEARER + token)
                .body(roleResource)
                .post(ROLE_API)
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^send created role once again$")
    public void sendRoleOnceAgain() {
        sendRole();
    }

    @When("^remove role with (.+) name$")
    public void deleteRoleByName(final String name) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, BEARER + token)
                .delete(ROLE_API + URI_SEPARATOR + name).thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @Then("^validate new role entity$")
    public void validateSavedNewEntity() {
        final RoleResource expected = scenarioContext.getDataByKey(DataKeys.ROLE, RoleResource.class);
        final Optional<Role> actual = roleRepository.findRoleByName(expected.getName());

        if (actual.isPresent()) {
            assertThat("Role names don't match!", actual.get().getName(), equalTo(expected.getName()));
        } else {
            fail("Role haven't been found in the database!");
        }
    }

    @Then("^validate deleted role entity$")
    public void validateDeletedEntity() {
        final RoleResource contextRole = scenarioContext.getDataByKey(DataKeys.ROLE, RoleResource.class);
        final Optional<Role> actual = roleRepository.findRoleByName(contextRole.getName());

        assertThat("Role hasn't been deleted!", actual, equalTo(Optional.empty()));
    }
}
