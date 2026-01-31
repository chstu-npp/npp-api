package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.Faculty;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Byte> {

    List<Faculty> findByActive(boolean active);

    List<Faculty> findByInstituteId(Byte instituteId);

    List<Faculty> findByInstituteIdAndActive(Byte instituteId, boolean active);

    Optional<Faculty> findByNameIgnoreCase(String name);

    @Query("SELECT f FROM Faculty f WHERE f.id = :id AND f.active = :active")
    Optional<Faculty> findByIdAndActive(@Param("id") Byte id, @Param("active") boolean active);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Byte id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndInstituteId(String name, Byte instituteId);

    boolean existsByNameIgnoreCaseAndInstituteIdAndIdNot(String name, Byte instituteId, Byte id);
}
