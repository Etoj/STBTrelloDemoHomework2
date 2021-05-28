package board;

import base.BaseTest;

import static board.BoardTest.boardId;
import static io.restassured.RestAssured.given;

public class DeleteBoard extends BaseTest {

    public static void deleteBoard() {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(200);
    }
}
