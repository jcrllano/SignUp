package testinput.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {
    Greeting greeting = new Greeting();


    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", greeting);
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        System.out.println(greeting.getEmail());
        System.out.println(greeting.getPassword() + " this is the content");
        String email =  "abbey@yahoo.com";
        String password = "XDR%6yhn";
        String UserInputemail =  greeting.getEmail();
        String UserInputpassword = greeting.getPassword();

        //this estatement will check the login input
        if (UserInputemail.equals(email)) {
            if (UserInputpassword.equals(password)) {
                model.addAttribute("greeting", greeting);  
                return "result";
            }
            return "result2";
        }
        else {
            return "result3";
        }
          
    }

    //this will invoke the sigup page
    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }
}
