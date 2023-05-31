package testinput.login;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name="sign_up_members")
@Getter
@Setter
public class SignUpForm {
    private String email;
    private String password;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipcode;
}
