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
 * @since 10/13/2022 9:39 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendingDto {
 private String idSpending;
 private String expenses;
 private String price;
 private List<String> use;
 private String note;
}
