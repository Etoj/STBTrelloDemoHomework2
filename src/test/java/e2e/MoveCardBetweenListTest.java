package e2e;

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
public class MoveCardBetweenListTest extends BaseTest {

    private static String boardId;
    private static String firstListId;
    private static String secondListId;
    private static String cardId;

    @Test
    @Order(1)
    public void createNewBoard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My first board")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My first board");

        boardId = json.get("id");
    }

    @Test
    @Order(2)
    public void createFirstList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My first List")
                .when()
                .post(BASE_URL + "/" + BOARDS + "/" + boardId + "/" + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My first List");

        firstListId = json.getString("id");
    }

    @Test
    @Order(3)
    public void createSecondList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My second List")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + "/" + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My second List");

        secondListId = json.getString("id");
    }

    @Test
    @Order(4)
    public void createCard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", firstListId)
                .queryParam("name", "My first card")
                .when()
                .post(BASE_URL + "/" + CARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My first card");

        cardId = json.getString("id");
    }

    @Test
    @Order(5)
    public void moveCardToSecondList() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + "/" + CARDS + "/" + cardId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("idList")).isEqualTo(secondListId);
    }

    @Test
    @Order(6)
    public void deleteBoard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("idBoard", boardId)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
