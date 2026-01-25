package ua.cn.stu.npp.npp_portal_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.response.InstituteResponse;
import ua.cn.stu.npp.npp_portal_backend.service.InstituteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/hierarchy")
@RequiredArgsConstructor
@Slf4j
public class PublicInstituteController {

    private final InstituteService instituteService;

    @GetMapping("/institutes")
    public ResponseEntity<List<InstituteResponse>> getPublicInstitutes() {
        log.info("GET /api/v1/public/hierarchy/institutes - Public access");
        List<InstituteResponse> institutes = instituteService.getAllInstitutes(true);
        return ResponseEntity.ok(institutes);
    }
}
