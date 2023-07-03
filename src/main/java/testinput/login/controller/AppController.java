package testinput.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import testinput.login.dto.UserDto;
import testinput.login.entity.ConfirmationTime;
import testinput.login.entity.User;
import testinput.login.service.ConfirmationService;
import testinput.login.service.TimeService;
import testinput.login.service.UserService;

@Controller
public class AppController {

    private UserService userService;
    
    @Autowired 
    private TimeService timeService; 

    @Autowired
    private ConfirmationService confirmationService;

    public AppController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("home")
    public String home(){
        return "home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

   /*  @GetMapping("/signup/{day}")
    String selectTime(Model model, @PathVariable String day) {
        day = "Monday";
        var listTime = confirmationService.getAllAvailableTimes();
        model.addAttribute("listTime", listTime);
        return "confirmation";
    }*/

    // handler method to handle user registration request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/customers")
    public String listRegisteredUsers(Model model){
        System.out.println(timeService.getAllInventory() + "this is the all time inventory");
        List<UserDto> users = userService.findAllUsers();
        var times  = timeService.getAllInventory();
        model.addAttribute("times", times);
        model.addAttribute("users", users);
        return "customers";
    } 
}
