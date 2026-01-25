package ua.cn.stu.npp.npp_portal_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.cn.stu.npp.npp_portal_backend.enums.WorksType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicActivityResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String description;

    private Short year;

    private Byte semester;

    private WorksType worksType;

    private LocalDateTime createdAt;

    private List<PublicActivityUserResponse> users;
}
