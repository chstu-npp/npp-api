package ua.cn.stu.npp.npp_portal_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;

@Entity
@Table(name = "activities", indexes = {
        @Index(name = "idx_activity_year_semester", columnList = "year, semester"),
        @Index(name = "idx_activity_works_type", columnList = "works_type"),
        @Index(name = "idx_activity_details_id", columnList = "activity_details_id")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"activityDetails", "userActivities"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "Опис роботи не може бути порожнім")
    @Size(max = 1000, message = "Опис роботи не може перевищувати 1000 символів")
    @Column(nullable = false, length = 1000)
    private String description;

    @NotNull(message = "Рік обов'язковий")
    @Min(value = 2020, message = "Рік не може бути меншим за 2020")
    @Max(value = 2100, message = "Рік не може бути більшим за 2100")
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private Short year;

    @NotNull(message = "Семестр обов'язковий")
    @Min(value = 0, message = "Семестр має бути 0, 1 або 2")
    @Max(value = 2, message = "Семестр має бути 0, 1 або 2")
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private Byte semester;

    @NotNull(message = "Тип роботи обов'язковий")
    @Enumerated(EnumType.STRING)
    @Column(name = "works_type", nullable = false, length = 50)
    private WorksType worksType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @NotNull(message = "Деталі роботи обов'язкові")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_details_id", nullable = false, columnDefinition = "SMALLINT")
    @JsonIgnore
    private ActivityDetails activityDetails;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private Set<UserActivity> userActivities = new HashSet<>();
}
