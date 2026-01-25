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
public class AssignDepartmentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID кафедри обов'язковий")
    private Byte departmentId;

    private Byte joint;

    private Float workload;
}
