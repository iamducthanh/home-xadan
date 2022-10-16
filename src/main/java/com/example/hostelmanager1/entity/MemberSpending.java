package com.example.hostelmanager1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/11/2022 9:35 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "member_spending")
@Builder
public class MemberSpending {
 @Id
 private String id;
 @Column(name = "spending_id")
 private String spendingId;
 @ManyToOne
 @JoinColumn(name = "member_id")
 private Member member;
}
