package com.example.hostelmanager1.repository;

import com.example.hostelmanager1.entity.MemberSpending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author GMO_ThanhND
 * @version 1.0
 * @since 10/11/2022
 */
public interface MemberSpendingRepository extends JpaRepository<MemberSpending, String> {
    List<MemberSpending> findAllBySpendingId(String spendingId);
}
