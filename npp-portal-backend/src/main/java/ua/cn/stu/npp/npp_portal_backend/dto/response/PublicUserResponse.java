package ua.cn.stu.npp.npp_portal_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicUserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String email;

    private String photo;

    private List<PublicUserDepartmentResponse> departments;
}
