package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto.DiffDto;
import com.wc.hr_bank.entity.ChangeLog;
import com.wc.hr_bank.entity.ChangeLogDiff;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper
{
  @Mapping(
      source = "updatedAt",
      target = "at",
      dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
  @Mapping(
      source = "diffs",
      target = "profileImageId",
      qualifiedByName = "extractProfileImageId"
  )
  ChangeLogDetailDto toDto(ChangeLog changeLog);

  /**
   * 상세 내역 리스트 변환
   *
   * @param diffs
   * @return
   */
  List<DiffDto> toDiffDtoList(List<ChangeLogDiff> diffs);

  // 특정 필드(image) 찾아 ID 추출
  @Named("extractProfileImageId")
  default Long extractProfileImageId(List<ChangeLogDiff> diffs) {
    if (diffs == null || diffs.isEmpty()) return null;

    return diffs.stream()
        .filter(diff -> "image".equals(diff.getPropertyName())) // 이미지 변경분 조회
        .map(ChangeLogDiff::getAfter)
        .filter(val -> val != null && !val.isBlank() && !val.equals("null"))
        .map(Long::valueOf) // String을 Long으로 변환
        .findFirst()
        .orElse(null);
  }
}

