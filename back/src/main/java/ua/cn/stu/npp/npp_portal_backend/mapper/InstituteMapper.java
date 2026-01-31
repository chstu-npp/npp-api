package ua.cn.stu.npp.npp_portal_backend.mapper;

import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateInstituteRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateInstituteRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.InstituteResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Institute;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper для конвертації між Institute Entity та DTO
 * ТІЛЬКИ маппінг даних, БЕЗ бізнес-логіки!
 */
@Component
public class InstituteMapper {

    public InstituteResponse toResponse(Institute institute) {
        if (institute == null) {
            return null;
        }

        return InstituteResponse.builder()
                .id(institute.getId())
                .name(institute.getName())
                .instLogo(institute.getInstLogo())
                .instHead(institute.getInstHead())
                .instPage(institute.getInstPage())
                .active(institute.isActive())
                .build();
    }

    public List<InstituteResponse> toResponseList(List<Institute> institutes) {
        if (institutes == null) {
            return List.of();
        }

        return institutes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Institute toEntity(CreateInstituteRequest request) {
        if (request == null) {
            return null;
        }

        return Institute.builder()
                .name(request.getName())
                .instLogo(request.getInstLogo())
                .instHead(request.getInstHead())
                .instPage(request.getInstPage())
                .build();
    }

    public void updateEntityFromDto(UpdateInstituteRequest request, Institute institute) {
        if (request == null || institute == null) {
            return;
        }

        if (request.getName() != null) {
            institute.setName(request.getName());
        }

        if (request.getInstLogo() != null) {
            institute.setInstLogo(request.getInstLogo());
        }

        if (request.getInstHead() != null) {
            institute.setInstHead(request.getInstHead());
        }

        if (request.getInstPage() != null) {
            institute.setInstPage(request.getInstPage());
        }
    }
}