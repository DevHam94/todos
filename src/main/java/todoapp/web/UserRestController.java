package todoapp.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.model.UserProfile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
public class UserRestController {
/*    private final UserSessionRepository userSessionRepository;

    public UserRestController(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }*/

    @GetMapping("/api/user/profile")
    @RolesAllowed("ROLE_USER")
    public ResponseEntity<UserProfile> userProfile(UserSession userSession) {
        //User user = (User) session.getAttribute("user");
        //UserSession userSession = userSessionRepository.get();
        //HandlerMethodArgumentResolver

//        if (Objects.nonNull(userSession)) {
//            return ResponseEntity.ok(new UserProfile(userSession.getUser()));
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(new UserProfile(userSession.getUser()));
    }
}
