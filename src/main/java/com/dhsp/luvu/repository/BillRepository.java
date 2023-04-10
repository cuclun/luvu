package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    @Transactional
    @Modifying
    @Query("update Bill b set b.approved = true where b.id = :id")
    void Approved(Long id);
}
