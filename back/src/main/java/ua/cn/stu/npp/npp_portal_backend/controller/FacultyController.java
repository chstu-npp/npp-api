package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateFacultyRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateFacultyRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.FacultyResponse;
import ua.cn.stu.npp.npp_portal_backend.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faculties")
@RequiredArgsConstructor
@Slf4j
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping
    public ResponseEntity<List<FacultyResponse>> getFaculties(
            @RequestParam(required = false) Byte instituteId,
            @RequestParam(required = false) Boolean active) {

        log.info("GET /api/v1/faculties - instituteId: {}, active: {}", instituteId, active);
        List<FacultyResponse> faculties = facultyService.getAllFaculties(instituteId, active);
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponse> getFacultyById(@PathVariable Byte id) {
        log.info("GET /api/v1/faculties/{}", id);
        FacultyResponse faculty = facultyService.getFacultyById(id);
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public ResponseEntity<FacultyResponse> createFaculty(
            @Valid @RequestBody CreateFacultyRequest request) {

        log.info("POST /api/v1/faculties - Creating faculty: {}", request.getName());
        FacultyResponse createdFaculty = facultyService.createFaculty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaculty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponse> updateFaculty(
            @PathVariable Byte id,
            @Valid @RequestBody UpdateFacultyRequest request) {

        log.info("PUT /api/v1/faculties/{} - Updating faculty", id);
        FacultyResponse updatedFaculty = facultyService.updateFaculty(id, request);
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Byte id) {
        log.info("DELETE /api/v1/faculties/{} - Soft deleting faculty", id);
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
