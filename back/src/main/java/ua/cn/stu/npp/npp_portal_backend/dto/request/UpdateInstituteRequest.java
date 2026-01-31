package ua.cn.stu.npp.npp_portal_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO для оновлення інституту
 * Всі поля опціональні (можна оновлювати частково)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInstituteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100, message = "Назва інституту не може перевищувати 100 символів")
    private String name;

    @Size(max = 255, message = "URL логотипу не може перевищувати 255 символів")
    @JsonProperty("instLogo")
    private String instLogo;

    @Size(max = 255, message = "ПІБ керівника не може перевищувати 255 символів")
    @JsonProperty("instHead")
    private String instHead;

    @Size(max = 255, message = "URL сторінки не може перевищувати 255 символів")
    @JsonProperty("instPage")
    private String instPage;
}
