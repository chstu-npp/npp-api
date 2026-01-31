package ua.cn.stu.npp.npp_portal_backend.dto.response;

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
public class ActivityDetailsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Short id;

    private Short numInOrd;

    private Byte numInRegulations;

    private Float coefficient;

    private Float normTime;

    private Map<String, Object> additionalDetails;

    private boolean active;
}
