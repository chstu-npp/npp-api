package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.*;
import ua.cn.stu.npp.npp_portal_backend.dto.response.*;
import ua.cn.stu.npp.npp_portal_backend.entity.*;
import ua.cn.stu.npp.npp_portal_backend.enums.UserRole;
import ua.cn.stu.npp.npp_portal_backend.exception.DuplicateResourceException;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.UserMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final DegreeRepository degreeRepository;
    private final PositionRepository positionRepository;
    private final EnglishLevelRepository englishLevelRepository;
    private final NasAffiliationRepository nasAffiliationRepository;
    private final HonorRepository honorRepository;
    private final UserMapper userMapper;

    public Page<UserResponse> getAllUsers(UserRole role, Boolean active, Byte departmentId, int page, int size) {
        log.debug("Fetching users - role: {}, active: {}, departmentId: {}", role, active, departmentId);

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;

        if (departmentId != null && active != null) {
            users = userRepository.findByDepartmentIdAndActive(departmentId, active, pageable);
        } else if (departmentId != null) {
            users = userRepository.findByDepartmentId(departmentId, pageable);
        } else if (role != null && active != null) {
            users = userRepository.findByRoleAndActive(role, active, pageable);
        } else if (role != null) {
            users = userRepository.findByRole(role, pageable);
        } else if (active != null) {
            users = userRepository.findByActive(active, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        log.debug("Found {} users", users.getTotalElements());
        return users.map(userMapper::toResponse);
    }

    public UserResponse getUserById(Integer id) {
        log.debug("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("Користувача не знайдено з ID: " + id);
                });

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.debug("Creating new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("User with email '{}' already exists", request.getEmail());
            throw new DuplicateResourceException("Користувач з таким email вже існує: " + request.getEmail());
        }

        String login = extractLoginFromEmail(request.getEmail());

        if (userRepository.existsByLogin(login)) {
            log.error("User with login '{}' already exists", login);
            throw new DuplicateResourceException("Користувач з таким login вже існує: " + login);
        }

        Degree degree = degreeRepository.findById(request.getDegreeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ступінь не знайдено з ID: " + request.getDegreeId()));
        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Посаду не знайдено з ID: " + request.getPositionId()));
        EnglishLevel englishLevel = englishLevelRepository.findById(request.getEnglishLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Рівень англійської не знайдено з ID: " + request.getEnglishLevelId()));
        NasAffiliation nasAffiliation = nasAffiliationRepository.findById(request.getNasAffiliationId())
                .orElseThrow(() -> new ResourceNotFoundException("Приналежність до НАН не знайдено з ID: " + request.getNasAffiliationId()));
        Honor honors = honorRepository.findById(request.getHonorsId())
                .orElseThrow(() -> new ResourceNotFoundException("Звання не знайдено з ID: " + request.getHonorsId()));

        User user = userMapper.toEntity(request);
        user.setLogin(login);
        user.setPassword("TEMPORARY_PASSWORD_WILL_BE_SET_VIA_EMAIL");
        user.setActive(true);

        User savedUser = userRepository.save(user);

        UserDetails userDetails = UserDetails.builder()
                .user(savedUser)
                .degree(degree)
                .position(position)
                .englishLevel(englishLevel)
                .nasAffiliation(nasAffiliation)
                .honors(honors)
                .active(true)
                .build();

        userDetailsRepository.save(userDetails);

        log.info("Successfully created user with id: {}. Login: {}, UUID: {}",
                savedUser.getId(), savedUser.getLogin(), savedUser.getUuid());

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Integer id, UpdateUserRequest request) {
        log.debug("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("Користувача не знайдено з ID: " + id);
                });

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                log.error("User with email '{}' already exists", request.getEmail());
                throw new DuplicateResourceException("Користувач з таким email вже існує: " + request.getEmail());
            }

            String newLogin = extractLoginFromEmail(request.getEmail());
            if (userRepository.existsByLoginAndIdNot(newLogin, id)) {
                log.error("User with login '{}' already exists", newLogin);
                throw new DuplicateResourceException("Користувач з таким login вже існує: " + newLogin);
            }
            user.setLogin(newLogin);
        }

        userMapper.updateEntityFromDto(request, user);
        User updatedUser = userRepository.save(user);

        log.info("Successfully updated user with id: {}", id);
        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Integer id) {
        log.debug("Soft deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("Користувача не знайдено з ID: " + id);
                });

        user.setActive(false);

        if (user.getUserDetails() != null) {
            user.getUserDetails().setActive(false);
        }

        userRepository.save(user);

        log.info("Successfully soft deleted user with id: {}", id);
    }

    private String extractLoginFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Невірний формат email");
        }
        return email.substring(0, email.indexOf("@"));
    }
}