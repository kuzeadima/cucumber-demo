package com.thekuzea.experimental.core.hook;

import com.thekuzea.experimental.config.ContextConfig;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.util.FileUtils;
import com.thekuzea.experimental.core.fixture.ApiConstants;
import com.thekuzea.experimental.core.fixture.DataKeys;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import javax.sql.DataSource;

import java.nio.file.Paths;
import java.sql.SQLException;

@SpringBootTest(classes = ContextConfig.class)
@RequiredArgsConstructor
public class BaseHook {

    private final ScenarioContext scenarioContext;

    private final DataSource dataSource;

    @Before(order = 1)
    public void setUp() throws SQLException {
        final RequestSpecification requestSpecification = RestAssured.given()
                .log()
                .all()
                .baseUri(ApiConstants.BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        scenarioContext.save(DataKeys.PREDEFINED_REST_TEMPLATE, requestSpecification);
        FileUtils.loadSqlAndExecute(dataSource, Paths.get("sql", "cleanup.sql").toString());
    }

    @Before(value = "@InsertUserRole", order = 2)
    public void insertRoles() throws SQLException {
        FileUtils.loadSqlAndExecute(dataSource, Paths.get("sql", "insert_roles.sql").toString());
    }

    @Before(value = "@InitializeAdministrator", order = 3)
    public void insertAdminUser() throws SQLException {
        FileUtils.loadSqlAndExecute(dataSource, Paths.get("sql", "initialize_administrator.sql").toString());
    }

    @After(value = "@Clean")
    public void tearDown() {
        scenarioContext.clean();
    }
}
