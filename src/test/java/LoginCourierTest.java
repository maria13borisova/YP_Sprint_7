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
import static org.hamcrest.Matchers.*;

public class LoginCourierTest {


    /* Данные для теста */
    private String login = "Bulka804";
    private String password = "1234";
    private String firstName = "New Bulka";
    private CourierToCreate courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = new CourierToCreate(login, password, firstName);
        Response response = getResponseFromApiCreate(courier);
        //response.then().statusCode(201);
    }

    /* Успешная авторизация */
    @Test
    public void courierLoginIsSuccess() {
        Courier courierLogin = new Courier(courier.getLogin(), courier.getPassword());
        Response response = getResponseFromApiLogin(courierLogin);
        response.then()
                .statusCode(200)
                .and()
                .body("id", any(Integer.class))
                .and()
                .body("id", greaterThan(0));
    }

    /* Авторизация с некорректным логином */
    @Test
    public void courierIncorrectLoginIsFailed() {
        Courier courierLogin = new Courier("Bulka815", courier.getPassword());
        Response response = getResponseFromApiLogin(courierLogin);
        checkError404(response);
    }


    /* Авторизация с некорректным паролем */
    @Test
    public void courierIncorrectPasswordIsFailed() {
        Courier courierLogin = new Courier(courier.getLogin(), "4321");
        Response response = getResponseFromApiLogin(courierLogin);
        checkError404(response);
    }

    /* Авторизация с пустым паролем */
    @Test
    public void courierWithEmptyPasswordIsFailed() {
        Courier courierLogin = new Courier(courier.getLogin(), "");
        Response response = getResponseFromApiLogin(courierLogin);
        response.then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    /* Авторизация несуществующего пользователя */
    @Test
    public void courierIsNotExistLoginIsFailed() {
        Courier courierLogin = new Courier("Bobr88888888", "1234");
        Response response = getResponseFromApiLogin(courierLogin);
        checkError404(response);
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


    @Step("Check error is 404")
    public boolean checkError404(Response response) {
        try {
            response.then()
                    .statusCode(404)
                    .and()
                    .body("message", equalTo("Учетная запись не найдена"));
            return true;}
        catch(AssertionError error){
            System.out.println("error");
                return false;
            }
    }
}