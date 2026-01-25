package ua.cn.stu.npp.npp_portal_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_details")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull(message = "Користувач обов'язковий")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @NotNull(message = "Ступінь обов'язковий")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_id", nullable = false, columnDefinition = "SMALLINT")
    @JsonIgnore
    private Degree degree;

    @NotNull(message = "Посада обов'язкова")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false, columnDefinition = "SMALLINT")
    @JsonIgnore
    private Position position;

    @NotNull(message = "Рівень англійської обов'язковий")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "english_level_id", nullable = false, columnDefinition = "SMALLINT")
    @JsonIgnore
    private EnglishLevel englishLevel;

    @NotNull(message = "Приналежність до НАН обов'язкова")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nas_affiliation_id", nullable = false, columnDefinition = "SMALLINT")
    @JsonIgnore
    private NasAffiliation nasAffiliation;

    @NotNull(message = "Звання обов'язкове")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "honors_id", nullable = false, columnDefinition = "SMALLINT")
    @JsonIgnore
    private Honor honors;

    @Size(max = 255, message = "URL фото не може перевищувати 255 символів")
    @Column(nullable = true, length = 255)
    private String photo;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}