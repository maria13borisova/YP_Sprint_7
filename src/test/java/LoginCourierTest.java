import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.api.client.CourierClient;
import ru.constants.Api;

public class LoginCourierTest {

    CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.BASE_URL;
        courierClient.createCourier();
    }

    /* Успешная авторизация */
    @Test
    @DisplayName("Successful auth")
    @Description("Успешная авторизация курьера")
    public void courierLoginIsSuccess() {
        courierClient.loginCourier();
    }

    /* Авторизация с некорректным логином */
    @Test
    @DisplayName("Incorrect login auth is failed")
    @Description("Авторизация с некорректным логином не прошла")
    public void courierIncorrectLoginIsFailed() {
        courierClient.badLoginCourier();
    }


    /* Авторизация с некорректным паролем */
    @Test
    @DisplayName("Incorrect password auth is failed")
    @Description("Авторизация с некорректным паролем не прошла")
    public void courierIncorrectPasswordIsFailed() {
        courierClient.badPasswordCourier();
    }


    @After
    public void endOfTest() {
        int courierId = CourierClient.loginAndExtractCourierId();
        CourierClient.deleteCourierById(courierId);
    }
}