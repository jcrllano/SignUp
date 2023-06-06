package testinput.login;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImplementation implements CustomerService {
    
    private CustomerRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public CustomerServiceImplementation(CustomerRepository userRepository,
                                         RoleRepository roleRepository,
                                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserSetterGetter userSetterGetter) {
        Customers customers = new Customers();
        customers.setFirtsName(userSetterGetter.getFirstName());
        customers.setLastName(userSetterGetter.getLastName());
        customers.setEmail(userSetterGetter.getEmail());
        customers.setAddress(userSetterGetter.getAddress());
        customers.setCity(userSetterGetter.getCity());
        customers.setSsn(userSetterGetter.getSsn());
        customers.setState(userSetterGetter.getState());
        customers.setZipcode(userSetterGetter.getZipcode());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        customers.setPassword(passwordEncoder.encode(userSetterGetter.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        customers.setRoles(Arrays.asList(role));
        userRepository.save(customers);
    }

    @Override
    public Customers findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserSetterGetter> findAllUsers() {
        List<Customers> customers = userRepository.findAll();
        return customers.stream().map((customer) -> convertEntityToDto(customer))
                .collect(Collectors.toList());
    }

    private UserSetterGetter convertEntityToDto(Customers customers){
        UserSetterGetter userDto = new UserSetterGetter();
        userDto.setFirstName(userDto.getFirstName());
        userDto.setLastName(userDto.getLastName());
        userDto.setEmail(customers.getEmail());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("Role_Admin");
        return roleRepository.save(role);
    }
}
