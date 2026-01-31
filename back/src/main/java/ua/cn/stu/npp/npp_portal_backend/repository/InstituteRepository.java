package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.Institute;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Byte> {

    /**
     * Знайти всі активні інститути
     */
    List<Institute> findByActive(boolean active);

    /**
     * Знайти інститут за назвою (регістронезалежний пошук)
     */
    Optional<Institute> findByNameIgnoreCase(String name);

    /**
     * Знайти інститут за ID тільки якщо він активний
     */
    @Query("SELECT i FROM Institute i WHERE i.id = :id AND i.active = :active")
    Optional<Institute> findByIdAndActive(@Param("id") Byte id, @Param("active") boolean active);

    /**
     * Перевірка чи існує інститут з такою назвою (для валідації при створенні/оновленні)
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Byte id);

    /**
     * Перевірка чи існує інститут з такою назвою
     */
    boolean existsByNameIgnoreCase(String name);
}
