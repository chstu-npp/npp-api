package ua.cn.stu.npp.npp_portal_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DictionaryItemResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Byte id;

    private String name;

    private String shortName;

    private String level;

    private boolean active;
}
