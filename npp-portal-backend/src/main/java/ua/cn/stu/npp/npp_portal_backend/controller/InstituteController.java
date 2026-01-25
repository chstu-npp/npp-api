package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateInstituteRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateInstituteRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.InstituteResponse;
import ua.cn.stu.npp.npp_portal_backend.service.InstituteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/institutes")
@RequiredArgsConstructor
@Slf4j
public class InstituteController {

    private final InstituteService instituteService;

    @GetMapping
    public ResponseEntity<List<InstituteResponse>> getInstitutes(
            @RequestParam(required = false) Boolean active) {

        log.info("GET /api/v1/institutes - active: {}", active);
        List<InstituteResponse> institutes = instituteService.getAllInstitutes(active);
        return ResponseEntity.ok(institutes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituteResponse> getInstituteById(@PathVariable Byte id) {
        log.info("GET /api/v1/institutes/{}", id);
        InstituteResponse institute = instituteService.getInstituteById(id);
        return ResponseEntity.ok(institute);
    }

    @PostMapping
    public ResponseEntity<InstituteResponse> createInstitute(
            @Valid @RequestBody CreateInstituteRequest request) {

        log.info("POST /api/v1/institutes - Creating institute: {}", request.getName());
        InstituteResponse createdInstitute = instituteService.createInstitute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstitute);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstituteResponse> updateInstitute(
            @PathVariable Byte id,
            @Valid @RequestBody UpdateInstituteRequest request) {

        log.info("PUT /api/v1/institutes/{} - Updating institute", id);
        InstituteResponse updatedInstitute = instituteService.updateInstitute(id, request);
        return ResponseEntity.ok(updatedInstitute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitute(@PathVariable Byte id) {
        log.info("DELETE /api/v1/institutes/{} - Soft deleting institute", id);
        instituteService.deleteInstitute(id);
        return ResponseEntity.noContent().build();
    }
}
