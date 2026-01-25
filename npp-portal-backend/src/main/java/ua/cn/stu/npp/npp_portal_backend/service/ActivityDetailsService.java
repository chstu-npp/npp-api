package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateActivityDetailsRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.ActivityDetailsResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.ActivityDetails;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.ActivityDetailsMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.ActivityDetailsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ActivityDetailsService {

    private final ActivityDetailsRepository activityDetailsRepository;
    private final ActivityDetailsMapper activityDetailsMapper;

    public Page<ActivityDetailsResponse> getAllActivityDetails(Boolean active, int page, int size) {
        log.debug("Fetching activity details - active: {}", active);

        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityDetails> activityDetails;

        if (active != null) {
            List<ActivityDetails> list = activityDetailsRepository.findByActive(active);
            activityDetails = new org.springframework.data.domain.PageImpl<>(
                    list.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageable.getPageSize(), list.size())),
                    pageable,
                    list.size()
            );
        } else {
            activityDetails = activityDetailsRepository.findAll(pageable);
        }

        log.debug("Found {} activity details", activityDetails.getTotalElements());
        return activityDetails.map(activityDetailsMapper::toResponse);
    }

    public ActivityDetailsResponse getActivityDetailsById(Short id) {
        log.debug("Fetching activity details by id: {}", id);

        ActivityDetails activityDetails = activityDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity details not found with id: {}", id);
                    return new ResourceNotFoundException("Деталі роботи не знайдено з ID: " + id);
                });

        return activityDetailsMapper.toResponse(activityDetails);
    }

    @Transactional
    public ActivityDetailsResponse createActivityDetails(CreateActivityDetailsRequest request) {
        log.debug("Creating new activity details");

        ActivityDetails activityDetails = activityDetailsMapper.toEntity(request);
        activityDetails.setActive(true);

        ActivityDetails savedActivityDetails = activityDetailsRepository.save(activityDetails);

        log.info("Successfully created activity details with id: {}", savedActivityDetails.getId());
        return activityDetailsMapper.toResponse(savedActivityDetails);
    }

    @Transactional
    public ActivityDetailsResponse updateActivityDetails(Short id, CreateActivityDetailsRequest request) {
        log.debug("Updating activity details with id: {}", id);

        ActivityDetails activityDetails = activityDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity details not found with id: {}", id);
                    return new ResourceNotFoundException("Деталі роботи не знайдено з ID: " + id);
                });

        activityDetailsMapper.updateEntityFromDto(request, activityDetails);
        ActivityDetails updatedActivityDetails = activityDetailsRepository.save(activityDetails);

        log.info("Successfully updated activity details with id: {}", id);
        return activityDetailsMapper.toResponse(updatedActivityDetails);
    }

    @Transactional
    public void deleteActivityDetails(Short id) {
        log.debug("Soft deleting activity details with id: {}", id);

        ActivityDetails activityDetails = activityDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity details not found with id: {}", id);
                    return new ResourceNotFoundException("Деталі роботи не знайдено з ID: " + id);
                });

        activityDetails.setActive(false);
        activityDetailsRepository.save(activityDetails);

        log.info("Successfully soft deleted activity details with id: {}", id);
    }
}
