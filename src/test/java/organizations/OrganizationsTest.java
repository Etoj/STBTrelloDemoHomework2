package organizations;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class OrganizationsTest extends BaseTest {

    private static String organizationId;
    private static String organizationWithWrongParamId;

    @Order(1)
    @Test
    public void createNewOrganization() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Moja testowa organizacja")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Moja testowa organizacja");
        organizationId = json.getString("id");
    }

    @Order(2)
    @Test
    public void deleteOrganization() {

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(200);
    }

    @Order(3)
    @Test
    public void createNewOrganizationWithoutDisplayName() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Order(4)
    @Test
    public void checkIfApiConvertWrongParametersToCorrectOne() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Organization name less then 3")
                .queryParam("name", "NA")
                .queryParam("website", "website.name")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Organization name less then 3");
        assertThat(json.getString("name")).isLowerCase();
        assertThat(json.getString("name")).startsWith("na");
        assertThat(json.getString("website")).startsWith("http://");
        assertThat(json.getString("website")).contains("website.name");
        organizationWithWrongParamId = json.getString("id");
    }

    @Order(5)
    @Test
    public void deleteOrganizationWithWrongInitialParameters() {

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationWithWrongParamId)
                .then()
                .statusCode(200);
    }

}
