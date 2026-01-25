package ua.cn.stu.npp.npp_portal_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users_departments")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "department"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserDepartmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("departmentId")
    @JoinColumn(name = "department_id", nullable = false, columnDefinition = "SMALLINT")
    @JsonIgnore
    private Department department;

    @Column(name = "joint", nullable = true, columnDefinition = "SMALLINT")
    private Byte joint;

    @Column(name = "workload", nullable = true)
    private Float workload;

    public UserDepartment(User user, Department department) {
        this.user = user;
        this.department = department;
        this.id = new UserDepartmentId(user.getId(), department.getId());
    }
}