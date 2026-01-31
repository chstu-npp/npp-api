package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateInstituteRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateInstituteRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.InstituteResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Institute;
import ua.cn.stu.npp.npp_portal_backend.exception.DuplicateResourceException;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.InstituteMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.InstituteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InstituteService {

    private final InstituteRepository instituteRepository;
    private final InstituteMapper instituteMapper;

    public List<InstituteResponse> getAllInstitutes(Boolean active) {
        log.debug("Fetching all institutes with active filter: {}", active);

        List<Institute> institutes;
        if (active != null) {
            institutes = instituteRepository.findByActive(active);
        } else {
            institutes = instituteRepository.findAll();
        }

        log.debug("Found {} institutes", institutes.size());
        return instituteMapper.toResponseList(institutes);
    }

    public InstituteResponse getInstituteById(Byte id) {
        log.debug("Fetching institute by id: {}", id);

        Institute institute = instituteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Institute not found with id: {}", id);
                    return new ResourceNotFoundException("Інститут не знайдено з ID: " + id);
                });

        return instituteMapper.toResponse(institute);
    }

    @Transactional
    public InstituteResponse createInstitute(CreateInstituteRequest request) {
        log.debug("Creating new institute with name: {}", request.getName());

        if (instituteRepository.existsByNameIgnoreCase(request.getName())) {
            log.error("Institute with name '{}' already exists", request.getName());
            throw new DuplicateResourceException("Інститут з такою назвою вже існує: " + request.getName());
        }

        Institute institute = instituteMapper.toEntity(request);
        institute.setActive(true);

        Institute savedInstitute = instituteRepository.save(institute);

        log.info("Successfully created institute with id: {}", savedInstitute.getId());
        return instituteMapper.toResponse(savedInstitute);
    }

    @Transactional
    public InstituteResponse updateInstitute(Byte id, UpdateInstituteRequest request) {
        log.debug("Updating institute with id: {}", id);

        Institute institute = instituteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Institute not found with id: {}", id);
                    return new ResourceNotFoundException("Інститут не знайдено з ID: " + id);
                });

        if (request.getName() != null &&
                !request.getName().equalsIgnoreCase(institute.getName()) &&
                instituteRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            log.error("Institute with name '{}' already exists", request.getName());
            throw new DuplicateResourceException("Інститут з такою назвою вже існує: " + request.getName());
        }

        instituteMapper.updateEntityFromDto(request, institute);
        Institute updatedInstitute = instituteRepository.save(institute);

        log.info("Successfully updated institute with id: {}", id);
        return instituteMapper.toResponse(updatedInstitute);
    }

    @Transactional
    public void deleteInstitute(Byte id) {
        log.debug("Soft deleting institute with id: {}", id);

        Institute institute = instituteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Institute not found with id: {}", id);
                    return new ResourceNotFoundException("Інститут не знайдено з ID: " + id);
                });

        institute.setActive(false);
        instituteRepository.save(institute);

        log.info("Successfully soft deleted institute with id: {}", id);
    }

    public boolean existsById(Byte id) {
        return instituteRepository.existsById(id);
    }

    public Institute getActiveInstituteEntity(Byte id) {
        return instituteRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Активний інститут не знайдено з ID: " + id));
    }
}
