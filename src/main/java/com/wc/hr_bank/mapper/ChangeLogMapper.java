package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto.DiffDto;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDto;
import com.wc.hr_bank.dto.response.changelog.CursorPageResponseChangeLogDto;
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
   * 수정 이력 조회 content 출력
   * @param changeLog
   * @return
   */
  @Mapping(source = "createdAt", target = "at", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
  ChangeLogDto toLogDto(ChangeLog changeLog);

  /**
   * 수정 이력 조회 content 및 커서 및 부가정보 출력
   * @param content
   * @param nextCursor
   * @param nextIdAfter
   * @param size
   * @param totalElements
   * @param hasNext
   * @return
   */
  CursorPageResponseChangeLogDto toCursorPageResponse(
      List<ChangeLogDto> content,
      String nextCursor,
      Long nextIdAfter,
      int size,
      Long totalElements,
      boolean hasNext
  );

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

