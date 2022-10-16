package com.example.hostelmanager1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/16/2022 2:04 PM
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatis {
 public Integer id;
 public String fullName;
 public String img;
 public Integer totalSpending;
 public Integer debitTotal;
 public List<DebitInfo> debitInfos;
}
