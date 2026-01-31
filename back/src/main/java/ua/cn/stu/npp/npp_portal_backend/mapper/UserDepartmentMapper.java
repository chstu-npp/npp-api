package ua.cn.stu.npp.npp_portal_backend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.response.PublicUserDepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserDepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.UserDepartment;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDepartmentMapper {

    private final DepartmentMapper departmentMapper;

    public UserDepartmentResponse toResponse(UserDepartment userDepartment) {
        if (userDepartment == null) {
            return null;
        }

        return UserDepartmentResponse.builder()
                .department(departmentMapper.toResponse(userDepartment.getDepartment()))
                .joint(userDepartment.getJoint())
                .workload(userDepartment.getWorkload())
                .build();
    }

    public List<UserDepartmentResponse> toResponseList(Set<UserDepartment> userDepartments) {
        if (userDepartments == null) {
            return List.of();
        }

        return userDepartments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PublicUserDepartmentResponse toPublicResponse(UserDepartment userDepartment) {
        if (userDepartment == null) {
            return null;
        }

        return PublicUserDepartmentResponse.builder()
                .department(departmentMapper.toResponse(userDepartment.getDepartment()))
                .build();
    }

    public List<PublicUserDepartmentResponse> toPublicResponseList(Set<UserDepartment> userDepartments) {
        if (userDepartments == null) {
            return List.of();
        }

        return userDepartments.stream()
                .map(this::toPublicResponse)
                .collect(Collectors.toList());
    }
}
