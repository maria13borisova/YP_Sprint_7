import POJO.Courier;
import POJO.CourierID;
import POJO.CourierToCreate;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasKey;

public class GetOrderListTest {
    private String login = "Bulka804";
    private String password = "1234";
    private String firstName = "Bulka";
    private CourierToCreate courier;

    private int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = new CourierToCreate(login, password, firstName);
        getResponseFromApiCreate(courier);
        Response responseLogin = getResponseFromApiLogin(courier);
        courierId = getCourierIdByResponse(responseLogin);
    }

    @Test
    public void getOrderListIsSuccess(){
        String fullPath = "/api/v1/orders?courierId=" + courierId;
        given()
                .get(fullPath)
                .then().statusCode(200)
                .and().body("orders", hasItems());
    }

    @After
    public void endOfTest() {
        clearAfterTest(courier);
    }

    /* Шаги для тест-кейсов */

    @Step("Get response from API Create courier")
    public Response getResponseFromApiCreate(CourierToCreate courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }


    @Step("Get response from API Login courier")
    public Response getResponseFromApiLogin(Courier courier) {
        /* Приводим courier к классу Courier */

        if (courier instanceof CourierToCreate) {
            courier = new Courier(courier.getLogin(), courier.getPassword());
        }

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Get courier ID by response")
    public int getCourierIdByResponse(Response response) {
        CourierID courierID = response.as(CourierID.class);
        return courierID.getCourierID();
    }

    @Step("Remove courier by id")
    public void removeCourierById(int courierId) {
        String fullPath = "/api/v1/courier/" + courierId;
        given()
                .and()
                .when()
                .delete(fullPath)
                .then().statusCode(200);
    }

    @Step("Remove courier after test, full path")
    public void clearAfterTest(Courier courier) {
        Response response = getResponseFromApiLogin(courier);
        int courierID = getCourierIdByResponse(response);
        removeCourierById(courierID);
    }
}
