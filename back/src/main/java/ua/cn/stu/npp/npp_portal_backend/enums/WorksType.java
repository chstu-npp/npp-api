package ua.cn.stu.npp.npp_portal_backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorksType {
    SCIENCE("Наукова робота"),
    METHOD("Методична робота"),
    ORGANIZATIONAL("Організаційна робота");

    private final String displayName;
}
