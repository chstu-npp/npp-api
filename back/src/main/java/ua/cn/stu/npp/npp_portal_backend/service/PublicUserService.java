package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.response.PublicUserResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.User;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.UserMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<PublicUserResponse> searchTeacher(String query, Byte departmentId) {
        log.debug("Searching teachers with query: {}, departmentId: {}", query, departmentId);

        if (query == null || query.trim().length() < 3) {
            log.warn("Search query too short: {}", query);
            return List.of();
        }

        List<User> users = userRepository.searchByFullName(query.trim());

        List<User> activeUsers = users.stream()
                .filter(User::isActive)
                .collect(Collectors.toList());

        if (departmentId != null) {
            activeUsers = activeUsers.stream()
                    .filter(user -> user.getUserDepartments().stream()
                            .anyMatch(ud -> ud.getDepartment().getId().equals(departmentId)))
                    .collect(Collectors.toList());
        }

        log.debug("Found {} teachers", activeUsers.size());
        return userMapper.toPublicResponseList(activeUsers);
    }

    public PublicUserResponse getPublicTeacherProfile(Integer teacherId) {
        log.debug("Fetching public profile for teacher id: {}", teacherId);

        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> {
                    log.error("Teacher not found with id: {}", teacherId);
                    return new ResourceNotFoundException("Викладача не знайдено з ID: " + teacherId);
                });

        if (!user.isActive()) {
            log.error("Teacher with id {} is not active", teacherId);
            throw new ResourceNotFoundException("Викладача не знайдено з ID: " + teacherId);
        }

        return userMapper.toPublicResponse(user);
    }

    public List<PublicUserResponse> getDepartmentTeachers(Byte departmentId) {
        log.debug("Fetching teachers for department id: {}", departmentId);

        List<User> users = userRepository.findByDepartmentIdAndActive(departmentId, true, PageRequest.of(0, 1000))
                .getContent();

        log.debug("Found {} teachers in department", users.size());
        return userMapper.toPublicResponseList(users);
    }
}
