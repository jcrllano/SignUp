package testinput.login.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import testinput.login.entity.ConfirmationTime;
import testinput.login.entity.Time;
import testinput.login.entity.Transactions;
import testinput.login.entity.User;
import testinput.login.repository.CheckingRepository;
import testinput.login.repository.ConfirmationTimeRepository;
import testinput.login.repository.TimeRepository;
import testinput.login.repository.TransactionsRepository;
import testinput.login.repository.UserRepository;
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
    
    @Autowired
        private UserRepository userRepository;
    
    @Autowired
        private CheckingRepository checkingRepository;
    
    @Autowired
    TransactionsRepository transactionsRepository;

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

    @GetMapping("/about")
    public String about() {
        return "about";
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
        Checking checkingList = checkingRepository.getReferenceById(loggedUser.getId());
        model.addAttribute("checkingList", checkingList);
        return "maketransfer";
    }

    @PostMapping("/maketransfer/save")
    public String makeTransferSave(@ModelAttribute("checkingList") Checking check) { 
        //this function gets the date for the transactions
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
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

        //this function counts the number of rows in the transaction table
        Long tranID = transactionsRepository.count();
        
        //this function will convert Long to integer
        int tranIDInteger = Math.toIntExact(tranID);
        check = checkingRepository.getReferenceById(loggedUser.getId());

        //this function will get the transaction ID of the latest table row of the transaction table
        String tranIDIndex = transactionsRepository.findAll().get(tranIDInteger - 1).getId();
        
        //this function will remove the - and split into the user ID and the transaction ID
        String [] tranIDsplitter = tranIDIndex.split("-");
        
        //this function gets the transaction ID as string
        String tranIDString = tranIDsplitter[1];

        //this function converst string ID into an integer
        int tranIDIndexInt = Integer.parseInt(tranIDString);

        //this function convert the trnascation amount fro string to double and removes commas
        double tranAmountvalue = Double.parseDouble( setTransactionAmount.replace(",",".") );
        double tranAvailableBal = Double.parseDouble( setTransactionBal.replace(",",".") );

        //this will add the transaction amount value to the available balance value into a double
        double totalAvailableBal = tranAmountvalue + tranAvailableBal; 

        //this function converst from double to string
        String grandtotalAvailableBal = String.valueOf(totalAvailableBal);
        check.setAvailableBalance(grandtotalAvailableBal); 
        String customerCheckID = String.valueOf(checkingID);
        String customerTransactionsID = String.valueOf(tranIDIndexInt + 1);

        //this fucntions will set the values into the database
        transactions.setId(customerCheckID + "-" + customerTransactionsID);
        transactions.setDescription("this is a test transaction"); 
        transactions.setAmount(setTransactionAmount);  
        transactions.setBalance(setTransactionBal); 
        transactions.setDate(todaysdate);
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
