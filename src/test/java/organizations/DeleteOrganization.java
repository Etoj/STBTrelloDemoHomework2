package organizations;

import base.BaseTest;

import static io.restassured.RestAssured.given;
import static organizations.OrganizationsTest.organizationId;
import static organizations.OrganizationsTest.response;

public class DeleteOrganization extends BaseTest {

    public static void deleteOrganization() {
        //System.out.println(response.statusCode());
        if (response.statusCode() == 200) {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                    .then()
                    .statusCode(200);
        } else if (response.statusCode() == 201) {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                    .then()
                    .statusCode(201);
        } else
            System.out.println("The organization was not created, delete was omitted");//
    }
}
