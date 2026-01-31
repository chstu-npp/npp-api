package ua.cn.stu.npp.npp_portal_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.response.FacultyResponse;
import ua.cn.stu.npp.npp_portal_backend.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/hierarchy")
@RequiredArgsConstructor
@Slf4j
public class PublicFacultyController {

    private final FacultyService facultyService;

    @GetMapping("/faculties")
    public ResponseEntity<List<FacultyResponse>> getPublicFaculties(
            @RequestParam(required = false) Byte instituteId) {

        log.info("GET /api/v1/public/hierarchy/faculties - instituteId: {}, Public access", instituteId);
        List<FacultyResponse> faculties = facultyService.getAllFaculties(instituteId, true);
        return ResponseEntity.ok(faculties);
    }
}
