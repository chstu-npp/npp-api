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
@Table(name = "departments", indexes = {
        @Index(name = "idx_department_name", columnList = "name"),
        @Index(name = "idx_department_faculty_active", columnList = "faculty_id, active")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"faculty", "userDepartments"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "SMALLINT")
    @EqualsAndHashCode.Include
    private Byte id;

    @NotBlank(message = "Назва кафедри не може бути порожньою")
    @Size(max = 255, message = "Назва кафедри не може перевищувати 255 символів")
    @Column(nullable = false, length = 255)
    private String name;

    @Size(max = 255, message = "URL логотипу не може перевищувати 255 символів")
    @Column(name = "dep_logo", nullable = true, length = 255)
    private String depLogo;

    @Size(max = 255, message = "ПІБ керівника не може перевищувати 255 символів")
    @Column(name = "dep_head", nullable = true, length = 255)
    private String depHead;

    @Size(max = 255, message = "URL сторінки не може перевищувати 255 символів")
    @Column(name = "dep_page", nullable = true, length = 255)
    private String depPage;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @NotNull(message = "Факультет обов'язковий")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonIgnore
    private Faculty faculty;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<UserDepartment> userDepartments = new HashSet<>();
}