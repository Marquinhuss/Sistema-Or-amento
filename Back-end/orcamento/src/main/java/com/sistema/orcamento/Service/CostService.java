package com.sistema.orcamento.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.orcamento.Model.Entity.Cost;
import com.sistema.orcamento.Model.Enum.CostType;
import com.sistema.orcamento.Model.Enum.Supplier;
import com.sistema.orcamento.Model.Supplier.SupplierShipping;
import com.sistema.orcamento.Repository.BudgetRepository;
import com.sistema.orcamento.Repository.CostRepository;

@Service
public class CostService {
    
    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    CostRepository costRepository;
    
    /* 
    public Cost setCost(Cost cost){ 
        if(cost.getCostType().equals(CostType.SERVICO)){
            cost.setSupplier(Supplier.SERVICE);
            cost.setMargin(0);
        }
        
        BigDecimal unitValue = cost.calculateUnitValue(cost.getPurchaseValue(), cost.getSupplier(), cost.getMargin(), cost.getQuantity());
        BigDecimal totalValue = cost.calculateTotalValue(cost.getQuantity(), cost.getPurchaseValue(), cost.getSupplier(), cost.getMargin(), cost.getCostType());
        BigDecimal discountedValue = cost.calculateDiscountedValue(cost.getQuantity(), cost.getPurchaseValue(), cost.getSupplier(), cost.getMargin(), cost.getPercentageDiscount(), cost.getCostType());
        BigDecimal discountValue = cost.calculateDiscountValue(cost.getPercentageDiscount(), totalValue);

        cost.setUnitValue(unitValue);

        cost.setUnitValue(cost.getCostType().equals(CostType.SERVICO) ? BigDecimal.valueOf(0) : cost.getUnitValue());
        cost.setUnitValue(cost.getCostType().equals(CostType.SERV_TERCEIROS) ? BigDecimal.valueOf(0) : cost.getUnitValue());

        cost.setRealDiscount(discountValue);
        cost.setDiscountedValue(discountedValue);
        cost.setTotalValue(totalValue);

        Cost returnCost = costRepository.save(cost);

        return returnCost;
    }
    */

    public List<Cost> setCosts(List<Cost> costList){ 
        List<Cost> returnList = new ArrayList<>();
        Map<Supplier, BigDecimal> unitShippingCostMap = calculateUnitShippingValue(costList);
        
        for (Cost cost : costList) {
            if(cost.getCostType().equals(CostType.SERVICO)){
                cost.setSupplier(Supplier.SERVICE);
                cost.setMargin(0);
            }

            // Variables values declaration from each cost in the cost List
            BigDecimal unitShippingValue = unitShippingCostMap.get(cost.getSupplier());
            BigDecimal purchaseValue = cost.getPurchaseValue();
            Integer margin = cost.getMargin();
            BigDecimal quantity = cost.getQuantity();
            CostType costType = cost.getCostType();
            Integer percentageDiscount = cost.getPercentageDiscount();
            // End of variables declaration

            System.out.println("Função SetCosts - Percentual de desconto de cada custo: " + percentageDiscount);

            BigDecimal unitValue = cost.calculateUnitValue(purchaseValue, unitShippingValue, margin, quantity);
            BigDecimal totalValue = cost.calculateTotalValue(quantity, purchaseValue, unitShippingValue, margin, costType);

            BigDecimal discountedValue = cost.calculateDiscountedValue(quantity, purchaseValue, unitShippingValue, margin, percentageDiscount, costType);

            System.out.println("Função SetCosts - Valor descontado de cada custo " + discountedValue);

            BigDecimal discountValue = cost.calculateDiscountValue(percentageDiscount, totalValue);

            System.out.println("Função SetCosts - Valor do desconto de cada custo " + discountValue);
            
            cost.setUnitValue(unitValue);
            cost.setUnitValue(cost.getCostType().equals(CostType.SERVICO) ? BigDecimal.valueOf(0) : cost.getUnitValue());
            cost.setUnitValue(cost.getCostType().equals(CostType.SERV_TERCEIROS) ? BigDecimal.valueOf(0) : cost.getUnitValue());
    
            cost.setRealDiscount(discountValue);
            cost.setDiscountedValue(discountedValue);
            cost.setTotalValue(totalValue);
    
            Cost returnCost = costRepository.save(cost);
            returnList.add(returnCost);
        }
        return returnList;
    }

    public Map<Supplier, BigDecimal> calculateUnitShippingValue(List<Cost> costList){
        Map <Supplier, SupplierShipping> shippingValueMap = new HashMap<>();

        for (Cost cost : costList) {
            SupplierShipping orDefault = shippingValueMap.getOrDefault(cost.getSupplier(), new SupplierShipping(cost));
            orDefault.increase();
            shippingValueMap.put(cost.getSupplier(), orDefault); 
        }
        Map<Supplier, BigDecimal> unitShippingCostMap = new HashMap<>();
        for (Map.Entry<Supplier, SupplierShipping> entry : shippingValueMap.entrySet()) {
            Supplier supplier = entry.getKey();
            SupplierShipping supplierShipping = entry.getValue();
            BigDecimal unitShippingCost = supplierShipping.getShipping().divide(BigDecimal.valueOf(supplierShipping.getQuantity()), 2, RoundingMode.HALF_UP);
            unitShippingCostMap.put(supplier, unitShippingCost);
        }
        return unitShippingCostMap;
    }

    /*  
    public BigDecimal calculateUnitShippingValue(Cost cost){
        Map <Supplier, SupplierShipping> shippingValueMap = new HashMap<>();

        SupplierShipping orDefault = shippingValueMap.getOrDefault(cost.getSupplier(), new SupplierShipping(cost));
        orDefault.increase();
        shippingValueMap.put(cost.getSupplier(), orDefault); 
       
        Map<Supplier, BigDecimal> unitShippingCostMap = new HashMap<>();
        for (Map.Entry<Supplier, SupplierShipping> entry : shippingValueMap.entrySet()) {
            Supplier supplier = entry.getKey();
            SupplierShipping supplierShipping = entry.getValue();
            BigDecimal unitShippingCost = supplierShipping.getShipping().divide(BigDecimal.valueOf(supplierShipping.getQuantity()), 2, RoundingMode.HALF_UP);
            unitShippingCostMap.put(supplier, unitShippingCost);
        }
        return unitShippingCostMap.get(cost.getSupplier());
    } 
    */
    

    /*public List<Cost> getAllCost(List<Cost> costList){
        List<Cost> costList = new ArrayList<>();
        for (cost c : costListDTO) {
            Cost cost = c.toEntity();
            costList.add(cost);
        }

        return costList;
    }
    */
}
