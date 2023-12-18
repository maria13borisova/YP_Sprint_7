import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.api.client.CourierClient;
import ru.api.client.OrdersClient;
import ru.constants.Api;

public class GetOrderListTest {

    CourierClient courierClient = new CourierClient();
    OrdersClient ordersClient = new OrdersClient();
    private int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.BASE_URL;
        courierClient.createCourier();
        courierId = CourierClient.loginAndExtractCourierId();
    }

    @Test
    public void getOrderListIsSuccess(){
        ordersClient.getOrdersByCourierId(courierId);
    }

    @After
    public void endOfTest() {
        CourierClient.deleteCourierById(courierId);
    }

}
