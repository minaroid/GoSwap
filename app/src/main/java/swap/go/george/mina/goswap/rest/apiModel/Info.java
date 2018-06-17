
package swap.go.george.mina.goswap.rest.apiModel;

import java.util.HashMap;
import java.util.Map;

public class Info {

    private String name;
    private int swapperId;
    private String email;
    private String password;
    private int phone;
    private String pic;
    private String fbId;

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSwapperId() {
        return swapperId;
    }

    public void setSwapperId(int swapperId) {
        this.swapperId = swapperId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
