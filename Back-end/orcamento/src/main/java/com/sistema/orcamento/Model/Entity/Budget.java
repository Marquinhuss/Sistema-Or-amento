package com.sistema.orcamento.Model.Entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "budget")
@Table(name = "Budgets")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long budgetId;
    
    private String clientName;

    @Column(nullable = false)
    private String vehicle;

    private String phone;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private String year;

    //CUSTOS UNITÁRIOS
    private BigDecimal partsCost;

    private BigDecimal servicesCost;

    private BigDecimal thirdPartyCost;
    
    //DESCONTOS UNITÁRIOS
    private BigDecimal discountParts;

    private BigDecimal discountServices;

    private BigDecimal discountThirdParty;

    //CUSTOS TOTAIS
    private BigDecimal totalBudgetCost;

    private BigDecimal totalDiscountedBudgetCost;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "costList")
    private List<Cost> costList;


    /* @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "custoTerceiros")
    private List<Cost> thirdPartyCost;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "custoServicos")
    private List<Cost> servicesCost; */
}
