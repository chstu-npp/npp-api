package ua.cn.stu.npp.npp_portal_backend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateUserRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateUserRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.PublicUserResponse;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserDetailsMapper userDetailsMapper;
    private final UserDepartmentMapper userDepartmentMapper;

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .patronymic(user.getPatronymic())
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .details(userDetailsMapper.toResponse(user.getUserDetails()))
                .departments(userDepartmentMapper.toResponseList(user.getUserDepartments()))
                .build();
    }

    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PublicUserResponse toPublicResponse(User user) {
        if (user == null) {
            return null;
        }

        String photo = user.getUserDetails() != null ? user.getUserDetails().getPhoto() : null;

        return PublicUserResponse.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .patronymic(user.getPatronymic())
                .email(user.getEmail())
                .photo(photo)
                .departments(userDepartmentMapper.toPublicResponseList(user.getUserDepartments()))
                .build();
    }

    public List<PublicUserResponse> toPublicResponseList(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::toPublicResponse)
                .collect(Collectors.toList());
    }

    public User toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .role(request.getRole())
                .build();
    }

    public void updateEntityFromDto(UpdateUserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPatronymic() != null) {
            user.setPatronymic(request.getPatronymic());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
    }
}
