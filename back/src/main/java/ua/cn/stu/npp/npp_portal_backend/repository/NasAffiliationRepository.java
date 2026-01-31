package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface NasAffiliationRepository extends JpaRepository<NasAffiliation, Byte> {
    List<NasAffiliation> findByActive(boolean active);
    Optional<NasAffiliation> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Byte id);
    boolean existsByNameIgnoreCase(String name);
}
