package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.UserActivity;
import ua.cn.stu.npp.npp_portal_backend.entity.UserActivityId;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UserActivityId> {

    List<UserActivity> findByUserId(Integer userId);

    List<UserActivity> findByActivityId(Integer activityId);

    boolean existsByUserIdAndActivityId(Integer userId, Integer activityId);
}
