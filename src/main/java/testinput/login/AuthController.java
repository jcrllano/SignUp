package testinput.login;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AuthController {
    @Autowired
    private CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String home(){
        System.out.println("hello world");
        return "home";
    }

    @GetMapping("/login")
    public String loginForm() { 
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserSetterGetter user = new UserSetterGetter();
        model.addAttribute("user", user);
        return "register";
    }

     // handler method to handle register user form submit request
     @PostMapping("/register/save")
     public String registration(@Valid @ModelAttribute("user") UserSetterGetter user,
                                BindingResult result,
                                Model model){
         Customers existing = customerService.findByEmail(user.getEmail());
         if (existing != null) {
             result.rejectValue("email", null, "There is already an account registered with that email");
         }
         if (result.hasErrors()) {
             model.addAttribute("user", user);
             return "register";
         }
         customerService.saveUser(user);
         return "redirect:/register?success";
     } 

     @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserSetterGetter> users = customerService.findAllUsers();
        System.out.println(users);
        model.addAttribute("users", users);
        return "users";
    }
   
} 
