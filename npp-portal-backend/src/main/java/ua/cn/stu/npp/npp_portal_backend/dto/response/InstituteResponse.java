package ua.cn.stu.npp.npp_portal_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO для відповіді з інформацією про інститут
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstituteResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Byte id;

    private String name;

    @JsonProperty("instLogo")
    private String instLogo;

    @JsonProperty("instHead")
    private String instHead;

    @JsonProperty("instPage")
    private String instPage;

    private boolean active;
}

