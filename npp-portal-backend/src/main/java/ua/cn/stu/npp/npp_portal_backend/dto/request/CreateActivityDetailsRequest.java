package ua.cn.stu.npp.npp_portal_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateActivityDetailsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Short numInOrd;

    @NotNull(message = "Номер у положенні обов'язковий")
    private Byte numInRegulations;

    @NotNull(message = "Коефіцієнт обов'язковий")
    private Float coefficient;

    @NotNull(message = "Нормований час обов'язковий")
    private Float normTime;

    private Map<String, Object> additionalDetails;
}
