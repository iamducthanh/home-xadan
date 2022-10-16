package com.example.hostelmanager1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/16/2022 2:07 PM
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitInfo {
 public Integer creditorId;
 public String creditor;
 public Integer total;
}
