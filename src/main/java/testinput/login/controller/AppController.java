package testinput.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import testinput.login.entity.Time;
import testinput.login.entity.User;
import testinput.login.repository.ConfirmationTimeRepository;
import testinput.login.repository.TimeRepository;
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

    @Autowired
        ConfirmationTimeRepository confirmationTimeRepository;

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
         Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("this is the logged in user " + loggedInUser.getName());
    String email = loggedInUser.getName();
        var times  = timeService.getAllInventory();
        model.addAttribute("times", times);
        model.addAttribute("users", users);
        return "customers";
    } 

    @GetMapping("/signup/{day}")
    String selectTime(Model model, @PathVariable String day) {
        day = "friday";
        System.out.println("i been called");
        ConfirmationTime listTime = new ConfirmationTime();
        model.addAttribute("listTime", listTime);
        return "confirmation";
    }
    @PostMapping("/save")
    public String saveTime(@ModelAttribute ConfirmationTime confirmationTime) {
        confirmationTimeRepository.save(confirmationTime);
        return "redirect:/customers";
    }

   @GetMapping("/")
  public String currentUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model) {

    Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("this is the logged in user " + loggedInUser);
    String email = loggedInUser.getName(); 

    // User user = userR.findByEmailAddress(email);
    //String firstname = user.getFirstName();
     //model.addAttribute("firstName", firstname);
    //model.addAttribute("emailAddress", email);

    return "home"; //this is the name of my template
}
}
