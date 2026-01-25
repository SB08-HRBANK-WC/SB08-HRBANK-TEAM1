package com.wc.hr_bank.dto.request.employee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeListRequest {

  private String cursor; // 다음 페이지의 시작점 (예: 마지막 조회한 사원 번호 등)

  private int size = 10; // 한 페이지에 보여줄 개수

}