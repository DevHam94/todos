package todoapp.web;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import todoapp.core.user.domain.ProfilePictureStorage;
import todoapp.security.UserSession;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UserController {

    private final ProfilePictureStorage profilePictureStorage;

    public UserController(ProfilePictureStorage profilePictureStorage) {
        this.profilePictureStorage = profilePictureStorage;
    }

    @RequestMapping("/user/profile-picture")
    @RolesAllowed("ROLE_USER")
    public @ResponseBody Resource profilePicture(UserSession userSession) throws IOException {
//        Path profilePicturePath = Paths.get(userSession.getUser().getProfilePicture().getUri());
        return profilePictureStorage.load(userSession.getUser().getProfilePicture().getUri());
//        return Files.readAllBytes(profilePicturePath);
    }
}
