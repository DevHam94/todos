package todoapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import todoapp.core.user.application.UserPasswordVerifier;
import todoapp.core.user.application.UserRegistration;
import todoapp.web.model.SiteProperties;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    private final UserPasswordVerifier userPasswordVerifier;
    private final UserRegistration userRegistration;
    //private final SiteProperties siteProperties;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LoginController(UserPasswordVerifier userPasswordVerifier, UserRegistration userRegistration) {
        this.userPasswordVerifier = userPasswordVerifier;
        this.userRegistration = userRegistration;
    }

    /*
    @ModelAttribute("site")
    public SiteProperties siteProperties() {
        return siteProperties;
    }
     */

    @GetMapping("/login")
    public void loginForm() {

    }

    @PostMapping("/login")
    public void loginProcess(@RequestParam String username,
                             @RequestParam String password) {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");

        logger.debug("login command: {}, {}", username, password);
    }

    static class LoginCommand {

        String username;
        String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "LoginCommand{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
