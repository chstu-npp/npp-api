package ua.cn.stu.npp.npp_portal_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 255, message = "Ім'я не може перевищувати 255 символів")
    private String firstName;

    @Size(max = 255, message = "Прізвище не може перевищувати 255 символів")
    private String lastName;

    @Size(max = 255, message = "По батькові не може перевищувати 255 символів")
    private String patronymic;

    @Email(message = "Невірний формат email")
    @Size(max = 255, message = "Email не може перевищувати 255 символів")
    private String email;
}
