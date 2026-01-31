package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateActivityRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateActivityRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.ActivityResponse;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;
import ua.cn.stu.npp.npp_portal_backend.service.ActivityService;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<Page<ActivityResponse>> getActivities(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) WorksType worksType,
            @RequestParam(required = false) Short year,
            @RequestParam(required = false) Byte semester,
            @RequestParam(required = false) Short yearFrom,
            @RequestParam(required = false) Short yearTo,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /api/v1/activities - userId: {}, worksType: {}, year: {}, semester: {}, active: {}",
                userId, worksType, year, semester, active);

        Page<ActivityResponse> activities = activityService.getAllActivities(
                userId, worksType, year, semester, yearFrom, yearTo, active, page, size);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable Integer id) {
        log.info("GET /api/v1/activities/{}", id);
        ActivityResponse activity = activityService.getActivityById(id);
        return ResponseEntity.ok(activity);
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(
            @RequestParam Integer currentUserId,
            @Valid @RequestBody CreateActivityRequest request) {

        log.info("POST /api/v1/activities - Creating activity by user: {}", currentUserId);
        ActivityResponse createdActivity = activityService.createActivity(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(
            @PathVariable Integer id,
            @RequestParam Integer currentUserId,
            @Valid @RequestBody UpdateActivityRequest request) {

        log.info("PUT /api/v1/activities/{} - Updating activity by user: {}", id, currentUserId);
        ActivityResponse updatedActivity = activityService.updateActivity(id, currentUserId, request);
        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Integer id,
            @RequestParam Integer currentUserId) {

        log.info("DELETE /api/v1/activities/{} - Deleting activity by user: {}", id, currentUserId);
        activityService.deleteActivity(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}
