package testinput.login.service;

import java.util.List;

import testinput.login.dto.UserDto;
import testinput.login.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
