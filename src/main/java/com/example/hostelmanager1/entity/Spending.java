package com.example.hostelmanager1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/11/2022 9:33 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "spending")
@Builder
public class Spending {
 @Id
 private String id;
 private String expenses;
 private Integer price;
 @ManyToOne
 @JoinColumn(name = "create_by")
 private Member member;
 @Column(name = "create_date")
 private Date createdDate;
 @Column(name = "modify_by")
 private String modifiedBy;
 @Column(name = "modify_date")
 private Date modifiedDate;
 private String note;
 @Transient
 private boolean isDelete;

 @OneToMany(mappedBy = "spendingId")
 private List<MemberSpending> memberSpendings;
}
