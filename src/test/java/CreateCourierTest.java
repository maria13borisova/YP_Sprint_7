import POJO.Courier;
import POJO.CourierID;
import POJO.CourierToCreate;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class CreateCourierTest {

    /* Данные для теста */
    private String login = "Bulka804";
    private String password = "1234";
    private String firstName = "Bulka Hlebovna";

    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }


    /* Кейс: успешное создание курьера */
    @Test
    @DisplayName("Successfully creating a courier")
    public void createCourierIsSuccess(){
        CourierToCreate courier = new CourierToCreate(login, password, firstName);
        Response response = getResponseFromApiCreate(courier);
        response.then().statusCode(201)
                .and()
                .body("ok", equalTo(true));
        clearAfterTest(courier);
    }

    /* Кейс: создание двух одинаковых курьеров */
    @Test
    @DisplayName("Creating duplicate couriers")
    public void createDuplicateCourierIsFailed(){
        CourierToCreate courier1 = new CourierToCreate(login, password, firstName);
        CourierToCreate courier2 = new CourierToCreate(login, password, firstName);

        Response response1 = getResponseFromApiCreate(courier1);
        response1.then().statusCode(201)
                .and()
                .body("ok", equalTo(true));;

        Response response2 = getResponseFromApiCreate(courier2);
        response2.then().statusCode(409);

        clearAfterTest(courier1);
    }

    /* Кейс: не заполнены обязательные поля для создания курьера */
    @Test
    @DisplayName("Creating courier with null field")
    public void createCourierWithNullPasswordFailed(){
        CourierToCreate courier = new CourierToCreate(login, null, firstName);
        Response response = getResponseFromApiCreate(courier);
        response.then().statusCode(400);
    }

    /* Кейс: создание курьера с существующим логином */
    @Test
    @DisplayName("Creating courier with same login")
    public void createCourierWithSameLoginIsFailed(){
        CourierToCreate courier1 = new CourierToCreate(login, password, firstName);
        CourierToCreate courier2 = new CourierToCreate(login, "2626", "Other name");

        Response response1 = getResponseFromApiCreate(courier1);
        response1.then().statusCode(201)
                .and()
                .body("ok", equalTo(true));;

        Response response2 = getResponseFromApiCreate(courier2);
        response2.then().statusCode(409);

        clearAfterTest(courier1);

    }



/* Шаги для тест-кейсов */

    @Step("Get response from API Create courier")
    public Response getResponseFromApiCreate(CourierToCreate courier){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }


    @Step("Get response from API Login courier")
    public Response getResponseFromApiLogin(Courier courier){
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
    public int getCourierIdByResponse(Response response){
        CourierID courierID = response.as(CourierID.class);
        return courierID.getCourierID();
    }

    @Step("Remove courier by id")
    public void removeCourierById(int courierId){
        String fullPath = "/api/v1/courier/" + courierId;
        given()
                .and()
                .when()
                .delete(fullPath)
                .then().statusCode(200);
    }

    @Step("Remove courier after test, full path")
    public void clearAfterTest(Courier courier){
        Response response = getResponseFromApiLogin(courier);
        int courierID = getCourierIdByResponse(response);
        removeCourierById(courierID);
    }
}
