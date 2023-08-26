package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.individuals.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomUserController {
    private final CustomUserService customUserService;

    @Autowired
    public CustomUserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }


    @GetMapping("/all-users")
    public ResponseEntity<ListResponse<CustomUser>> getAllRegisteredUsers() {
        return ResponseEntity.ok(customUserService.getAllCustomUsers());
    }

}
