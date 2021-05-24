package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected final static String BASE_URL = "https://api.trello.com/1";
    protected final static String BOARDS = "boards";
    protected final static String LISTS = "lists";
    protected final static String CARDS = "cards";
    protected final static String ORGANIZATIONS = "organizations";

    protected final static String KEY = "YOUR_KEY";
    protected final static String TOKEN = "YOUR_TOKEN";

    private static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    @BeforeAll
    public static void beforeAll(){
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        reqSpec =reqBuilder.build();
    }
}
