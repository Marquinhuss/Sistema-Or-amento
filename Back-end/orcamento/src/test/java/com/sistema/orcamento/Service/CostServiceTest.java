/* package com.sistema.orcamento.Service;


import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.orcamento.Model.Entity.Cost;
import com.sistema.orcamento.Model.Enum.CostType;
import com.sistema.orcamento.Model.Enum.Supplier;

@SpringBootTest
public class CostServiceTest {

    @Test
    public void setCost() {
    Cost expectedCost = new Cost();
    expectedCost.setId((long) 12);
    expectedCost.setCostName("Bomba de água");
    expectedCost.setQuantity(BigDecimal.valueOf(100));
    expectedCost.setSupplier(Supplier.C);
    expectedCost.setMargin(100);
    expectedCost.setPercentageDiscount(0);
    expectedCost.setRealDiscount(BigDecimal.valueOf(0));
    expectedCost.setUnitValue(BigDecimal.valueOf(225));
    expectedCost.setTotalValue(BigDecimal.valueOf(225));
    expectedCost.setDiscountedValue(BigDecimal.valueOf(225));
    expectedCost.setCostType(CostType.PECA);

    Cost cost = new Cost();

    cost.setId((long) 12);
    cost.setCostName("Troca Bomba da água");
    cost.setPurchaseValue(BigDecimal.valueOf(120));
    cost.setQuantity(BigDecimal.valueOf(1));
    cost.setSupplier(Supplier.SERVICE);
    cost.setMargin(100);
    cost.setPercentageDiscount(10);
    cost.setCostType(CostType.SERV_TERCEIROS);

    if (cost.getCostType().equals(CostType.SERVICO)) {
    cost.setSupplier(Supplier.SERVICE);
    cost.setMargin(0);
    }

    BigDecimal unitValue = cost.calculateUnitValue(cost.getPurchaseValue(), BigDecimal.valueOf(10), cost.getMargin(), cost.getQuantity());
    BigDecimal totalValue = cost.calculateTotalValue(cost.getQuantity(), cost.getPurchaseValue(),cost.getSupplier(), cost.getMargin(), cost.getCostType());
    BigDecimal discountedValue = cost.calculateDiscountedValue(cost.getQuantity(), cost.getPurchaseValue(),cost.getSupplier(), cost.getMargin(), cost.getPercentageDiscount(), cost.getCostType());
    BigDecimal discountValue = cost.calculateDiscountValue(cost.getPercentageDiscount(), totalValue);

    cost.setUnitValue(unitValue);

    cost.setUnitValue(cost.getCostType().equals(CostType.SERVICO) ? BigDecimal.valueOf(0) : cost.getUnitValue());
    cost.setUnitValue(cost.getCostType().equals(CostType.SERV_TERCEIROS) ? BigDecimal.valueOf(0) : cost.getUnitValue());

    cost.setRealDiscount(discountValue);
    cost.setDiscountedValue(discountedValue);
    cost.setTotalValue(totalValue);

    System.out.println(cost + "Custo com valores calculados");

    

    Assertions.assertEquals(expectedCost, cost);
    // Cost returnCost = costRepository.save(cost);
    }

}
*/