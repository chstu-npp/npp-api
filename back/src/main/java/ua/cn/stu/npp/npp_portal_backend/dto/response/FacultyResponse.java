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
public class FacultyResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Byte id;

    private String name;

    @JsonProperty("facLogo")
    private String facLogo;

    @JsonProperty("facHead")
    private String facHead;

    @JsonProperty("facPage")
    private String facPage;

    private boolean active;

    private InstituteResponse institute;
}
