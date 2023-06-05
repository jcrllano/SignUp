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


    //this will invoke the sigup page
    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }
}
