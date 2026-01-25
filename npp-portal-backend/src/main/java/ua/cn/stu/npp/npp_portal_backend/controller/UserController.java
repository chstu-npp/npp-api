package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateUserRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateUserRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserResponse;
import ua.cn.stu.npp.npp_portal_backend.enums.UserRole;
import ua.cn.stu.npp.npp_portal_backend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Byte departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/v1/users - role: {}, active: {}, departmentId: {}, page: {}, size: {}",
                role, active, departmentId, page, size);

        Page<UserResponse> users = userService.getAllUsers(role, active, departmentId, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        log.info("GET /api/v1/users/{}", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/v1/users - Creating user: {}", request.getEmail());
        UserResponse createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequest request) {

        log.info("PUT /api/v1/users/{} - Updating user", id);
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info("DELETE /api/v1/users/{} - Soft deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
