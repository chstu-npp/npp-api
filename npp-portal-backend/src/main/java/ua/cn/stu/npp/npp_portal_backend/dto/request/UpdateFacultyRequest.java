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
public class UpdateFacultyRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 255, message = "Назва факультету не може перевищувати 255 символів")
    private String name;

    @JsonProperty("instituteId")
    private Byte instituteId;

    @Size(max = 255, message = "URL логотипу не може перевищувати 255 символів")
    @JsonProperty("facLogo")
    private String facLogo;

    @Size(max = 255, message = "ПІБ керівника не може перевищувати 255 символів")
    @JsonProperty("facHead")
    private String facHead;

    @Size(max = 255, message = "URL сторінки не може перевищувати 255 символів")
    @JsonProperty("facPage")
    private String facPage;
}
