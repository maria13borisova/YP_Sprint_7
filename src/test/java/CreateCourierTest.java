import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import ru.api.client.CourierClient;
import ru.constants.Api;



public class CreateCourierTest {

    CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        RestAssured.baseURI= Api.BASE_URL;
    }


    /* Кейс: успешное создание курьера */
    @Test
    @DisplayName("Successfully creating a courier")
    @Description("Проверка работы API при создании курьера с корректными данными")
    public void createCourierIsSuccess(){
        courierClient.createCourier();
        int courierId = CourierClient.loginAndExtractCourierId();
        CourierClient.deleteCourierById(courierId);
    }

    /* Кейс: создание двух одинаковых курьеров */
    @Test
    @DisplayName("Creating duplicate couriers")
    @Description("Проверка работы API при создании двух курьеров с одинаковыми данными")
    public void createDuplicateCourierIsFailed(){
        courierClient.createCourier();
        courierClient.createDuplicateCourier();
        int courierId = CourierClient.loginAndExtractCourierId();
        CourierClient.deleteCourierById(courierId);
    }

    /* Кейс: не заполнены обязательные поля для создания курьера */
    @Test
    @DisplayName("Creating courier without password field")
    @Description("Проверка работы API при создании курьера без указания пароля")
    public void createCourierWithNullPasswordFailed(){
        courierClient.createBadCourier();
    }

}
