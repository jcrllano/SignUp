package testinput.login.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Check;
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
import jakarta.validation.constraints.Null;
import lombok.var;
import testinput.login.dto.UserDto;
import testinput.login.entity.Checking;
import testinput.login.entity.CheckingTransactionForm;
import testinput.login.entity.ConfirmationTime;
import testinput.login.entity.ConfirmationToken;
import testinput.login.entity.Time;
import testinput.login.entity.Transactions;
import testinput.login.entity.User;
import testinput.login.repository.CheckingRepository;
import testinput.login.repository.ConfirmationTimeRepository;
import testinput.login.repository.ConfirmationTokenRepository;
import testinput.login.repository.TimeRepository;
import testinput.login.repository.TransactionsRepository;
import testinput.login.repository.UserRepository;
import testinput.login.service.ConfirmationService;
import testinput.login.service.TimeService;
import testinput.login.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage; 


@Controller
public class AppController {

    private UserService userService;
    
    @Autowired 
    private TimeService timeService; 

    @Autowired
    private ConfirmationService confirmationService;

    @Autowired
        ConfirmationTimeRepository confirmationTimeRepository;
    
    @Autowired
        private UserRepository userRepository;
    
    @Autowired
        private CheckingRepository checkingRepository;

   // @Autowired
   // private MailSender mailSender;
    
    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    public AppController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("home")
    public String home(){
        return "home";
    }
 
    @GetMapping("/forgotpassword")
    public ModelAndView forgotPassword(ModelAndView modelAndView, User user){
        modelAndView.addObject("user", user);
		modelAndView.setViewName("forgotpassword");
        return modelAndView; 
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    //https://github.com/ro6ley/java-forgot-pswd/blob/master/src/main/java/com/springsecurity/demo/controller/UserAccountController.java
    
    @PostMapping("/forgotpassword")
    public String postMethodName(@ModelAttribute("user") User user ) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        System.out.println("this is the existing user " + existingUser);
        if (existingUser != null) {
            // Create token
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
            //System.out.println("http://localhost:8082/confirm-reset?token=" + confirmationToken.getConfirmationToken());
            
            // Save it
            var UserToekIDExist = confirmationToken.getUser();
            confirmationTokenRepository.save(confirmationToken);
            
            // Create the email
            //SimpleMailMessage mailMessage = new SimpleMailMessage();
            //mailMessage.setTo(existingUser.getEmail());
            //mailMessage.setSubject("Complete Password Reset!");
            //mailMessage.setFrom("dolphinsnotredamfan@yahoo.com");
            //mailMessage.setText("To complete the password reset process, please click here: "
            //+ "http://localhost:8082/confirm-reset?token="+confirmationToken.getConfirmationToken());

            //mailSender.send(mailMessage);
            

            //modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
            //modelAndView.setViewName("successForgotPassword");

        //} else {
            //modelAndView.addObject("message", "This email address does not exist!");
            //modelAndView.setViewName("error");
        }
        return "redirect:/login";
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
        Checking checking = new Checking();
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        checking.setBalance("0");
        checking.setAvailableBalance("0");
        checkingRepository.save(checking);
        userService.saveUser(user);
        return "redirect:/register?success";
    } 

    @GetMapping("/customers")
    public String CustomerMainPage(Model model){
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        var loggedUser = userRepository.findByEmail(loggedInUser.getName());
        var customerID = checkingRepository.findById(loggedUser.getId()); 
        var customerID2 = userRepository.findById(loggedUser.getId());
        var availableBalance = "";
        var currentBalance = "";
        if (customerID.get().getId() == customerID2.get().getId()) {
            availableBalance = customerID.get().getAvailableBalance();
            currentBalance = customerID.get().getBalance();
        }
        
        Transactions transactions = new Transactions();
        
        //Iterable<Employee> emps = employeeRepository.findAllById(ids);
    
        System.out.println("this the transaction User ID " + transactionsRepository.findAllById(Transactions, 1));
        model.addAttribute("availableBalance", availableBalance);
        model.addAttribute("currentBalance", currentBalance); 
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("transactions", transactionsRepository.findAll());
        return "customers"; 
    } 

    @GetMapping("/maketransfer")
    public String makeTransfer(Model model) { 
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        var loggedUser = userRepository.findByEmail(loggedInUser.getName());
        //Transactions transactions = transactionsRepository.getReferenceById("1-2");
        Transactions transactions = new Transactions();
        Checking checkingList = checkingRepository.getReferenceById(loggedUser.getId());
        model.addAttribute("checkingList", checkingList);
        model.addAttribute("transactions", transactions);
        return "maketransfer"; 
    }

    @PostMapping("/maketransfer/save")
    public String makeTransferSave(@ModelAttribute("checkingList") Checking check, 
                                   @ModelAttribute("transactions") Transactions transaction, 
                                   @ModelAttribute("user") User user) { 
        
        //this function gets the date for the transactions
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        
        //this function will convert the integer into a string
        int hour = cal.getTime().getHours();
        int minutes = cal.getTime().getMinutes();
        String hourString = Integer.toString(hour);
        String minutesString = Integer.toString(minutes);
        String time = hourString + ":" + minutesString;

        String todaysdate = dateFormat.format(date);

        //gets the transactions class
        var transactions = new Transactions();
        
        //gets the logged user authentications
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        
        //gets the info frothe logged user
        var loggedUser = userRepository.findByEmail(loggedInUser.getName());
        
        //this variable is set the transcation balance
        String setTransactionBal = "";

        //this variable will get the ID of the checking table
        Integer checkingID = 0;

        //this funcation will get the available balance from the checking table using the logged user
        if (loggedUser.getId() != 0) {
            var checkingRepositoryID = checkingRepository.getReferenceById(loggedUser.getId());
            checkingID = loggedUser.getId();
            setTransactionBal = checkingRepositoryID.getAvailableBalance();
        }

        //this variable will set the transaction amount in the transactions table
        String setTransactionAmount = check.getAvailableBalance();

        check = checkingRepository.getReferenceById(loggedUser.getId());
        
        //this function convert the trnascation amount fro string to double and removes commas
        double tranAmountvalue = Double.parseDouble( setTransactionAmount.replace(",",".") );
        double tranAvailableBal = Double.parseDouble( setTransactionBal.replace(",",".") );

        //this will add the transaction amount value to the available balance value into a double
        double totalAvailableBal = tranAmountvalue + tranAvailableBal; 

        //this function converst from double to string
        String grandtotalAvailableBal = String.valueOf(totalAvailableBal);
        check.setAvailableBalance(grandtotalAvailableBal); 
        String customerCheckID = String.valueOf(checkingID);

        //this sets the currentlogged user ID into the userid column in the Transactions table
        User existingUser = loggedUser;
        
        transactions.setUser(existingUser);
        transactions.setDescription(transaction.getDescription()); 
        transactions.setAmount(setTransactionAmount);  
        transactions.setBalance(setTransactionBal); 
        transactions.setDate(todaysdate);
        transactions.setTime(time);
        transactionsRepository.save(transactions);
        checkingRepository.save(check);
        return "redirect:/customers";  
    } 

    @GetMapping("/appointmentsetup")
    public String appointmentSetUp(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        var loggedUser = userRepository.findByEmail(loggedInUser.getName());
        var times  = timeService.getAllInventory();
        model.addAttribute("times", times);
        model.addAttribute("loggedUser", loggedUser);
        return "appointmentsetup";
    }

     @GetMapping("/creditcardapply")
    public String creditCardApply(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        var loggedUser = userRepository.findByEmail(loggedInUser.getName());
        model.addAttribute("loggedUser", loggedUser);
        return "creditcardapply";
    }

    @GetMapping("/signup/{day}")
    String selectTime(Model model, @PathVariable String day) {
        day = "friday";
        ConfirmationTime listTime = new ConfirmationTime();
        model.addAttribute("listTime", listTime);
        return "confirmation";
    }
    @PostMapping("/save")
    public String saveTime(@ModelAttribute ConfirmationTime confirmationTime) {
        confirmationTimeRepository.save(confirmationTime);
        return "appointmentSucess";
    }
}
