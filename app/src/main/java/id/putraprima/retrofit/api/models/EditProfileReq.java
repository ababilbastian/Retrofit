package id.putraprima.retrofit.api.models;

public class EditProfileReq {
    public String name,email;

    public EditProfileReq(String email,String name) {
        this.email = email;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
