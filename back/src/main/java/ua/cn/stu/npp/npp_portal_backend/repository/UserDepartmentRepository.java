package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.UserDepartment;
import ua.cn.stu.npp.npp_portal_backend.entity.UserDepartmentId;

import java.util.List;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, UserDepartmentId> {

    List<UserDepartment> findByUserId(Integer userId);

    List<UserDepartment> findByDepartmentId(Byte departmentId);

    boolean existsByUserIdAndDepartmentId(Integer userId, Byte departmentId);
}
