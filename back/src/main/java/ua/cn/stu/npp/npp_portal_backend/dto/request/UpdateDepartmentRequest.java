package ua.cn.stu.npp.npp_portal_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateDepartmentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 255, message = "Назва кафедри не може перевищувати 255 символів")
    private String name;

    @JsonProperty("facultyId")
    private Byte facultyId;

    @Size(max = 255, message = "URL логотипу не може перевищувати 255 символів")
    @JsonProperty("depLogo")
    private String depLogo;

    @Size(max = 255, message = "ПІБ керівника не може перевищувати 255 символів")
    @JsonProperty("depHead")
    private String depHead;

    @Size(max = 255, message = "URL сторінки не може перевищувати 255 символів")
    @JsonProperty("depPage")
    private String depPage;
}
