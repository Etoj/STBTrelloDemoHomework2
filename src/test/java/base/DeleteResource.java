package base;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteResource {

    public static void deleteResourceById(String BASE_URL, String endpoint, String resourceId, Response response) {

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            given()
                    .spec(BaseTest.reqSpec)
                    .when()
                    .delete(BASE_URL + "/" + endpoint + "/" + resourceId)
                    .then()
                    .statusCode(200);

        } else
            System.out.println("The resource was not created, delete was omitted");//
    }
}
