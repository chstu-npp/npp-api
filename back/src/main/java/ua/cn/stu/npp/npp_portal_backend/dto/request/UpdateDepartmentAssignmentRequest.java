package ua.cn.stu.npp.npp_portal_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDepartmentAssignmentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Byte joint;

    private Float workload;
}
