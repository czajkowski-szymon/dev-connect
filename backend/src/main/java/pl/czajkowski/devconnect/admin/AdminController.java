package pl.czajkowski.devconnect.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PatchMapping("users/block/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> lockUser(@PathVariable Integer userId) {
        adminService.lockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
