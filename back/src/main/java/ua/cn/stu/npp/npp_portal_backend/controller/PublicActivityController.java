package ua.cn.stu.npp.npp_portal_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.response.PublicActivityResponse;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;
import ua.cn.stu.npp.npp_portal_backend.service.PublicActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/teacher")
@RequiredArgsConstructor
@Slf4j
public class PublicActivityController {

    private final PublicActivityService publicActivityService;

    @GetMapping("/{teacherId}/activities")
    public ResponseEntity<List<PublicActivityResponse>> getPublicTeacherActivities(
            @PathVariable Integer teacherId,
            @RequestParam(required = false) WorksType worksType,
            @RequestParam(required = false) Short year,
            @RequestParam(required = false) Byte semester) {

        log.info("GET /api/v1/public/teacher/{}/activities - worksType: {}, year: {}, semester: {}",
                teacherId, worksType, year, semester);

        List<PublicActivityResponse> activities = publicActivityService.getPublicTeacherActivities(
                teacherId, worksType, year, semester);
        return ResponseEntity.ok(activities);
    }
}
