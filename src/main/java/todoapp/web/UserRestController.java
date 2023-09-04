package todoapp.web;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;
import todoapp.core.user.application.ProfilePictureChanger;
import todoapp.core.user.domain.ProfilePicture;
import todoapp.core.user.domain.ProfilePictureStorage;
import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.model.UserProfile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RolesAllowed("ROLE_USER")
public class UserRestController {
/*    private final UserSessionRepository userSessionRepository;

    public UserRestController(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }*/

    private final ProfilePictureChanger profilePictureChanger;
    private final ProfilePictureStorage profilePictureStorage;
    private final UserSessionRepository userSessionRepository;

    public UserRestController(ProfilePictureChanger profilePictureChanger, ProfilePictureStorage profilePictureStorage, UserSessionRepository userSessionRepository) {
        this.profilePictureChanger = profilePictureChanger;
        this.profilePictureStorage = profilePictureStorage;
        this.userSessionRepository = userSessionRepository;
    }

    @GetMapping("/api/user/profile")
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

    @PostMapping("/api/user/profile-picture")
    public UserProfile changeProfilePicture(MultipartFile profilePicture, UserSession userSession) throws IOException {

        // 1. 업로드된 프로필 이미지 파일 저장하기
//        Path basePath = Paths.get("./files/user-profile-picture");
//        if(!basePath.toFile().exists()) {
//            basePath.toFile().mkdirs();
//        }
//        Path profilePicturePath = basePath.resolve(profilePicture.getOriginalFilename());
//        profilePicture.transferTo(profilePicturePath);
        URI profilePictureUri = profilePictureStorage.save(profilePicture.getResource());;

        // 2. 프로필 이미지 변경 후 세션을 갱신하기
        User updatedUser = profilePictureChanger.change(userSession.getName(), new ProfilePicture(profilePictureUri));
        userSessionRepository.set(new UserSession(updatedUser));


        return new UserProfile(updatedUser);
    }
}
