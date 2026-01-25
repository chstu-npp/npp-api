package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.DepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getDepartments(
            @RequestParam(required = false) Byte facultyId,
            @RequestParam(required = false) Boolean active) {

        log.info("GET /api/v1/departments - facultyId: {}, active: {}", facultyId, active);
        List<DepartmentResponse> departments = departmentService.getAllDepartments(facultyId, active);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Byte id) {
        log.info("GET /api/v1/departments/{}", id);
        DepartmentResponse department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {

        log.info("POST /api/v1/departments - Creating department: {}", request.getName());
        DepartmentResponse createdDepartment = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Byte id,
            @Valid @RequestBody UpdateDepartmentRequest request) {

        log.info("PUT /api/v1/departments/{} - Updating department", id);
        DepartmentResponse updatedDepartment = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Byte id) {
        log.info("DELETE /api/v1/departments/{} - Soft deleting department", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}