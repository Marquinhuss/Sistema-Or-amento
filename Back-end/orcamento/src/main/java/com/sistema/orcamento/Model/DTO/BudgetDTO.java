package com.sistema.orcamento.Model.DTO;

import java.math.BigDecimal;
import java.util.List;

import com.sistema.orcamento.Model.Entity.Budget;
import com.sistema.orcamento.Model.Entity.Cost;

public record BudgetDTO(Long budgetId,
                           String clientName,
                           String vehicle,
                           String phone,
                           String plate,
                           String year,
                           BigDecimal partsCost,
                           BigDecimal servicesCost,
                           BigDecimal thirdPartyCost,
                           BigDecimal discountParts,
                           BigDecimal discountServices,
                           BigDecimal discountThirdParty,
                           BigDecimal totalBudgetCost,
                           BigDecimal totalDiscountedBudgetCost,
                           List<Cost> costList
                           ) {
    
    public static BudgetDTO fromEntity(Budget budget) {
        return new BudgetDTO(
            budget.getBudgetId(),
            budget.getClientName(),
            budget.getVehicle(),
            budget.getPhone(),
            budget.getPlate(),
            budget.getYear(),
            budget.getPartsCost(),
            budget.getServicesCost(),
            budget.getThirdPartyCost(),
            budget.getDiscountParts(),
            budget.getDiscountServices(),
            budget.getDiscountThirdParty(),
            budget.getTotalBudgetCost(),
            budget.getTotalDiscountedBudgetCost(),
            budget.getCostList()
        );
    }

    public Budget toEntity() {
        return new Budget(
            this.budgetId(),
            this.clientName(),
            this.vehicle(),
            this.phone(),
            this.plate(),
            this.year(),
            this.partsCost(),
            this.servicesCost(),
            this.thirdPartyCost(),
            this.discountParts(),
            this.discountServices(),
            this.discountThirdParty(),
            this.totalBudgetCost(),
            this.totalDiscountedBudgetCost(),
            this.costList()
        );
    }

}
