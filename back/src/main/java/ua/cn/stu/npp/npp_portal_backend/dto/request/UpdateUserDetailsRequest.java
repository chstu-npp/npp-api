package ua.cn.stu.npp.npp_portal_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDetailsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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