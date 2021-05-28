package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class BoardTest extends BaseTest {

    public static String boardId;

    @Test
    public void createNewBoard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My first board")
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My first board");

        boardId = json.get("id");
        DeleteBoard.deleteBoard();
    }

    @Test
    public void createBoardWithEmptyBoardName() {
        given()
                .spec(reqSpec)
                .queryParam("name", "")
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    public void createBoardWithoutDefaultList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board without default list")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Board without default list");

        boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + BOARDS + "/" + boardId + "/" + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonGet = responseGet.jsonPath();
        List<String> idList = jsonGet.getList("id");
        assertThat(idList).hasSize(0);

        DeleteBoard.deleteBoard();
    }

    @Test
    public void createBoardWithDefaultList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board with default list")
                .queryParam("defaultLists", true)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Board with default list");

        boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + BOARDS + "/" + boardId + "/" + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonGet = responseGet.jsonPath();
        List<String> listName = jsonGet.getList("name");
        assertThat(listName).hasSize(3).contains("To Do", "Doing", "Done");

        DeleteBoard.deleteBoard();
    }
}
