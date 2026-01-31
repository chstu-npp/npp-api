package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.AssignDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateDepartmentAssignmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateUserDetailsRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserDetailsResponse;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserDepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.service.UserDetailsService;
import ua.cn.stu.npp.npp_portal_backend.service.UserDepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserDetailsService userDetailsService;
    private final UserDepartmentService userDepartmentService;

    @GetMapping("/{id}/details")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable Integer id) {
        log.info("GET /api/v1/users/{}/details", id);
        UserDetailsResponse userDetails = userDetailsService.getUserDetails(id);
        return ResponseEntity.ok(userDetails);
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<UserDetailsResponse> updateUserDetails(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserDetailsRequest request) {

        log.info("PUT /api/v1/users/{}/details - Updating user details", id);
        UserDetailsResponse updatedDetails = userDetailsService.updateUserDetails(id, request);
        return ResponseEntity.ok(updatedDetails);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<Void> uploadUserPhoto(
            @PathVariable Integer id,
            @RequestParam String photoUrl) {

        log.info("POST /api/v1/users/{}/photo - Uploading photo", id);
        userDetailsService.updateUserPhoto(id, photoUrl);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<Void> deleteUserPhoto(@PathVariable Integer id) {
        log.info("DELETE /api/v1/users/{}/photo - Deleting photo", id);
        userDetailsService.deleteUserPhoto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/departments")
    public ResponseEntity<List<UserDepartmentResponse>> getUserDepartments(@PathVariable Integer id) {
        log.info("GET /api/v1/users/{}/departments", id);
        List<UserDepartmentResponse> departments = userDepartmentService.getUserDepartments(id);
        return ResponseEntity.ok(departments);
    }

    @PostMapping("/{id}/departments")
    public ResponseEntity<Void> assignDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody AssignDepartmentRequest request) {

        log.info("POST /api/v1/users/{}/departments - Assigning department: {}", id, request.getDepartmentId());
        userDepartmentService.assignDepartment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}/departments/{departmentId}")
    public ResponseEntity<Void> updateUserDepartment(
            @PathVariable Integer userId,
            @PathVariable Byte departmentId,
            @Valid @RequestBody UpdateDepartmentAssignmentRequest request) {

        log.info("PUT /api/v1/users/{}/departments/{} - Updating department assignment", userId, departmentId);
        userDepartmentService.updateUserDepartment(userId, departmentId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/departments/{departmentId}")
    public ResponseEntity<Void> removeDepartment(
            @PathVariable Integer userId,
            @PathVariable Byte departmentId) {

        log.info("DELETE /api/v1/users/{}/departments/{} - Removing department", userId, departmentId);
        userDepartmentService.removeDepartment(userId, departmentId);
        return ResponseEntity.noContent().build();
    }
}