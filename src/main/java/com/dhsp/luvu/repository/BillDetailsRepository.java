package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Bill;
import com.dhsp.luvu.entity.BillDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillDetailsRepository extends JpaRepository<BillDetails, Long> {
    List<BillDetails> findAllByBill(Bill bill);
}
