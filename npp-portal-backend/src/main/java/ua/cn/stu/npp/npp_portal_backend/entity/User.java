package ua.cn.stu.npp.npp_portal_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ua.cn.stu.npp.npp_portal_backend.enums.UserRole;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_login", columnList = "login", unique = true),
        @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_user_role_active", columnList = "role, active")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "userDetails", "userDepartments"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "Ім'я не може бути порожнім")
    @Size(max = 255, message = "Ім'я не може перевищувати 255 символів")
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @NotBlank(message = "Прізвище не може бути порожнім")
    @Size(max = 255, message = "Прізвище не може перевищувати 255 символів")
    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @NotBlank(message = "По батькові не може бути порожнім")
    @Size(max = 255, message = "По батькові не може перевищувати 255 символів")
    @Column(nullable = false, length = 255)
    private String patronymic;

    @NotBlank(message = "Логін не може бути порожнім")
    @Size(max = 255, message = "Логін не може перевищувати 255 символів")
    @Column(nullable = false, unique = true, length = 255)
    private String login;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Невірний формат email")
    @Size(max = 255, message = "Email не може перевищувати 255 символів")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(max = 255, message = "Пароль не може перевищувати 255 символів")
    @Column(nullable = false, length = 255)
    @JsonIgnore
    private String password;

    @NotNull(message = "Роль обов'язкова")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role;

    @Column(nullable = false, unique = true, columnDefinition = "UUID")
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<UserDepartment> userDepartments = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<UserActivity> userActivities = new HashSet<>();
}