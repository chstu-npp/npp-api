package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.AssignDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateDepartmentAssignmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserDepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Department;
import ua.cn.stu.npp.npp_portal_backend.entity.User;
import ua.cn.stu.npp.npp_portal_backend.entity.UserDepartment;
import ua.cn.stu.npp.npp_portal_backend.entity.UserDepartmentId;
import ua.cn.stu.npp.npp_portal_backend.exception.DuplicateResourceException;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.UserDepartmentMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.UserDepartmentRepository;
import ua.cn.stu.npp.npp_portal_backend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserDepartmentService {

    private final UserDepartmentRepository userDepartmentRepository;
    private final UserRepository userRepository;
    private final DepartmentService departmentService;
    private final UserDepartmentMapper userDepartmentMapper;

    public List<UserDepartmentResponse> getUserDepartments(Integer userId) {
        log.debug("Fetching departments for user id: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Користувача не знайдено з ID: " + userId);
        }

        List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(userId);

        log.debug("Found {} departments for user", userDepartments.size());
        return userDepartmentMapper.toResponseList(userDepartments.stream().collect(java.util.stream.Collectors.toSet()));
    }

    @Transactional
    public void assignDepartment(Integer userId, AssignDepartmentRequest request) {
        log.debug("Assigning department {} to user {}", request.getDepartmentId(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено з ID: " + userId));

        Department department = departmentService.getActiveDepartmentEntity(request.getDepartmentId());

        if (userDepartmentRepository.existsByUserIdAndDepartmentId(userId, request.getDepartmentId())) {
            log.error("User {} is already assigned to department {}", userId, request.getDepartmentId());
            throw new DuplicateResourceException("Користувач вже призначений на цю кафедру");
        }

        UserDepartmentId id = new UserDepartmentId(userId, request.getDepartmentId());
        UserDepartment userDepartment = UserDepartment.builder()
                .id(id)
                .user(user)
                .department(department)
                .joint(request.getJoint())
                .workload(request.getWorkload())
                .build();

        userDepartmentRepository.save(userDepartment);

        log.info("Successfully assigned user {} to department {}", userId, request.getDepartmentId());
    }

    @Transactional
    public void updateUserDepartment(Integer userId, Byte departmentId, UpdateDepartmentAssignmentRequest request) {
        log.debug("Updating department assignment for user {} and department {}", userId, departmentId);

        UserDepartmentId id = new UserDepartmentId(userId, departmentId);
        UserDepartment userDepartment = userDepartmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User department assignment not found for user {} and department {}", userId, departmentId);
                    return new ResourceNotFoundException("Призначення не знайдено");
                });

        if (request.getJoint() != null) {
            userDepartment.setJoint(request.getJoint());
        }

        if (request.getWorkload() != null) {
            userDepartment.setWorkload(request.getWorkload());
        }

        userDepartmentRepository.save(userDepartment);

        log.info("Successfully updated department assignment for user {} and department {}", userId, departmentId);
    }

    @Transactional
    public void removeDepartment(Integer userId, Byte departmentId) {
        log.debug("Removing department {} from user {}", departmentId, userId);

        UserDepartmentId id = new UserDepartmentId(userId, departmentId);

        if (!userDepartmentRepository.existsById(id)) {
            log.error("User department assignment not found for user {} and department {}", userId, departmentId);
            throw new ResourceNotFoundException("Призначення не знайдено");
        }

        userDepartmentRepository.deleteById(id);

        log.info("Successfully removed user {} from department {}", userId, departmentId);
    }
}
