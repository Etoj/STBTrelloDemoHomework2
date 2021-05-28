package board;

import base.BaseTest;

import static board.BoardTest.boardId;
import static io.restassured.RestAssured.given;
import static board.BoardTest.response;

public class DeleteBoard extends BaseTest {

    public static void deleteBoard() {
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            given()
                    .spec(reqSpec)
                    .when()
                    .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                    .then()
                    .statusCode(200);
        } else
            System.out.println("Something goes wrong");
    }
}
