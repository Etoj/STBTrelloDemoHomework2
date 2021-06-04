package organizations;

import base.BaseTest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteOrganization {

    public static void deleteOrganization(String BASE_URL, String ORGANIZATIONS, String organizationId, Response response) {

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            given()
                    .spec(BaseTest.reqSpec)
                    .when()
                    .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                    .then()
                    .statusCode(200);

        } else
            System.out.println("The organization was not created, delete was omitted");//
    }
}
