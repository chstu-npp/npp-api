package ua.cn.stu.npp.npp_portal_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.cn.stu.npp.npp_portal_backend.enums.UserRole;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Ім'я обов'язкове")
    @Size(max = 255, message = "Ім'я не може перевищувати 255 символів")
    private String firstName;

    @NotBlank(message = "Прізвище обов'язкове")
    @Size(max = 255, message = "Прізвище не може перевищувати 255 символів")
    private String lastName;

    @NotBlank(message = "По батькові обов'язкове")
    @Size(max = 255, message = "По батькові не може перевищувати 255 символів")
    private String patronymic;

    @NotBlank(message = "Email обов'язковий")
    @Email(message = "Невірний формат email")
    @Size(max = 255, message = "Email не може перевищувати 255 символів")
    private String email;

    @NotNull(message = "Роль обов'язкова")
    private UserRole role;

    @NotNull(message = "ID ступеня обов'язковий")
    private Byte degreeId;

    @NotNull(message = "ID посади обов'язковий")
    private Byte positionId;

    @NotNull(message = "ID рівня англійської обов'язковий")
    private Byte englishLevelId;

    @NotNull(message = "ID приналежності до НАН обов'язковий")
    private Byte nasAffiliationId;

    @NotNull(message = "ID звання обов'язковий")
    private Byte honorsId;
}