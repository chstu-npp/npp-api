package ua.cn.stu.npp.npp_portal_backend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateFacultyRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateFacultyRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.FacultyResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Faculty;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FacultyMapper {

    private final InstituteMapper instituteMapper;

    public FacultyResponse toResponse(Faculty faculty) {
        if (faculty == null) {
            return null;
        }

        return FacultyResponse.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .facLogo(faculty.getFacLogo())
                .facHead(faculty.getFacHead())
                .facPage(faculty.getFacPage())
                .active(faculty.isActive())
                .institute(instituteMapper.toResponse(faculty.getInstitute()))
                .build();
    }

    public List<FacultyResponse> toResponseList(List<Faculty> faculties) {
        if (faculties == null) {
            return List.of();
        }

        return faculties.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Faculty toEntity(CreateFacultyRequest request) {
        if (request == null) {
            return null;
        }

        return Faculty.builder()
                .name(request.getName())
                .facLogo(request.getFacLogo())
                .facHead(request.getFacHead())
                .facPage(request.getFacPage())
                .build();
    }

    public void updateEntityFromDto(UpdateFacultyRequest request, Faculty faculty) {
        if (request == null || faculty == null) {
            return;
        }

        if (request.getName() != null) {
            faculty.setName(request.getName());
        }

        if (request.getFacLogo() != null) {
            faculty.setFacLogo(request.getFacLogo());
        }

        if (request.getFacHead() != null) {
            faculty.setFacHead(request.getFacHead());
        }

        if (request.getFacPage() != null) {
            faculty.setFacPage(request.getFacPage());
        }
    }
}
