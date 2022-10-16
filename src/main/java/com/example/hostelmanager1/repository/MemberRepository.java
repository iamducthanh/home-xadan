package com.example.hostelmanager1.repository;

import com.example.hostelmanager1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/10/2022 10:13 PM
 */
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByUsername(String username);
}
