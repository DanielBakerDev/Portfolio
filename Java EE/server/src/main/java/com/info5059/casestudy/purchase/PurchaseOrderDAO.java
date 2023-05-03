package com.info5059.casestudy.purchase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
@Component
public class PurchaseOrderDAO {
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public Long create(PurchaseOrder clientrep) {
        PurchaseOrder realPurchaseOrder = new PurchaseOrder();
        realPurchaseOrder.setAmount(clientrep.getAmount());
        realPurchaseOrder.setPodate(new Date());
        realPurchaseOrder.setDatecreated(clientrep.getDatecreated());
        realPurchaseOrder.setVendorid(clientrep.getVendorid());
        entityManager.persist(realPurchaseOrder);
        for(PurchaseOrderLineitem item :clientrep.getItems()) {
            PurchaseOrderLineitem realItem = new PurchaseOrderLineitem();
            realItem.setPoid(realPurchaseOrder.getId());
            realItem.setPrice(item.getPrice());
            realItem.setProductid(item.getProductid());
            realItem.setQty(item.getQty());
            entityManager.persist(realItem);
        }
        return realPurchaseOrder.getId();
    }
    public PurchaseOrder findOne(Long id) {

        PurchaseOrder po = entityManager.find(PurchaseOrder.class, id);

        if (po == null) {
            throw new EntityNotFoundException("Can't find PurchaseOrder for ID "
                    + id);
        }
        return po;
    }
    public Iterable<PurchaseOrder> findByVendor(Long vendorId) {
        return entityManager.createQuery("select r from PurchaseOrder r where r.vendorid = :id")
                .setParameter("id", vendorId)
                .getResultList();
    }

}

