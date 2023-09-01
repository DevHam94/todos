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
import todoapp.core.user.domain.UserEntityNotFoundException;
import todoapp.web.model.SiteProperties;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;

@Controller
public class LoginController {

    private final UserPasswordVerifier userPasswordVerifier;
    private final UserRegistration userRegistration;
    private final SiteProperties siteProperties;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LoginController(UserPasswordVerifier userPasswordVerifier, UserRegistration userRegistration, SiteProperties siteProperties) {
        this.userPasswordVerifier = userPasswordVerifier;
        this.userRegistration = userRegistration;
        this.siteProperties = siteProperties;
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
    public String loginProcess(@Valid LoginCommand command) {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
        logger.debug("login command: {}", command);

        if(command.getUsername().length() < 4) {
            // 입력값이 올바르지 않습니다.

        }

        try {
            // 1. 사용자 저장소에 사용자가 있을 경우: 비밀번호 확인 후 로그인 처리
            userPasswordVerifier.verify(command.getUsername(), command.getPassword());
        } catch (UserEntityNotFoundException error) {
            // 2. 사용자가 없는 경우: 회원가입 처리 후 로그인 처리
            userRegistration.join(command.getUsername(), command.getPassword());
        }

        return "redirect:/todos";

    }

    static class LoginCommand {

        @Size(min = 4, max = 20)
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
