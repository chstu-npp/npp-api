package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.response.PublicActivityResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Activity;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;
import ua.cn.stu.npp.npp_portal_backend.mapper.ActivityMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.ActivityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public List<PublicActivityResponse> getPublicTeacherActivities(
            Integer teacherId,
            WorksType worksType,
            Short year,
            Byte semester) {

        log.debug("Fetching public activities for teacher: {}, worksType: {}, year: {}, semester: {}",
                teacherId, worksType, year, semester);

        List<Activity> activities;

        if (year != null) {
            activities = activityRepository.findByUserIdAndYear(teacherId, year, PageRequest.of(0, 1000)).getContent();
        } else if (worksType != null) {
            activities = activityRepository.findByUserIdAndWorksType(teacherId, worksType, PageRequest.of(0, 1000)).getContent();
        } else {
            activities = activityRepository.findByUserIdAndActive(teacherId, true, PageRequest.of(0, 1000)).getContent();
        }

        if (semester != null && semester > 0) {
            activities = activities.stream()
                    .filter(a -> a.getSemester().equals(semester))
                    .toList();
        }

        activities = activities.stream()
                .filter(Activity::isActive)
                .toList();

        log.debug("Found {} public activities", activities.size());
        return activityMapper.toPublicResponseList(activities);
    }
}