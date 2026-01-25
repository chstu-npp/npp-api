package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnglishLevelRepository extends JpaRepository<EnglishLevel, Byte> {
    List<EnglishLevel> findByActive(boolean active);
    Optional<EnglishLevel> findByLevelIgnoreCase(String level);
    boolean existsByLevelIgnoreCaseAndIdNot(String level, Byte id);
    boolean existsByLevelIgnoreCase(String level);
}
