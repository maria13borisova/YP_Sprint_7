import POJO.Order;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.greaterThan;


@RunWith(Parameterized.class)
public class CreateOrderTest {

    private List<String> listOfColors;

    public CreateOrderTest(List<String> listOfColors) {
        if(listOfColors != null) {
            this.listOfColors = new ArrayList<>(listOfColors);
        }
        else {
                this.listOfColors = new ArrayList<>();
            }
        }


    @Parameterized.Parameters
    public static Object[][] getOrderColors() {
        /* тестовые данные */
        return new Object[][] {
                { new ArrayList<>(Arrays.asList("BLACK")) },
                { new ArrayList<>(Arrays.asList("GREY")) },
                { new ArrayList<>(Arrays.asList("BLACK", "GREY")) },
                { new ArrayList<>() }
        };
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createOrderIsSuccess() {
        Order order = new Order(listOfColors);
        Response response = createOrderResponse(order);
        checkOrderIsCreated(response);
    }

    /* Шаги для тест-кейсов */
    @Step("Create order")
    public Response createOrderResponse(Order order){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        return response;
    }

    @Step("Check order is created")
    public void checkOrderIsCreated(Response response){
        response.then().statusCode(201)
                .and()
                .body("track", any(Integer.class))
                .and()
                .body("track", greaterThan(0));
    }
}
