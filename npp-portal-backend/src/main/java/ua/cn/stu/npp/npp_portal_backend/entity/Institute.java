package ua.cn.stu.npp.npp_portal_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "institutes", indexes = {
        @Index(name = "idx_institute_name", columnList = "name")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"faculties"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Institute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "SMALLINT")
    @EqualsAndHashCode.Include
    private Byte id;

    @NotBlank(message = "Назва інституту не може бути порожньою")
    @Size(max = 100, message = "Назва інституту не може перевищувати 100 символів")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 255, message = "URL логотипу не може перевищувати 255 символів")
    @Column(name = "inst_logo", nullable = true, length = 255)
    private String instLogo;

    @Size(max = 255, message = "ПІБ керівника не може перевищувати 255 символів")
    @Column(name = "inst_head", nullable = true, length = 255)
    private String instHead;

    @Size(max = 255, message = "URL сторінки не може перевищувати 255 символів")
    @Column(name = "inst_page", nullable = true, length = 255)
    private String instPage;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "institute", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    @JsonIgnore
    private Set<Faculty> faculties = new HashSet<>();

    // Методи для двостороннього зв'язку (опціонально, але корисно)
    public void addFaculty(Faculty faculty) {
        faculties.add(faculty);
        faculty.setInstitute(this);
    }

    public void removeFaculty(Faculty faculty) {
        faculties.remove(faculty);
        faculty.setInstitute(null);
    }
}