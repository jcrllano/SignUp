package testinput.login.dto;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import testinput.login.service.TimeService;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    TimeService timeService;

    private Integer id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    
    private String ssn;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipcode;
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;

}
