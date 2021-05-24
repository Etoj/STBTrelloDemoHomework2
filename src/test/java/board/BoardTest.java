package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class BoardTest extends BaseTest {

    @Test
    public void createNewBoard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My first board")
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        //Assertions.assertEquals("My first board", json.get("name"));
        assertThat(json.getString("name")).isEqualTo("My first board");

        String boardId = json.get("id");

        given()
                .spec(reqSpec)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(200);
    }

    @Test
    public void createBoardWithEmptyBoardName() {
        given()
                .spec(reqSpec)
                .queryParam("name", "")
                .when()
                .post("https://api.trello.com/1/boards/")
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
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        //Assertions.assertEquals("Board without default list", json.get("name"));
        assertThat(json.getString("name")).isEqualTo("Board without default list");

        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get("https://api.trello.com/1/boards/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonGet = responseGet.jsonPath();
        List<String> idList = jsonGet.getList("id");
        //Assertions.assertEquals(0, idList.size());
        assertThat(idList).hasSize(0);

        given()
                .spec(reqSpec)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(200);
    }

    @Test
    public void createBoardWithDefaultList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board with default list")
                .queryParam("defaultLists", true)
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        //Assertions.assertEquals("Board with default list", json.get("name"));
        assertThat(json.getString("name")).isEqualTo("Board with default list");

        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get("https://api.trello.com/1/boards/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .response();

        //System.out.println(responseGet.prettyPrint());

        JsonPath jsonGet = responseGet.jsonPath();
        List<String> listName = jsonGet.getList("name");
        assertThat(listName).hasSize(3).contains("To Do", "Doing", "Done");

        given()
                .spec(reqSpec)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(200);
    }
}
