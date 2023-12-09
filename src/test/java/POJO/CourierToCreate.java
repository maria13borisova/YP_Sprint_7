package POJO;

import POJO.Courier;

public class CourierToCreate extends Courier {

    private String firstName;

    public CourierToCreate(){
        super();
    }

    public CourierToCreate(String login, String password, String firstName){
        super(login, password);
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
