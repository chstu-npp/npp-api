package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateFacultyRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateFacultyRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.FacultyResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Faculty;
import ua.cn.stu.npp.npp_portal_backend.entity.Institute;
import ua.cn.stu.npp.npp_portal_backend.exception.DuplicateResourceException;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.FacultyMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.FacultyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final InstituteService instituteService;

    public List<FacultyResponse> getAllFaculties(Byte instituteId, Boolean active) {
        log.debug("Fetching faculties with instituteId: {}, active: {}", instituteId, active);

        List<Faculty> faculties;

        if (instituteId != null && active != null) {
            faculties = facultyRepository.findByInstituteIdAndActive(instituteId, active);
        } else if (instituteId != null) {
            faculties = facultyRepository.findByInstituteId(instituteId);
        } else if (active != null) {
            faculties = facultyRepository.findByActive(active);
        } else {
            faculties = facultyRepository.findAll();
        }

        log.debug("Found {} faculties", faculties.size());
        return facultyMapper.toResponseList(faculties);
    }

    public FacultyResponse getFacultyById(Byte id) {
        log.debug("Fetching faculty by id: {}", id);

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Faculty not found with id: {}", id);
                    return new ResourceNotFoundException("Факультет не знайдено з ID: " + id);
                });

        return facultyMapper.toResponse(faculty);
    }

    @Transactional
    public FacultyResponse createFaculty(CreateFacultyRequest request) {
        log.debug("Creating new faculty with name: {}", request.getName());

        Institute institute = instituteService.getActiveInstituteEntity(request.getInstituteId());

        if (facultyRepository.existsByNameIgnoreCaseAndInstituteId(request.getName(), request.getInstituteId())) {
            log.error("Faculty with name '{}' already exists in institute {}", request.getName(), request.getInstituteId());
            throw new DuplicateResourceException("Факультет з такою назвою вже існує в цьому інституті: " + request.getName());
        }

        Faculty faculty = facultyMapper.toEntity(request);
        faculty.setInstitute(institute);
        faculty.setActive(true);

        Faculty savedFaculty = facultyRepository.save(faculty);

        log.info("Successfully created faculty with id: {}", savedFaculty.getId());
        return facultyMapper.toResponse(savedFaculty);
    }

    @Transactional
    public FacultyResponse updateFaculty(Byte id, UpdateFacultyRequest request) {
        log.debug("Updating faculty with id: {}", id);

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Faculty not found with id: {}", id);
                    return new ResourceNotFoundException("Факультет не знайдено з ID: " + id);
                });

        if (request.getInstituteId() != null && !request.getInstituteId().equals(faculty.getInstitute().getId())) {
            Institute newInstitute = instituteService.getActiveInstituteEntity(request.getInstituteId());
            faculty.setInstitute(newInstitute);
        }

        if (request.getName() != null && !request.getName().equalsIgnoreCase(faculty.getName())) {
            Byte instituteIdToCheck = request.getInstituteId() != null ? request.getInstituteId() : faculty.getInstitute().getId();

            if (facultyRepository.existsByNameIgnoreCaseAndInstituteIdAndIdNot(request.getName(), instituteIdToCheck, id)) {
                log.error("Faculty with name '{}' already exists in institute {}", request.getName(), instituteIdToCheck);
                throw new DuplicateResourceException("Факультет з такою назвою вже існує в цьому інституті: " + request.getName());
            }
        }

        facultyMapper.updateEntityFromDto(request, faculty);
        Faculty updatedFaculty = facultyRepository.save(faculty);

        log.info("Successfully updated faculty with id: {}", id);
        return facultyMapper.toResponse(updatedFaculty);
    }

    @Transactional
    public void deleteFaculty(Byte id) {
        log.debug("Soft deleting faculty with id: {}", id);

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Faculty not found with id: {}", id);
                    return new ResourceNotFoundException("Факультет не знайдено з ID: " + id);
                });

        faculty.setActive(false);
        facultyRepository.save(faculty);

        log.info("Successfully soft deleted faculty with id: {}", id);
    }

    public boolean existsById(Byte id) {
        return facultyRepository.existsById(id);
    }

    public Faculty getActiveFacultyEntity(Byte id) {
        return facultyRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Активний факультет не знайдено з ID: " + id));
    }
}
