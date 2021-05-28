package organizations;

import base.BaseTest;

import static io.restassured.RestAssured.given;
import static organizations.OrganizationsTest.organizationId;

public class DeleteOrganization extends BaseTest {

    public static void deleteOrganization() {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(200);
    }
}
