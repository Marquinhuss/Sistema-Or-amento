package com.sistema.orcamento.Model.Supplier;

import java.math.BigDecimal;

import com.sistema.orcamento.Model.Entity.Cost;


import lombok.Data;

@Data
public class SupplierShipping {
    
    public SupplierShipping(Cost cost){
        this.shipping = cost.getSupplier().getshippingValue(cost.getMargin());
        this.quantity = 0;
    }

    public void increase(){
        this.quantity++;
    }

    private BigDecimal shipping;

    private Integer quantity;

}
