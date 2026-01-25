package ua.cn.stu.npp.npp_portal_backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DictionaryType {
    DEGREES("degrees", "Ступені"),
    POSITIONS("positions", "Посади"),
    ENGLISH_LEVELS("english-levels", "Рівні англійської"),
    NAS_AFFILIATIONS("nas-affiliations", "Приналежність до НАН"),
    HONORS("honors", "Звання");

    private final String urlPath;
    private final String displayName;

    public static DictionaryType fromUrlPath(String urlPath) {
        for (DictionaryType type : values()) {
            if (type.urlPath.equalsIgnoreCase(urlPath)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Невідомий тип довідника: " + urlPath);
    }
}
