package com.sistema.orcamento.Model.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sistema.orcamento.Model.Enum.CostType;
import com.sistema.orcamento.Model.Enum.Supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "cost")
@Table(name = "costs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String costName;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private BigDecimal purchaseValue;

    private Supplier supplier;

    private Integer margin;

    private Integer percentageDiscount;

    private BigDecimal realDiscount;

    private BigDecimal unitValue;

    private BigDecimal totalValue;

    private BigDecimal discountedValue;

    private CostType costType;

    //#region
    //#region calculateWithoutDiscount
    /**
     * @implNote
     * @param Integer quantity
     * @param BigDecimal purchaseValue
     * @param Supplier supplier
     * @param Integer margin
     * @param CostType costType
     * @return  Returns the BigDecimal total value cost without discount.
     */
     public BigDecimal  calculateTotalValue(BigDecimal quantity, BigDecimal purchaseValue, BigDecimal unitShippingValue, Integer comingMargin, CostType costType) {
        BigDecimal marginIncreaseValue = calculateMargin(purchaseValue, comingMargin);
        if(costType.equals(CostType.SERVICO)){
            Integer discount = 0;
            BigDecimal serviceValue = calculateServiceValue(purchaseValue, discount);
            return serviceValue.setScale(2, RoundingMode.HALF_UP);
        } 
        if(costType.equals(CostType.PECA)){
            BigDecimal partsValue = calculatePartsValue(quantity, purchaseValue, unitShippingValue, marginIncreaseValue, comingMargin);
            return partsValue.setScale(2, RoundingMode.HALF_UP);
        }
        purchaseValue = purchaseValue.add(marginIncreaseValue);
        BigDecimal finalValue = quantity.multiply(purchaseValue);
        return finalValue;     
    }
    //#endregion

    //#region calculateWithDiscount
    /**
     * @implNote
     * @return  Returns the BigDecimal total value cost with discount.
     */
    public BigDecimal calculateDiscountedValue(BigDecimal quantity, BigDecimal purchaseValue, BigDecimal unitShippingValue, Integer margin ,Integer percentageDiscount, CostType costType) {
        BigDecimal marginIncreaseValue = calculateMargin(purchaseValue, margin);
        if(costType.equals(CostType.SERVICO)){
            BigDecimal serviceValue = calculateServiceValue(purchaseValue, percentageDiscount);
            return serviceValue.setScale(2, RoundingMode.HALF_UP);
        }  
        if(costType.equals(CostType.PECA)){
            BigDecimal partsValue = calculatePartsValue(quantity, purchaseValue, unitShippingValue, marginIncreaseValue, margin);
            BigDecimal discountValue = calculateDiscountValue(percentageDiscount, partsValue);
            BigDecimal discountedValue = partsValue.subtract(discountValue);
    
            return discountedValue.setScale(2, RoundingMode.HALF_UP);
        }
        purchaseValue = purchaseValue.add(marginIncreaseValue);
        BigDecimal finalValue = quantity.multiply(purchaseValue);
        BigDecimal discountValue = calculateDiscountValue(percentageDiscount, purchaseValue);
        finalValue = purchaseValue.subtract(discountValue);
    
        return finalValue.setScale(2, RoundingMode.HALF_UP);

    }
    //#endregion


    //#region calculate Unit Value
    /**
     * @return  Returns the BigDecimal number for the unit value.
     */
    public BigDecimal calculateUnitValue(BigDecimal purchaseValue, BigDecimal unitShippingValue, Integer margin, BigDecimal quantity){
        BigDecimal marginValue = calculateMargin(purchaseValue, margin);
        BigDecimal unitValue = purchaseValue;

        unitValue = unitValue.add(unitShippingValue);
       
        unitValue = unitValue.add(marginValue).setScale(2, RoundingMode.HALF_UP);
        
        return unitValue;
    }
    //#endregion

    //#region Complementary methods
    public BigDecimal calculateDiscountValue(Integer percentageDiscount, BigDecimal totalValue){
        BigDecimal discountValue = BigDecimal.valueOf(percentageDiscount)
            .divide(BigDecimal.valueOf(100))
            .multiply(totalValue);
        return discountValue.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMargin(BigDecimal purchaseValue, Integer incomingMargin){
        BigDecimal marginAddedValue = purchaseValue.multiply(BigDecimal.valueOf(incomingMargin).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
        return marginAddedValue;
    }

    public BigDecimal calculateServiceValue(BigDecimal purchaseValue, Integer discount){
        BigDecimal finalValue = purchaseValue;
        if(discount > 0){
            BigDecimal discountValue = BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100)).multiply(finalValue);
            finalValue = finalValue.subtract(discountValue);
        }

        return finalValue;
    }
    
    public BigDecimal calculatePartsValue(BigDecimal quantity, BigDecimal purchaseValue, BigDecimal unitShippingValue, BigDecimal marginValue, Integer percentageMargin) {
        purchaseValue = purchaseValue.add(marginValue);
        
        purchaseValue = purchaseValue.add(unitShippingValue);

        BigDecimal finalValue = quantity.multiply(purchaseValue);
    
        return finalValue;
    }

    public BigDecimal splitShippingCost(BigDecimal shippingValue, BigDecimal quantity){
        BigDecimal returnValue = shippingValue.divide(quantity);
    
        return returnValue;
    }

    //#endregion

    //#endregion
}
