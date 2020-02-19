package com.thekuzea.experimental.core.stepdef;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.UserResource;
import com.thekuzea.experimental.domain.model.User;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static com.thekuzea.experimental.core.fixture.ApiConstants.AUTHORIZATION;
import static com.thekuzea.experimental.core.fixture.ApiConstants.BEARER;
import static com.thekuzea.experimental.core.fixture.ApiConstants.URI_SEPARATOR;
import static com.thekuzea.experimental.core.fixture.ApiConstants.USER_API;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RequiredArgsConstructor
public class UserStepDefinition {

    @Value("${default.role}")
    private String defaultRole;

    private final ScenarioContext scenarioContext;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ObjectMapper objectMapper;

    @Given("^create new user with (.*) username and (.*) password$")
    public void createNewUser(final String username, final String password) {
        final UserResource userResource = UserResource.builder()
                .username(username)
                .password(password)
                .build();

        scenarioContext.save(DataKeys.USER_RESOURCE, userResource);
    }

    @When("^send created user$")
    public void sendUser() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final UserResource userResourceForSending = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class);

        final Response response = requestSpec
                .body(userResourceForSending)
                .post(USER_API)
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^send created user once again$")
    public void sendUserOnceAgain() {
        sendUser();
    }

    @When("^search for user with (.+) username$")
    public void findUserByUserName(final String username) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, BEARER + token)
                .get(USER_API + URI_SEPARATOR + username).thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^remove user with (.+) username$")
    public void deleteUserByUserName(final String username) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, BEARER + token)
                .delete(USER_API + URI_SEPARATOR + username).thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^prepare user for update$")
    public void prepareUserForUpdate() {
        final String currentUserId = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class).getId().toString();
        final UserResource userResource = UserResource.builder()
                .id(currentUserId)
                .build();

        scenarioContext.save(DataKeys.USER_RESOURCE, userResource);
    }

    @When("^update user's (.+) username$")
    public void updateUsernameOfUser(final String username) {
        scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class).setUsername(username);
    }

    @When("^update user's (.+) password$")
    public void updatePasswordOfUser(final String password) {
        scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class).setPassword(password);
    }

    @When("^update user's (.+) role$")
    public void updateRoleOfUser(final String role) {
        scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class).setRole(role);
    }

    @Then("^send updated user$")
    public void sendUpdatedUser() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final UserResource userResource = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, BEARER + token)
                .body(userResource)
                .put(USER_API + URI_SEPARATOR + userResource.getId())
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @Then("^save user in context$")
    public void saveUserInContext() {
        final UserResource userResource = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class);
        final Optional<User> foundUser = userRepository.findUserByUsername(userResource.getUsername());

        if (foundUser.isPresent()) {
            scenarioContext.save(DataKeys.USER_MODEL, foundUser.get());
        } else {
            fail("User hasn't been found in the database!");
        }
    }

    @Then("^validate new user entity$")
    public void validateSavedNewEntity() {
        final User actual = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);
        final UserResource expected = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
            softAssertions.assertThat(bCryptPasswordEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();
            softAssertions.assertThat(actual.getRole().getName()).isEqualTo(defaultRole);
        });
    }

    @Then("^validate received user entity$")
    public void validateReceivedEntity() {
        final Response response = scenarioContext.getDataByKey(DataKeys.RESPONSE, Response.class);
        final User expected = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);

        try {
            final UserResource actual = objectMapper.readValue(response.getBody().asByteArray(), UserResource.class);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getId()).isEqualTo(expected.getId().toString());
                softAssertions.assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
                softAssertions.assertThat(actual.getPassword()).isNull();
                softAssertions.assertThat(actual.getRole()).isEqualTo(expected.getRole().getName());
            });
        } catch (IOException e) {
            fail("Can't convert response body as byte array to object!");
        }
    }

    @Then("^validate updated user entity$")
    public void validateUpdatedEntity() {
        final User actual = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);
        final UserResource expected = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserResource.class);
        int assertions = 0;

        if (!isBlank(expected.getUsername())) {
            assertThat("Usernames don't match!", actual.getUsername(), equalTo(expected.getUsername()));
            ++assertions;
        }
        if (!isBlank(expected.getPassword())) {
            assertTrue("Passwords don't match!", bCryptPasswordEncoder.matches(expected.getPassword(), actual.getPassword()));
            ++assertions;
        }
        if (!isBlank(expected.getRole())) {
            assertThat("Roles don't match!", actual.getRole().getName(), equalTo(expected.getRole()));
            ++assertions;
        }

        assertThat("At least one assertion must be done!", assertions, greaterThan(0));
    }

    @Then("^validate deleted user entity$")
    public void validateDeletedEntity() {
        final User contextEntity = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);
        final Optional<User> actual = userRepository.findUserByUsername(contextEntity.getUsername());

        assertThat("User hasn't been deleted!", actual, equalTo(Optional.empty()));
    }
}
