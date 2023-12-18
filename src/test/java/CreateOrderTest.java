import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.api.client.OrdersClient;
import ru.constants.Api;
import ru.pojo.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(Parameterized.class)
public class CreateOrderTest {

    private List<String> listOfColors;
    OrdersClient ordersClient = new OrdersClient();

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
        RestAssured.baseURI = Api.BASE_URL;
    }

    @Test
    @DisplayName("Create order")
    @Description("Создание заказа с указанием параметров")
    public void createOrderIsSuccess() {
        Order order = new Order(listOfColors);
        Response response = ordersClient.createOrderResponse(order);
        ordersClient.checkOrderIsCreated(response);
    }

}
