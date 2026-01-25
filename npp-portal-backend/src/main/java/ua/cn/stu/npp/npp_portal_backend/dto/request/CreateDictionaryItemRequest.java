package ua.cn.stu.npp.npp_portal_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class CreateDictionaryItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Назва обов'язкова")
    @Size(max = 255, message = "Назва не може перевищувати 255 символів")
    private String name;

    @Size(max = 255, message = "Коротка назва не може перевищувати 255 символів")
    private String shortName;
}
