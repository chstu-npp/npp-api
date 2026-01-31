package ua.cn.stu.npp.npp_portal_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.cn.stu.npp.npp_portal_backend.enums.UserRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String login;

    private String email;

    private UserRole role;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserDetailsResponse details;

    private List<UserDepartmentResponse> departments;
}
