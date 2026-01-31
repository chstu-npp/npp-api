package ua.cn.stu.npp.npp_portal_backend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.cn.stu.npp.npp_portal_backend.dto.response.UserDetailsResponse;
import ua.cn.stu.npp.npp_portal_backend.entity.UserDetails;
import ua.cn.stu.npp.npp_portal_backend.enums.DictionaryType;

@Component
@RequiredArgsConstructor
public class UserDetailsMapper {

    private final DictionaryMapper dictionaryMapper;

    public UserDetailsResponse toResponse(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }

        return UserDetailsResponse.builder()
                .id(userDetails.getId())
                .photo(userDetails.getPhoto())
                .degree(dictionaryMapper.toResponse(userDetails.getDegree(), DictionaryType.DEGREES))
                .position(dictionaryMapper.toResponse(userDetails.getPosition(), DictionaryType.POSITIONS))
                .englishLevel(dictionaryMapper.toResponse(userDetails.getEnglishLevel(), DictionaryType.ENGLISH_LEVELS))
                .nasAffiliation(dictionaryMapper.toResponse(userDetails.getNasAffiliation(), DictionaryType.NAS_AFFILIATIONS))
                .honors(dictionaryMapper.toResponse(userDetails.getHonors(), DictionaryType.HONORS))
                .build();
    }
}
