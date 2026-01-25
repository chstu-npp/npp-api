package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.ActivityDetails;

import java.util.List;

@Repository
public interface ActivityDetailsRepository extends JpaRepository<ActivityDetails, Short> {

    List<ActivityDetails> findByActive(boolean active);
}
