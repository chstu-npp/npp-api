package ua.cn.stu.npp.npp_portal_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.cn.stu.npp.npp_portal_backend.entity.Activity;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;

import java.util.List;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    Page<Activity> findByActive(boolean active, Pageable pageable);

    Page<Activity> findByWorksType(WorksType worksType, Pageable pageable);

    Page<Activity> findByWorksTypeAndActive(WorksType worksType, boolean active, Pageable pageable);

    Page<Activity> findByYear(Short year, Pageable pageable);

    Page<Activity> findByYearAndActive(Short year, boolean active, Pageable pageable);

    Page<Activity> findByYearAndSemester(Short year, Byte semester, Pageable pageable);

    Page<Activity> findByYearAndSemesterAndActive(Short year, Byte semester, boolean active, Pageable pageable);

    @Query("SELECT a FROM Activity a JOIN a.userActivities ua WHERE ua.user.id = :userId")
    Page<Activity> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT a FROM Activity a JOIN a.userActivities ua WHERE ua.user.id = :userId AND a.active = :active")
    Page<Activity> findByUserIdAndActive(@Param("userId") Integer userId, @Param("active") boolean active, Pageable pageable);

    @Query("SELECT a FROM Activity a JOIN a.userActivities ua WHERE ua.user.id = :userId AND a.worksType = :worksType")
    Page<Activity> findByUserIdAndWorksType(@Param("userId") Integer userId, @Param("worksType") WorksType worksType, Pageable pageable);

    @Query("SELECT a FROM Activity a JOIN a.userActivities ua WHERE ua.user.id = :userId AND a.year = :year")
    Page<Activity> findByUserIdAndYear(@Param("userId") Integer userId, @Param("year") Short year, Pageable pageable);

    @Query("SELECT a FROM Activity a JOIN a.userActivities ua WHERE ua.user.id = :userId AND a.year >= :yearFrom AND a.year <= :yearTo")
    Page<Activity> findByUserIdAndYearBetween(@Param("userId") Integer userId, @Param("yearFrom") Short yearFrom, @Param("yearTo") Short yearTo, Pageable pageable);
}