package ua.cn.stu.npp.npp_portal_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.npp.npp_portal_backend.dto.request.CreateDictionaryItemRequest;
import ua.cn.stu.npp.npp_portal_backend.dto.response.DictionaryItemResponse;
import ua.cn.stu.npp.npp_portal_backend.enums.DictionaryType;
import ua.cn.stu.npp.npp_portal_backend.service.DictionaryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionaries")
@RequiredArgsConstructor
@Slf4j
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/{type}")
    public ResponseEntity<List<DictionaryItemResponse>> getDictionaryItems(
            @PathVariable String type,
            @RequestParam(required = false) Boolean active) {

        log.info("GET /api/v1/dictionaries/{} - active: {}", type, active);

        DictionaryType dictionaryType = DictionaryType.fromUrlPath(type);
        List<DictionaryItemResponse> items = dictionaryService.getAllItems(dictionaryType, active);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<DictionaryItemResponse> getDictionaryItemById(
            @PathVariable String type,
            @PathVariable Byte id) {

        log.info("GET /api/v1/dictionaries/{}/{}", type, id);

        DictionaryType dictionaryType = DictionaryType.fromUrlPath(type);
        DictionaryItemResponse item = dictionaryService.getItemById(dictionaryType, id);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/{type}")
    public ResponseEntity<DictionaryItemResponse> createDictionaryItem(
            @PathVariable String type,
            @Valid @RequestBody CreateDictionaryItemRequest request) {

        log.info("POST /api/v1/dictionaries/{} - Creating item: {}", type, request.getName());

        DictionaryType dictionaryType = DictionaryType.fromUrlPath(type);
        DictionaryItemResponse createdItem = dictionaryService.createItem(dictionaryType, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<DictionaryItemResponse> updateDictionaryItem(
            @PathVariable String type,
            @PathVariable Byte id,
            @Valid @RequestBody CreateDictionaryItemRequest request) {

        log.info("PUT /api/v1/dictionaries/{}/{} - Updating item", type, id);

        DictionaryType dictionaryType = DictionaryType.fromUrlPath(type);
        DictionaryItemResponse updatedItem = dictionaryService.updateItem(dictionaryType, id, request);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> deleteDictionaryItem(
            @PathVariable String type,
            @PathVariable Byte id) {

        log.info("DELETE /api/v1/dictionaries/{}/{} - Soft deleting item", type, id);

        DictionaryType dictionaryType = DictionaryType.fromUrlPath(type);
        dictionaryService.deleteItem(dictionaryType, id);
        return ResponseEntity.noContent().build();
    }
}