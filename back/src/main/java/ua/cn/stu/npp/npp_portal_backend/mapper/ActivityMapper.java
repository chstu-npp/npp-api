package ua.cn.stu.npp.npp_portal_backend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateActivityRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateActivityRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.ActivityResponse;
import ua.cn.stu.npp.npp_portal_backend.dto.response.PublicActivityResponse;
import ua.cn.stu.npp.npp_portal_backend.dto.response.PublicActivityUserResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Activity;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActivityMapper {

    private final ActivityDetailsMapper activityDetailsMapper;
    private final UserMapper userMapper;

    public ActivityResponse toResponse(Activity activity) {
        if (activity == null) {
            return null;
        }

        return ActivityResponse.builder()
                .id(activity.getId())
                .description(activity.getDescription())
                .year(activity.getYear())
                .semester(activity.getSemester())
                .worksType(activity.getWorksType())
                .active(activity.isActive())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .activityDetails(activityDetailsMapper.toResponse(activity.getActivityDetails()))
                .users(activity.getUserActivities().stream()
                        .map(ua -> userMapper.toResponse(ua.getUser()))
                        .collect(Collectors.toList()))
                .build();
    }

    public List<ActivityResponse> toResponseList(List<Activity> activities) {
        if (activities == null) {
            return List.of();
        }

        return activities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PublicActivityResponse toPublicResponse(Activity activity) {
        if (activity == null) {
            return null;
        }

        return PublicActivityResponse.builder()
                .id(activity.getId())
                .description(activity.getDescription())
                .year(activity.getYear())
                .semester(activity.getSemester())
                .worksType(activity.getWorksType())
                .createdAt(activity.getCreatedAt())
                .users(activity.getUserActivities().stream()
                        .map(ua -> PublicActivityUserResponse.builder()
                                .id(ua.getUser().getId())
                                .firstName(ua.getUser().getFirstName())
                                .lastName(ua.getUser().getLastName())
                                .patronymic(ua.getUser().getPatronymic())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public List<PublicActivityResponse> toPublicResponseList(List<Activity> activities) {
        if (activities == null) {
            return List.of();
        }

        return activities.stream()
                .map(this::toPublicResponse)
                .collect(Collectors.toList());
    }

    public Activity toEntity(CreateActivityRequest request) {
        if (request == null) {
            return null;
        }

        return Activity.builder()
                .description(request.getDescription())
                .year(request.getYear())
                .semester(request.getSemester())
                .worksType(request.getWorksType())
                .build();
    }

    public void updateEntityFromDto(UpdateActivityRequest request, Activity activity) {
        if (request == null || activity == null) {
            return;
        }

        if (request.getDescription() != null) {
            activity.setDescription(request.getDescription());
        }

        if (request.getYear() != null) {
            activity.setYear(request.getYear());
        }

        if (request.getSemester() != null) {
            activity.setSemester(request.getSemester());
        }
    }
}