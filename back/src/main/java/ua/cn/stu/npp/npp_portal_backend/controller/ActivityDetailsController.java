package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateActivityDetailsRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.ActivityDetailsResponse;
import ua.cn.stu.npp.npp_portal_backend.service.ActivityDetailsService;

@RestController
@RequestMapping("/api/v1/activity-details")
@RequiredArgsConstructor
@Slf4j
public class ActivityDetailsController {

    private final ActivityDetailsService activityDetailsService;

    @GetMapping
    public ResponseEntity<Page<ActivityDetailsResponse>> getActivityDetails(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        log.info("GET /api/v1/activity-details - active: {}", active);
        Page<ActivityDetailsResponse> activityDetails = activityDetailsService.getAllActivityDetails(active, page, size);
        return ResponseEntity.ok(activityDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDetailsResponse> getActivityDetailsById(@PathVariable Short id) {
        log.info("GET /api/v1/activity-details/{}", id);
        ActivityDetailsResponse activityDetails = activityDetailsService.getActivityDetailsById(id);
        return ResponseEntity.ok(activityDetails);
    }

    @PostMapping
    public ResponseEntity<ActivityDetailsResponse> createActivityDetails(
            @Valid @RequestBody CreateActivityDetailsRequest request) {

        log.info("POST /api/v1/activity-details - Creating activity details");
        ActivityDetailsResponse createdActivityDetails = activityDetailsService.createActivityDetails(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActivityDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDetailsResponse> updateActivityDetails(
            @PathVariable Short id,
            @Valid @RequestBody CreateActivityDetailsRequest request) {

        log.info("PUT /api/v1/activity-details/{} - Updating activity details", id);
        ActivityDetailsResponse updatedActivityDetails = activityDetailsService.updateActivityDetails(id, request);
        return ResponseEntity.ok(updatedActivityDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityDetails(@PathVariable Short id) {
        log.info("DELETE /api/v1/activity-details/{} - Soft deleting activity details", id);
        activityDetailsService.deleteActivityDetails(id);
        return ResponseEntity.noContent().build();
    }
}
