/*package com.sistema.orcamento.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;


import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.orcamento.Model.Entity.Cost;
import com.sistema.orcamento.Model.Enum.CostType;
import com.sistema.orcamento.Model.Enum.Supplier;

@SpringBootTest
public class CostTest {
    
    @Test
    public void must_calculate_total_parts_cost_without_discount(){
        Cost cost = new Cost();
        BigDecimal purchaseValue = new BigDecimal(320);
        Supplier supplier = Supplier.C;
        Integer margin = 30;
        CostType costType = CostType.PECA;
        BigDecimal quantity = BigDecimal.valueOf(1);

        BigDecimal expectedValue = BigDecimal.valueOf(568.58).setScale(2);

        BigDecimal calculatedValue = cost.calculateTotalValue(quantity, purchaseValue, supplier, margin, costType);

        Assertions.assertEquals(expectedValue, calculatedValue);

    }

    @Test
    public void must_calculate_total_parts_cost_with_discount(){
    Cost cost = new Cost();
        Integer percentageDiscount = 20;
        BigDecimal purchaseValue = new BigDecimal(320);
        Supplier supplier = Supplier.C;
        Integer margin = 50;
        CostType costType = CostType.PECA;
        BigDecimal quantity = BigDecimal.valueOf(1);

        BigDecimal expectedValue = BigDecimal.valueOf(423.94).setScale(2);

        BigDecimal calculatedValue = cost.calculateDiscountedValue(quantity, purchaseValue, supplier, margin, percentageDiscount, costType);

        Assertions.assertEquals(expectedValue, calculatedValue);   
    }

    @Test
    public void must_calculate_total_service_cost_without_discount(){
        Cost cost = new Cost();
        Supplier supplier = Supplier.SERVICE;

        BigDecimal purchaseValue = new BigDecimal(120);
        CostType costType = CostType.SERVICO;
        BigDecimal quantity = BigDecimal.valueOf(1.5);

        BigDecimal expectedValue = BigDecimal.valueOf(180);

        BigDecimal calculatedValue = cost.calculateTotalValue(quantity, purchaseValue, supplier, 60, costType);

        Assertions.assertEquals(expectedValue, calculatedValue);
    }

    @Test   
    public void must_calculate_total_service_cost_with_discount(){
        Cost cost = new Cost();
        Supplier supplier = Supplier.SERVICE;

        BigDecimal purchaseValue = new BigDecimal(120);
        CostType costType = CostType.SERV_TERCEIROS;
        BigDecimal quantity = BigDecimal.valueOf(1);

        BigDecimal expectedValue = BigDecimal.valueOf(162).setScale(2);

        BigDecimal calculatedValue = cost.calculateDiscountedValue(quantity, purchaseValue, supplier, 60, 10, costType);

        Assertions.assertEquals(expectedValue, calculatedValue);
    }

    @Test
    public void must_calculate_total_third_party_service_cost_without_discount(){
        Cost cost = new Cost();
        Supplier supplier = Supplier.SERVICE;

        BigDecimal purchaseValue = new BigDecimal(10231.55);
        CostType costType = CostType.SERV_TERCEIROS;
        BigDecimal quantity = BigDecimal.valueOf(1);

        BigDecimal expectedValue = BigDecimal.valueOf(17700.58).setScale(2);

        BigDecimal calculatedValue = cost.calculateDiscountedValue(quantity, purchaseValue, supplier, 73, 0, costType);

        Assertions.assertEquals(expectedValue, calculatedValue);
    }
        

    @Test
    public void must_calculate_total_third_party_service_cost_with_discount(){
        Cost cost = new Cost();
        Supplier supplier = Supplier.SERVICE;

        BigDecimal purchaseValue = new BigDecimal(10231.55);
        CostType costType = CostType.SERV_TERCEIROS;
        BigDecimal quantity = BigDecimal.valueOf(1);

        BigDecimal expectedValue = BigDecimal.valueOf(15399.51).setScale(2);

        BigDecimal calculatedValue = cost.calculateDiscountedValue(quantity, purchaseValue, supplier, 73, 13, costType);

        Assertions.assertEquals(expectedValue, calculatedValue);
    }


}
*/