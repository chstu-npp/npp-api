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
public class UserDetailsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String photo;

    private DictionaryItemResponse degree;

    private DictionaryItemResponse position;

    private DictionaryItemResponse englishLevel;

    private DictionaryItemResponse nasAffiliation;

    private DictionaryItemResponse honors;
}
