package ua.cn.stu.npp.npp_portal_backend.mapper;

import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateActivityDetailsRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.ActivityDetailsResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.ActivityDetails;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityDetailsMapper {

    public ActivityDetailsResponse toResponse(ActivityDetails activityDetails) {
        if (activityDetails == null) {
            return null;
        }

        return ActivityDetailsResponse.builder()
                .id(activityDetails.getId())
                .numInOrd(activityDetails.getNumInOrd())
                .numInRegulations(activityDetails.getNumInRegulations())
                .coefficient(activityDetails.getCoefficient())
                .normTime(activityDetails.getNormTime())
                .additionalDetails(activityDetails.getAdditionalDetails())
                .active(activityDetails.isActive())
                .build();
    }

    public List<ActivityDetailsResponse> toResponseList(List<ActivityDetails> activityDetailsList) {
        if (activityDetailsList == null) {
            return List.of();
        }

        return activityDetailsList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ActivityDetails toEntity(CreateActivityDetailsRequest request) {
        if (request == null) {
            return null;
        }

        return ActivityDetails.builder()
                .numInOrd(request.getNumInOrd())
                .numInRegulations(request.getNumInRegulations())
                .coefficient(request.getCoefficient())
                .normTime(request.getNormTime())
                .additionalDetails(request.getAdditionalDetails())
                .build();
    }

    public void updateEntityFromDto(CreateActivityDetailsRequest request, ActivityDetails activityDetails) {
        if (request == null || activityDetails == null) {
            return;
        }

        if (request.getNumInOrd() != null) {
            activityDetails.setNumInOrd(request.getNumInOrd());
        }

        if (request.getNumInRegulations() != null) {
            activityDetails.setNumInRegulations(request.getNumInRegulations());
        }

        if (request.getCoefficient() != null) {
            activityDetails.setCoefficient(request.getCoefficient());
        }

        if (request.getNormTime() != null) {
            activityDetails.setNormTime(request.getNormTime());
        }

        if (request.getAdditionalDetails() != null) {
            activityDetails.setAdditionalDetails(request.getAdditionalDetails());
        }
    }
}
