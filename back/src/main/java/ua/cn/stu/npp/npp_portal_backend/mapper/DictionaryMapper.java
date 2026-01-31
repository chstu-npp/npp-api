package ua.cn.stu.npp.npp_portal_backend.mapper;

import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateDictionaryItemRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.DictionaryItemResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.*;
import ua.cn.stu.npp.npp_portal_backend.enums.DictionaryType;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DictionaryMapper {

    public DictionaryItemResponse toResponse(Object entity, DictionaryType type) {
        if (entity == null) {
            return null;
        }

        DictionaryItemResponse.DictionaryItemResponseBuilder builder = DictionaryItemResponse.builder();

        switch (type) {
            case DEGREES -> {
                Degree degree = (Degree) entity;
                builder.id(degree.getId())
                        .name(degree.getName())
                        .shortName(degree.getShortName())
                        .active(degree.isActive());
            }
            case POSITIONS -> {
                Position position = (Position) entity;
                builder.id(position.getId())
                        .name(position.getName())
                        .active(position.isActive());
            }
            case ENGLISH_LEVELS -> {
                EnglishLevel level = (EnglishLevel) entity;
                builder.id(level.getId())
                        .level(level.getLevel())
                        .active(level.isActive());
            }
            case NAS_AFFILIATIONS -> {
                NasAffiliation nas = (NasAffiliation) entity;
                builder.id(nas.getId())
                        .name(nas.getName())
                        .active(nas.isActive());
            }
            case HONORS -> {
                Honor honor = (Honor) entity;
                builder.id(honor.getId())
                        .name(honor.getName())
                        .active(honor.isActive());
            }
        }

        return builder.build();
    }

    public List<DictionaryItemResponse> toResponseList(List<?> entities, DictionaryType type) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(entity -> toResponse(entity, type))
                .collect(Collectors.toList());
    }

    public Object toEntity(CreateDictionaryItemRequest request, DictionaryType type) {
        if (request == null) {
            return null;
        }

        return switch (type) {
            case DEGREES -> Degree.builder()
                    .name(request.getName())
                    .shortName(request.getShortName())
                    .build();
            case POSITIONS -> Position.builder()
                    .name(request.getName())
                    .build();
            case ENGLISH_LEVELS -> EnglishLevel.builder()
                    .level(request.getName())
                    .build();
            case NAS_AFFILIATIONS -> NasAffiliation.builder()
                    .name(request.getName())
                    .build();
            case HONORS -> Honor.builder()
                    .name(request.getName())
                    .build();
        };
    }

    public void updateEntityFromDto(CreateDictionaryItemRequest request, Object entity, DictionaryType type) {
        if (request == null || entity == null) {
            return;
        }

        switch (type) {
            case DEGREES -> {
                Degree degree = (Degree) entity;
                if (request.getName() != null) {
                    degree.setName(request.getName());
                }
                if (request.getShortName() != null) {
                    degree.setShortName(request.getShortName());
                }
            }
            case POSITIONS -> {
                Position position = (Position) entity;
                if (request.getName() != null) {
                    position.setName(request.getName());
                }
            }
            case ENGLISH_LEVELS -> {
                EnglishLevel level = (EnglishLevel) entity;
                if (request.getName() != null) {
                    level.setLevel(request.getName());
                }
            }
            case NAS_AFFILIATIONS -> {
                NasAffiliation nas = (NasAffiliation) entity;
                if (request.getName() != null) {
                    nas.setName(request.getName());
                }
            }
            case HONORS -> {
                Honor honor = (Honor) entity;
                if (request.getName() != null) {
                    honor.setName(request.getName());
                }
            }
        }
    }
}
