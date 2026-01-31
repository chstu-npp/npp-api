package ua.cn.stu.npp.npp_portal_backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    TEACHER("Викладач"),
    DIRECTOR("Завідувач кафедри/Декан"),
    ADMIN("Адміністратор");

    private final String displayName;
}
