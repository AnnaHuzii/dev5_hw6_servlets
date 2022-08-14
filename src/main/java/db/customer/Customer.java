package db.customer;

import lombok.Data;

@Data
public class Customer {
    private long customerId;
    private String customerName;
    private int EDRPOU;
    private String product;

}
