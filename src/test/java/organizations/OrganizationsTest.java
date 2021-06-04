package organizations;

import base.BaseTest;
import base.DeleteResource;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class OrganizationsTest extends BaseTest {

    public static String organizationId;
    public static Response response;

    @Order(1)
    @Test
    public void createNewOrganization() {

        response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Moja testowa organizacja")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Moja testowa organizacja");
        organizationId = json.getString("id");

        DeleteResource.deleteResourceById(BASE_URL, ORGANIZATIONS, organizationId, response);

    }

    @Order(2)
    @Test
    public void createNewOrganizationWithoutDisplayName() {

        response = given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Order(3)
    @Test
    public void checkIfApiConvertWrongParametersToCorrectOne() {

        response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Organization name less then 3")
                .queryParam("name", "NA")
                .queryParam("website", "website.name")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Organization name less then 3");
        assertThat(json.getString("name"))
                .isLowerCase()
                .startsWith("na");
        assertThat(json.getString("website"))
                .startsWith("http://")
                .contains("website.name");
        organizationId = json.getString("id");

        DeleteResource.deleteResourceById(BASE_URL, ORGANIZATIONS, organizationId, response);
    }
}
