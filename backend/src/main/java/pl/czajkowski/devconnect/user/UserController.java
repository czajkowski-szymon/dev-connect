package pl.czajkowski.devconnect.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.czajkowski.devconnect.user.models.RegistrationRequest;
import pl.czajkowski.devconnect.user.models.RegistrationResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        RegistrationResponse response = userService.register(request);
        return ResponseEntity.created(URI.create("/users/" + response.user().id())).body(response);
    }

    @PostMapping(
            value = "/{userId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadProfileImage(@PathVariable Integer userId, @RequestParam("file")MultipartFile file) {
        userService.uploadProfileImage(userId, file);
    }

    @GetMapping(
            value = "/{userId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] downloadProfileImage(@PathVariable Integer userId) {
        return userService.downloadProfileImage(userId);
    }
}