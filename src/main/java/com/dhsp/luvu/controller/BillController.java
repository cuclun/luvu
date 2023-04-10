package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.request.BillRequest;
import com.dhsp.luvu.dto.request.CartProductRequest;
import com.dhsp.luvu.dto.response.*;
import com.dhsp.luvu.entity.Bill;
import com.dhsp.luvu.entity.BillDetails;
import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.repository.BillDetailsRepository;
import com.dhsp.luvu.repository.BillRepository;
import com.dhsp.luvu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillDetailsRepository billDetailsRepository;

    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> postBill(@Valid @RequestBody BillRequest billRequest) {
        try {
            Bill bill = new Bill(new Date(), billRequest.getName(), billRequest.getPhone(), billRequest.getEmail(), billRequest.getAddress(), billRequest.getPaymentMethod(), false);
            bill = billRepository.save(bill);

            for (CartProductRequest item : billRequest.getListProduct()) {
                Product product = productRepository.getById(item.getId());
                BillDetails billDetails = new BillDetails(bill, product, item.getSize(), item.getColor(), item.getQty());
                productRepository.updateQuantity(product.getId(), item.getQty());
                billDetailsRepository.save(billDetails);
            }
            return new ResponseEntity<>(new MessageResponse("OK!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("ERROR!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getBill() {
        List<BillResponse> billResponses = new ArrayList<>();
        List<Bill> billList = billRepository.findAll();

        for (Bill bill : billList) {
            List<BillDetailsResponse> billDetailsResponses = new ArrayList<>();
            List<BillDetails> billDetailsList = billDetailsRepository.findAllByBill(bill);

            for (BillDetails billDetails : billDetailsList) {
                billDetailsResponses.add(new BillDetailsResponse(billDetails.getProduct(),
                        billDetails.getColor(), billDetails.getQuantity(),
                        billDetails.getSize()));
            }
            billResponses.add(new BillResponse(bill.getId(),
                    billDetailsResponses, bill.getNameCustom(), bill.getAddressCustom(),
                    bill.getPhoneCustom(), bill.getEmailCustom(), bill.getDateCreate(), bill.getApproved()));

        }

        return new ResponseEntity<>(billResponses, HttpStatus.OK);
    }

    @PutMapping(value={"/{id}", "/{id}/"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> Approved(@PathVariable Long id) {
        billRepository.Approved(id);
        return new ResponseEntity<>(new MessageResponse("Đã duyệt"), HttpStatus.OK);
    }
}
/*
bill: [
  0: {
    name:
    address:
    phone:
    email:
    date_create:
    bill_details: [
      1: {
        product:
        color:
        quantity:
        size:
      }
    ]
  }
]
 */