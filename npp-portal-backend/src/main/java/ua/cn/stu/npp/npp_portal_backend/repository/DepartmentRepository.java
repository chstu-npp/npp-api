package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Byte> {

    List<Department> findByActive(boolean active);

    List<Department> findByFacultyId(Byte facultyId);

    List<Department> findByFacultyIdAndActive(Byte facultyId, boolean active);

    Optional<Department> findByNameIgnoreCase(String name);

    @Query("SELECT d FROM Department d WHERE d.id = :id AND d.active = :active")
    Optional<Department> findByIdAndActive(@Param("id") Byte id, @Param("active") boolean active);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Byte id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndFacultyId(String name, Byte facultyId);

    boolean existsByNameIgnoreCaseAndFacultyIdAndIdNot(String name, Byte facultyId, Byte id);
}