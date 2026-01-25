package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.request.UpdateDepartmentRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.DepartmentResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.Department;
import ua.cn.stu.npp.npp_portal_backend.entity.Faculty;
import ua.cn.stu.npp.npp_portal_backend.exception.DuplicateResourceException;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.DepartmentMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.DepartmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final FacultyService facultyService;

    public List<DepartmentResponse> getAllDepartments(Byte facultyId, Boolean active) {
        log.debug("Fetching departments with facultyId: {}, active: {}", facultyId, active);

        List<Department> departments;

        if (facultyId != null && active != null) {
            departments = departmentRepository.findByFacultyIdAndActive(facultyId, active);
        } else if (facultyId != null) {
            departments = departmentRepository.findByFacultyId(facultyId);
        } else if (active != null) {
            departments = departmentRepository.findByActive(active);
        } else {
            departments = departmentRepository.findAll();
        }

        log.debug("Found {} departments", departments.size());
        return departmentMapper.toResponseList(departments);
    }

    public DepartmentResponse getDepartmentById(Byte id) {
        log.debug("Fetching department by id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department not found with id: {}", id);
                    return new ResourceNotFoundException("Кафедру не знайдено з ID: " + id);
                });

        return departmentMapper.toResponse(department);
    }

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        log.debug("Creating new department with name: {}", request.getName());

        Faculty faculty = facultyService.getActiveFacultyEntity(request.getFacultyId());

        if (departmentRepository.existsByNameIgnoreCaseAndFacultyId(request.getName(), request.getFacultyId())) {
            log.error("Department with name '{}' already exists in faculty {}", request.getName(), request.getFacultyId());
            throw new DuplicateResourceException("Кафедра з такою назвою вже існує на цьому факультеті: " + request.getName());
        }

        Department department = departmentMapper.toEntity(request);
        department.setFaculty(faculty);
        department.setActive(true);

        Department savedDepartment = departmentRepository.save(department);

        log.info("Successfully created department with id: {}", savedDepartment.getId());
        return departmentMapper.toResponse(savedDepartment);
    }

    @Transactional
    public DepartmentResponse updateDepartment(Byte id, UpdateDepartmentRequest request) {
        log.debug("Updating department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department not found with id: {}", id);
                    return new ResourceNotFoundException("Кафедру не знайдено з ID: " + id);
                });

        if (request.getFacultyId() != null && !request.getFacultyId().equals(department.getFaculty().getId())) {
            Faculty newFaculty = facultyService.getActiveFacultyEntity(request.getFacultyId());
            department.setFaculty(newFaculty);
        }

        if (request.getName() != null && !request.getName().equalsIgnoreCase(department.getName())) {
            Byte facultyIdToCheck = request.getFacultyId() != null ? request.getFacultyId() : department.getFaculty().getId();

            if (departmentRepository.existsByNameIgnoreCaseAndFacultyIdAndIdNot(request.getName(), facultyIdToCheck, id)) {
                log.error("Department with name '{}' already exists in faculty {}", request.getName(), facultyIdToCheck);
                throw new DuplicateResourceException("Кафедра з такою назвою вже існує на цьому факультеті: " + request.getName());
            }
        }

        departmentMapper.updateEntityFromDto(request, department);
        Department updatedDepartment = departmentRepository.save(department);

        log.info("Successfully updated department with id: {}", id);
        return departmentMapper.toResponse(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Byte id) {
        log.debug("Soft deleting department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department not found with id: {}", id);
                    return new ResourceNotFoundException("Кафедру не знайдено з ID: " + id);
                });

        department.setActive(false);
        departmentRepository.save(department);

        log.info("Successfully soft deleted department with id: {}", id);
    }

    public boolean existsById(Byte id) {
        return departmentRepository.existsById(id);
    }

    public Department getActiveDepartmentEntity(Byte id) {
        return departmentRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Активна кафедра не знайдена з ID: " + id));
    }
}