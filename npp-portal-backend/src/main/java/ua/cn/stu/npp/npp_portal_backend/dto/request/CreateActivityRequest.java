package ua.cn.stu.npp.npp_portal_backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateActivityRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Опис роботи обов'язковий")
    @Size(min = 3, max = 1000, message = "Опис роботи має бути від 3 до 1000 символів")
    private String description;

    @NotNull(message = "Рік обов'язковий")
    @Min(value = 2020, message = "Рік не може бути меншим за 2020")
    @Max(value = 2100, message = "Рік не може бути більшим за 2100")
    private Short year;

    @NotNull(message = "Семестр обов'язковий")
    @Min(value = 0, message = "Семестр має бути 0, 1 або 2")
    @Max(value = 2, message = "Семестр має бути 0, 1 або 2")
    private Byte semester;

    @NotNull(message = "Тип роботи обов'язковий")
    private WorksType worksType;

    @NotNull(message = "ID деталей роботи обов'язковий")
    private Short activityDetailsId;

    private List<Integer> coAuthorIds;
}
