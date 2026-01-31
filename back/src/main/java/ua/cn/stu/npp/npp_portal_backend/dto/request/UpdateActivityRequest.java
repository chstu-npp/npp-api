package ua.cn.stu.npp.npp_portal_backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateActivityRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(min = 3, max = 1000, message = "Опис роботи має бути від 3 до 1000 символів")
    private String description;

    @Min(value = 2020, message = "Рік не може бути меншим за 2020")
    @Max(value = 2100, message = "Рік не може бути більшим за 2100")
    private Short year;

    @Min(value = 0, message = "Семестр має бути 0, 1 або 2")
    @Max(value = 2, message = "Семестр має бути 0, 1 або 2")
    private Byte semester;

    private Short activityDetailsId;

    private List<Integer> coAuthorIds;
}
