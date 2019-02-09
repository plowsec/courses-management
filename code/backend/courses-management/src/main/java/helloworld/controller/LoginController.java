package helloworld.controller;

import helloworld.entity.IUser;
import helloworld.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    public ResponseEntity<IUser> whoami() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        IUser user = userService.getUserById(authentication.getName());
        HttpStatus status = user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(user, status);
    }
}
