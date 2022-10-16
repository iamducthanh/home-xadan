package com.example.hostelmanager1.repository;

import com.example.hostelmanager1.entity.Spending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author GMO_ThanhND
 * @version 1.0
 * @since 10/11/2022
 */
public interface SpendingRepository extends JpaRepository<Spending, String> {
    @Query("select o from spending o order by o.modifiedDate desc ")
    List<Spending> getAllSpending();
}
