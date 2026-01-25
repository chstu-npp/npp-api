package ua.cn.stu.npp.npp_portal_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Byte id;

    private String name;

    @JsonProperty("depLogo")
    private String depLogo;

    @JsonProperty("depHead")
    private String depHead;

    @JsonProperty("depPage")
    private String depPage;

    private boolean active;

    private FacultyResponse faculty;
}