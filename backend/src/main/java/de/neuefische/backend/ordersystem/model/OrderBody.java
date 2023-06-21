package de.neuefische.backend.ordersystem.model;

import de.neuefische.backend.productsystem.model.ProductBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("OrderList")
public class OrderBody {
    private String id;
    private String owner;
    private List<ProductBody> productBodyList;
    private double price;
    private String created;
    private String arrival;
    private boolean approvalPurchase;
    private boolean approvalLead;
    private String orderStatus;
}
