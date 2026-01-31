package ua.cn.stu.npp.npp_portal_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "degrees", indexes = {
        @Index(name = "idx_degree_name", columnList = "name")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Degree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "SMALLINT")
    @EqualsAndHashCode.Include
    private Byte id;

    @NotBlank(message = "Назва ступеня не може бути порожньою")
    @Size(max = 255, message = "Назва ступеня не може перевищувати 255 символів")
    @Column(nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Коротка назва ступеня не може бути порожньою")
    @Size(max = 255, message = "Коротка назва ступеня не може перевищувати 255 символів")
    @Column(name = "short_name", nullable = false, length = 255)
    private String shortName;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
