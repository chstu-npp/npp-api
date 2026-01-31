package ua.cn.stu.npp.npp_portal_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "faculties", indexes = {
        @Index(name = "idx_faculty_name", columnList = "name"),
        @Index(name = "idx_faculty_institute_active", columnList = "institute_id, active")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"institute", "departments"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Faculty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "SMALLINT")
    @EqualsAndHashCode.Include
    private Byte id;

    @NotBlank(message = "Назва факультету не може бути порожньою")
    @Size(max = 255, message = "Назва факультету не може перевищувати 255 символів")
    @Column(nullable = false, length = 255)
    private String name;

    @Size(max = 255, message = "URL логотипу не може перевищувати 255 символів")
    @Column(name = "fac_logo", nullable = true, length = 255)
    private String facLogo;

    @Size(max = 255, message = "ПІБ керівника не може перевищувати 255 символів")
    @Column(name = "fac_head", nullable = true, length = 255)
    private String facHead;

    @Size(max = 255, message = "URL сторінки не може перевищувати 255 символів")
    @Column(name = "fac_page", nullable = true, length = 255)
    private String facPage;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @NotNull(message = "Інститут обов'язковий")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institute_id", nullable = false)
    @JsonIgnore
    private Institute institute;

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<Department> departments = new HashSet<>();

    public void addDepartment(Department department) {
        departments.add(department);
        department.setFaculty(this);
    }

    public void removeDepartment(Department department) {
        departments.remove(department);
        department.setFaculty(null);
    }
}