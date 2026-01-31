package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.User;
import ua.cn.stu.npp.npp_portal_backend.enums.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    Page<User> findByActive(boolean active, Pageable pageable);

    Page<User> findByRole(UserRole role, Pageable pageable);

    Page<User> findByRoleAndActive(UserRole role, boolean active, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.userDepartments ud WHERE ud.department.id = :departmentId AND u.active = :active")
    Page<User> findByDepartmentIdAndActive(@Param("departmentId") Byte departmentId, @Param("active") boolean active, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.userDepartments ud WHERE ud.department.id = :departmentId")
    Page<User> findByDepartmentId(@Param("departmentId") Byte departmentId, Pageable pageable);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsByLoginAndIdNot(String login, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);

    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.lastName, ' ', u.firstName, ' ', u.patronymic)) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<User> searchByFullName(@Param("query") String query);
}