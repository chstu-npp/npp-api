package ua.cn.stu.npp.npp_portal_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateDictionaryItemRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.DictionaryItemResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.*;
import ua.cn.stu.npp.npp_portal_backend.enums.DictionaryType;
import ua.cn.stu.npp.npp_portal_backend.exception.DuplicateResourceException;
import ua.cn.stu.npp.npp_portal_backend.exception.ResourceNotFoundException;
import ua.cn.stu.npp.npp_portal_backend.mapper.DictionaryMapper;
import ua.cn.stu.npp.npp_portal_backend.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DictionaryService {

    private final DegreeRepository degreeRepository;
    private final PositionRepository positionRepository;
    private final EnglishLevelRepository englishLevelRepository;
    private final NasAffiliationRepository nasAffiliationRepository;
    private final HonorRepository honorRepository;
    private final DictionaryMapper dictionaryMapper;

    public List<DictionaryItemResponse> getAllItems(DictionaryType type, Boolean active) {
        log.debug("Fetching {} items with active: {}", type.getDisplayName(), active);

        List<?> items = switch (type) {
            case DEGREES -> active != null ? degreeRepository.findByActive(active) : degreeRepository.findAll();
            case POSITIONS -> active != null ? positionRepository.findByActive(active) : positionRepository.findAll();
            case ENGLISH_LEVELS -> active != null ? englishLevelRepository.findByActive(active) : englishLevelRepository.findAll();
            case NAS_AFFILIATIONS -> active != null ? nasAffiliationRepository.findByActive(active) : nasAffiliationRepository.findAll();
            case HONORS -> active != null ? honorRepository.findByActive(active) : honorRepository.findAll();
        };

        log.debug("Found {} items", items.size());
        return dictionaryMapper.toResponseList(items, type);
    }

    public DictionaryItemResponse getItemById(DictionaryType type, Byte id) {
        log.debug("Fetching {} by id: {}", type.getDisplayName(), id);

        Object item = switch (type) {
            case DEGREES -> degreeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ступінь не знайдено з ID: " + id));
            case POSITIONS -> positionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Посаду не знайдено з ID: " + id));
            case ENGLISH_LEVELS -> englishLevelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Рівень англійської не знайдено з ID: " + id));
            case NAS_AFFILIATIONS -> nasAffiliationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Приналежність до НАН не знайдено з ID: " + id));
            case HONORS -> honorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Звання не знайдено з ID: " + id));
        };

        return dictionaryMapper.toResponse(item, type);
    }

    @Transactional
    public DictionaryItemResponse createItem(DictionaryType type, CreateDictionaryItemRequest request) {
        log.debug("Creating new {} with name: {}", type.getDisplayName(), request.getName());

        checkDuplicateName(type, request.getName(), null);

        Object item = dictionaryMapper.toEntity(request, type);

        switch (type) {
            case DEGREES -> ((Degree) item).setActive(true);
            case POSITIONS -> ((Position) item).setActive(true);
            case ENGLISH_LEVELS -> ((EnglishLevel) item).setActive(true);
            case NAS_AFFILIATIONS -> ((NasAffiliation) item).setActive(true);
            case HONORS -> ((Honor) item).setActive(true);
        }

        Object savedItem = saveItem(type, item);

        log.info("Successfully created {} with id: {}", type.getDisplayName(), getId(savedItem, type));
        return dictionaryMapper.toResponse(savedItem, type);
    }

    @Transactional
    public DictionaryItemResponse updateItem(DictionaryType type, Byte id, CreateDictionaryItemRequest request) {
        log.debug("Updating {} with id: {}", type.getDisplayName(), id);

        Object item = getEntityById(type, id);

        if (request.getName() != null && !request.getName().equalsIgnoreCase(getName(item, type))) {
            checkDuplicateName(type, request.getName(), id);
        }

        dictionaryMapper.updateEntityFromDto(request, item, type);
        Object updatedItem = saveItem(type, item);

        log.info("Successfully updated {} with id: {}", type.getDisplayName(), id);
        return dictionaryMapper.toResponse(updatedItem, type);
    }

    @Transactional
    public void deleteItem(DictionaryType type, Byte id) {
        log.debug("Soft deleting {} with id: {}", type.getDisplayName(), id);

        Object item = getEntityById(type, id);

        switch (type) {
            case DEGREES -> ((Degree) item).setActive(false);
            case POSITIONS -> ((Position) item).setActive(false);
            case ENGLISH_LEVELS -> ((EnglishLevel) item).setActive(false);
            case NAS_AFFILIATIONS -> ((NasAffiliation) item).setActive(false);
            case HONORS -> ((Honor) item).setActive(false);
        }

        saveItem(type, item);

        log.info("Successfully soft deleted {} with id: {}", type.getDisplayName(), id);
    }

    private Object getEntityById(DictionaryType type, Byte id) {
        return switch (type) {
            case DEGREES -> degreeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ступінь не знайдено з ID: " + id));
            case POSITIONS -> positionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Посаду не знайдено з ID: " + id));
            case ENGLISH_LEVELS -> englishLevelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Рівень англійської не знайдено з ID: " + id));
            case NAS_AFFILIATIONS -> nasAffiliationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Приналежність до НАН не знайдено з ID: " + id));
            case HONORS -> honorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Звання не знайдено з ID: " + id));
        };
    }

    private Object saveItem(DictionaryType type, Object item) {
        return switch (type) {
            case DEGREES -> degreeRepository.save((Degree) item);
            case POSITIONS -> positionRepository.save((Position) item);
            case ENGLISH_LEVELS -> englishLevelRepository.save((EnglishLevel) item);
            case NAS_AFFILIATIONS -> nasAffiliationRepository.save((NasAffiliation) item);
            case HONORS -> honorRepository.save((Honor) item);
        };
    }

    private void checkDuplicateName(DictionaryType type, String name, Byte excludeId) {
        boolean exists = switch (type) {
            case DEGREES -> excludeId != null
                    ? degreeRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId)
                    : degreeRepository.existsByNameIgnoreCase(name);
            case POSITIONS -> excludeId != null
                    ? positionRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId)
                    : positionRepository.existsByNameIgnoreCase(name);
            case ENGLISH_LEVELS -> excludeId != null
                    ? englishLevelRepository.existsByLevelIgnoreCaseAndIdNot(name, excludeId)
                    : englishLevelRepository.existsByLevelIgnoreCase(name);
            case NAS_AFFILIATIONS -> excludeId != null
                    ? nasAffiliationRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId)
                    : nasAffiliationRepository.existsByNameIgnoreCase(name);
            case HONORS -> excludeId != null
                    ? honorRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId)
                    : honorRepository.existsByNameIgnoreCase(name);
        };

        if (exists) {
            log.error("{} with name '{}' already exists", type.getDisplayName(), name);
            throw new DuplicateResourceException(type.getDisplayName() + " з такою назвою вже існує: " + name);
        }
    }

    private String getName(Object item, DictionaryType type) {
        return switch (type) {
            case DEGREES -> ((Degree) item).getName();
            case POSITIONS -> ((Position) item).getName();
            case ENGLISH_LEVELS -> ((EnglishLevel) item).getLevel();
            case NAS_AFFILIATIONS -> ((NasAffiliation) item).getName();
            case HONORS -> ((Honor) item).getName();
        };
    }

    private Byte getId(Object item, DictionaryType type) {
        return switch (type) {
            case DEGREES -> ((Degree) item).getId();
            case POSITIONS -> ((Position) item).getId();
            case ENGLISH_LEVELS -> ((EnglishLevel) item).getId();
            case NAS_AFFILIATIONS -> ((NasAffiliation) item).getId();
            case HONORS -> ((Honor) item).getId();
        };
    }
}