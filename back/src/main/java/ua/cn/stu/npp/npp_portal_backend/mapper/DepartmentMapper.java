package ua.cn.stu.npp.npp_portal_backend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.DepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Department;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    private final FacultyMapper facultyMapper;

    public DepartmentResponse toResponse(Department department) {
        if (department == null) {
            return null;
        }

        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .depLogo(department.getDepLogo())
                .depHead(department.getDepHead())
                .depPage(department.getDepPage())
                .active(department.isActive())
                .faculty(facultyMapper.toResponse(department.getFaculty()))
                .build();
    }

    public List<DepartmentResponse> toResponseList(List<Department> departments) {
        if (departments == null) {
            return List.of();
        }

        return departments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Department toEntity(CreateDepartmentRequest request) {
        if (request == null) {
            return null;
        }

        return Department.builder()
                .name(request.getName())
                .depLogo(request.getDepLogo())
                .depHead(request.getDepHead())
                .depPage(request.getDepPage())
                .build();
    }

    public void updateEntityFromDto(UpdateDepartmentRequest request, Department department) {
        if (request == null || department == null) {
            return;
        }

        if (request.getName() != null) {
            department.setName(request.getName());
        }

        if (request.getDepLogo() != null) {
            department.setDepLogo(request.getDepLogo());
        }

        if (request.getDepHead() != null) {
            department.setDepHead(request.getDepHead());
        }

        if (request.getDepPage() != null) {
            department.setDepPage(request.getDepPage());
        }
    }
}
