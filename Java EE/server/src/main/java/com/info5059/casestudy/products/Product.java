package com.info5059.casestudy.products;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Expense entity
 */
@Entity
@Data
@RequiredArgsConstructor
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private int vendorid;
    private String name;
    private BigDecimal costprice;
    private BigDecimal msrp;
    private int rop;
    private int eoq;
    private int qoh;
    private int qoo;
    // needed in case 2
    @Lob
    @Basic(optional = true)
    private String qrcodetxt;

    @Lob
    @Basic(optional = true)
    private byte[] qrcode;
    // needed in case 2


}
