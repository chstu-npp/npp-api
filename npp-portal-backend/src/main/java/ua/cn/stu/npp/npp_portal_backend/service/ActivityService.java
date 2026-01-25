package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateActivityRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateActivityRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.ActivityResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.*;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.ActivityMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityDetailsRepository activityDetailsRepository;
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;
    private final ActivityMapper activityMapper;

    public Page<ActivityResponse> getAllActivities(
            Integer userId,
            WorksType worksType,
            Short year,
            Byte semester,
            Short yearFrom,
            Short yearTo,
            Boolean active,
            int page,
            int size) {

        log.debug("Fetching activities - userId: {}, worksType: {}, year: {}, semester: {}, active: {}",
                userId, worksType, year, semester, active);

        Pageable pageable = PageRequest.of(page, size);
        Page<Activity> activities;

        if (userId != null) {
            if (yearFrom != null && yearTo != null) {
                activities = activityRepository.findByUserIdAndYearBetween(userId, yearFrom, yearTo, pageable);
            } else if (year != null) {
                activities = activityRepository.findByUserIdAndYear(userId, year, pageable);
            } else if (worksType != null) {
                activities = activityRepository.findByUserIdAndWorksType(userId, worksType, pageable);
            } else if (active != null) {
                activities = activityRepository.findByUserIdAndActive(userId, active, pageable);
            } else {
                activities = activityRepository.findByUserId(userId, pageable);
            }
        } else {
            if (year != null && semester != null && active != null) {
                activities = activityRepository.findByYearAndSemesterAndActive(year, semester, active, pageable);
            } else if (year != null && semester != null) {
                activities = activityRepository.findByYearAndSemester(year, semester, pageable);
            } else if (year != null && active != null) {
                activities = activityRepository.findByYearAndActive(year, active, pageable);
            } else if (year != null) {
                activities = activityRepository.findByYear(year, pageable);
            } else if (worksType != null && active != null) {
                activities = activityRepository.findByWorksTypeAndActive(worksType, active, pageable);
            } else if (worksType != null) {
                activities = activityRepository.findByWorksType(worksType, pageable);
            } else if (active != null) {
                activities = activityRepository.findByActive(active, pageable);
            } else {
                activities = activityRepository.findAll(pageable);
            }
        }

        log.debug("Found {} activities", activities.getTotalElements());
        return activities.map(activityMapper::toResponse);
    }

    public ActivityResponse getActivityById(Integer id) {
        log.debug("Fetching activity by id: {}", id);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity not found with id: {}", id);
                    return new ResourceNotFoundException("Роботу не знайдено з ID: " + id);
                });

        return activityMapper.toResponse(activity);
    }

    @Transactional
    public ActivityResponse createActivity(Integer currentUserId, CreateActivityRequest request) {
        log.debug("Creating new activity by user: {}", currentUserId);

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено з ID: " + currentUserId));

        ActivityDetails activityDetails = activityDetailsRepository.findById(request.getActivityDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException("Деталі роботи не знайдено з ID: " + request.getActivityDetailsId()));

        Activity activity = activityMapper.toEntity(request);
        activity.setActivityDetails(activityDetails);
        activity.setActive(true);

        Activity savedActivity = activityRepository.save(activity);

        UserActivity userActivity = new UserActivity(currentUser, savedActivity);
        userActivityRepository.save(userActivity);

        if (request.getCoAuthorIds() != null && !request.getCoAuthorIds().isEmpty()) {
            for (Integer coAuthorId : request.getCoAuthorIds()) {
                if (!coAuthorId.equals(currentUserId)) {
                    User coAuthor = userRepository.findById(coAuthorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Співавтора не знайдено з ID: " + coAuthorId));

                    UserActivity coAuthorActivity = new UserActivity(coAuthor, savedActivity);
                    userActivityRepository.save(coAuthorActivity);
                }
            }
        }

        log.info("Successfully created activity with id: {}", savedActivity.getId());
        return activityMapper.toResponse(savedActivity);
    }

    @Transactional
    public ActivityResponse updateActivity(Integer id, Integer currentUserId, UpdateActivityRequest request) {
        log.debug("Updating activity with id: {} by user: {}", id, currentUserId);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity not found with id: {}", id);
                    return new ResourceNotFoundException("Роботу не знайдено з ID: " + id);
                });

        boolean isAuthor = activity.getUserActivities().stream()
                .anyMatch(ua -> ua.getUser().getId().equals(currentUserId));

        if (!isAuthor) {
            log.error("User {} is not an author of activity {}", currentUserId, id);
            throw new IllegalStateException("Тільки автор роботи може її редагувати");
        }

        if (request.getActivityDetailsId() != null) {
            ActivityDetails newActivityDetails = activityDetailsRepository.findById(request.getActivityDetailsId())
                    .orElseThrow(() -> new ResourceNotFoundException("Деталі роботи не знайдено з ID: " + request.getActivityDetailsId()));
            activity.setActivityDetails(newActivityDetails);
        }

        if (request.getCoAuthorIds() != null) {
            Set<UserActivity> currentUserActivities = new HashSet<>(activity.getUserActivities());

            UserActivity mainAuthor = currentUserActivities.stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Не знайдено основного автора"));

            currentUserActivities.stream()
                    .filter(ua -> !ua.getUser().getId().equals(mainAuthor.getUser().getId()))
                    .forEach(ua -> userActivityRepository.delete(ua));

            for (Integer coAuthorId : request.getCoAuthorIds()) {
                if (!coAuthorId.equals(mainAuthor.getUser().getId())) {
                    User coAuthor = userRepository.findById(coAuthorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Співавтора не знайдено з ID: " + coAuthorId));

                    if (!userActivityRepository.existsByUserIdAndActivityId(coAuthorId, id)) {
                        UserActivity coAuthorActivity = new UserActivity(coAuthor, activity);
                        userActivityRepository.save(coAuthorActivity);
                    }
                }
            }
        }

        activityMapper.updateEntityFromDto(request, activity);
        Activity updatedActivity = activityRepository.save(activity);

        log.info("Successfully updated activity with id: {}", id);
        return activityMapper.toResponse(updatedActivity);
    }

    @Transactional
    public void deleteActivity(Integer id, Integer currentUserId) {
        log.debug("Soft deleting activity with id: {} by user: {}", id, currentUserId);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity not found with id: {}", id);
                    return new ResourceNotFoundException("Роботу не знайдено з ID: " + id);
                });

        UserActivity firstAuthor = activity.getUserActivities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Не знайдено автора роботи"));

        if (!firstAuthor.getUser().getId().equals(currentUserId)) {
            log.error("User {} is not the owner of activity {}", currentUserId, id);
            throw new IllegalStateException("Тільки власник роботи (перший автор) може її видалити");
        }

        activity.setActive(false);
        activityRepository.save(activity);

        log.info("Successfully soft deleted activity with id: {}", id);
    }
}
