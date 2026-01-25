package ua.cn.stu.npp.npp_portal_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.response.DepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/hierarchy")
@RequiredArgsConstructor
@Slf4j
public class PublicDepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getPublicDepartments(
            @RequestParam(required = false) Byte facultyId) {

        log.info("GET /api/v1/public/hierarchy/departments - facultyId: {}, Public access", facultyId);
        List<DepartmentResponse> departments = departmentService.getAllDepartments(facultyId, true);
        return ResponseEntity.ok(departments);
    }
}
