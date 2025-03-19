package com.sistema.orcamento.Model.DTO;

/*  
import java.math.BigDecimal;

import com.sistema.orcamento.Model.Entity.Cost;
import com.sistema.orcamento.Model.Enum.Supplier;
import com.sistema.orcamento.Model.Enum.CostType;

public record CostDTO(Long costId, 
                    String costName,
                    Integer quantityCost, 
                    BigDecimal purchaseValue,
                    Supplier supplier,
                    Integer margin,
                    Integer percentageDiscount,
                    BigDecimal realDiscount,
                    BigDecimal unitValue,
                    BigDecimal totalValue,
                    BigDecimal discountedValue,
                    CostType costType) {


    public static CostDTO fromEntity(Cost cost){
        return new CostDTO(cost.getId(),
                           cost.getCostName(),
                           cost.getQuantityCost(),
                           cost.getPurchaseValue(),
                           cost.getSupplier(),
                           cost.getMargin(),
                           cost.getPercentageDiscount(),
                           cost.getRealDiscount(),
                           cost.getUnitValue(),
                           cost.getTotalValue(),
                           cost.getDiscountedValue(),
                           cost.getCostType()
                          );
    }

    public Cost toEntity(){
        return new Cost(
            this.costId,
            this.costName,
            this.quantityCost,
            this.purchaseValue,
            this.supplier,
            this.margin,
            this.percentageDiscount,
            this.realDiscount,
            this.unitValue,
            this.totalValue,
            this.discountedValue,
            this.costType
        );
    }
}
    */