package denti.example.dentiplusclient;

public class Users {

    public String name,email,phone,request;

    public Users(String name, String email, String phone,String request) {
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.request=request;
    }

    public  Users(){}

    public String getName() {
        return name;
    }
}
