package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateUserDetailsRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserDetailsResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.*;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.UserDetailsMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final DegreeRepository degreeRepository;
    private final PositionRepository positionRepository;
    private final EnglishLevelRepository englishLevelRepository;
    private final NasAffiliationRepository nasAffiliationRepository;
    private final HonorRepository honorRepository;
    private final UserDetailsMapper userDetailsMapper;

    public UserDetailsResponse getUserDetails(Integer userId) {
        log.debug("Fetching user details for user id: {}", userId);

        UserDetails userDetails = userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User details not found for user id: {}", userId);
                    return new ResourceNotFoundException("Деталі користувача не знайдено для ID: " + userId);
                });

        return userDetailsMapper.toResponse(userDetails);
    }

    @Transactional
    public UserDetailsResponse updateUserDetails(Integer userId, UpdateUserDetailsRequest request) {
        log.debug("Updating user details for user id: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Користувача не знайдено з ID: " + userId);
        }

        UserDetails userDetails = userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User details not found for user id: {}", userId);
                    return new ResourceNotFoundException("Деталі користувача не знайдено для ID: " + userId);
                });

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

        userDetails.setDegree(degree);
        userDetails.setPosition(position);
        userDetails.setEnglishLevel(englishLevel);
        userDetails.setNasAffiliation(nasAffiliation);
        userDetails.setHonors(honors);

        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);

        log.info("Successfully updated user details for user id: {}", userId);
        return userDetailsMapper.toResponse(savedUserDetails);
    }

    @Transactional
    public void updateUserPhoto(Integer userId, String photoUrl) {
        log.debug("Updating photo for user id: {}", userId);

        UserDetails userDetails = userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Деталі користувача не знайдено для ID: " + userId));

        userDetails.setPhoto(photoUrl);
        userDetailsRepository.save(userDetails);

        log.info("Successfully updated photo for user id: {}", userId);
    }

    @Transactional
    public void deleteUserPhoto(Integer userId) {
        log.debug("Deleting photo for user id: {}", userId);

        UserDetails userDetails = userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Деталі користувача не знайдено для ID: " + userId));

        userDetails.setPhoto(null);
        userDetailsRepository.save(userDetails);

        log.info("Successfully deleted photo for user id: {}", userId);
    }
}