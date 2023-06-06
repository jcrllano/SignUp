package testinput.login;

import java.util.List;

public interface CustomerService {
    void saveUser(UserSetterGetter userSetterGetter);

    Customers findByEmail(String email);

    List<UserSetterGetter> findAllUsers();
}
