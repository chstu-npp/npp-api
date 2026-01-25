package ua.cn.stu.npp.npp_portal_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Map;

@Entity
@Table(name = "activity_details")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ActivityDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "SMALLINT")
    @EqualsAndHashCode.Include
    private Short id;

    @Column(name = "num_in_ord", nullable = true, columnDefinition = "SMALLINT")
    private Short numInOrd;

    @NotNull(message = "Номер у положенні обов'язковий")
    @Column(name = "num_in_regulations", nullable = false, columnDefinition = "SMALLINT")
    private Byte numInRegulations;

    @NotNull(message = "Коефіцієнт обов'язковий")
    @Column(nullable = false)
    private Float coefficient;

    @NotNull(message = "Нормований час обов'язковий")
    @Column(name = "norm_time", nullable = false)
    private Float normTime;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_details", columnDefinition = "jsonb")
    private Map<String, Object> additionalDetails;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}