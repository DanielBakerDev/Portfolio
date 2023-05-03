package com.info5059.casestudy.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;
    @PostMapping("/api/pos")
    public ResponseEntity<Long> addOne(@RequestBody PurchaseOrder clientrep) { // use RequestBody here
        Long reportId = purchaseOrderDAO.create(clientrep);
        return new ResponseEntity<Long>(reportId, HttpStatus.OK);
    }
    @GetMapping("/api/pos/{id}")
    public ResponseEntity<Iterable<PurchaseOrder>> findByVendor(@PathVariable Long id) { // use RequestBody here
        Iterable<PurchaseOrder> pos = purchaseOrderDAO.findByVendor(id);
        return new ResponseEntity<Iterable<PurchaseOrder>>(pos, HttpStatus.OK);
    }
}
