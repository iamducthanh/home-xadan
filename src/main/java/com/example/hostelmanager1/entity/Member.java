package com.example.hostelmanager1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/10/2022 10:14 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "member")
@Builder
public class Member {
 @Id
 private Integer id;
 private String username;
 private String password;
 private String fullname;
 private String img;

}
