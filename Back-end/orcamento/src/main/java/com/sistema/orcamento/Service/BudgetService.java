package com.sistema.orcamento.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sistema.orcamento.Model.Enum.CostType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.orcamento.Model.DTO.BudgetDTO;
import com.sistema.orcamento.Model.Entity.Budget;
import com.sistema.orcamento.Model.Entity.Cost;
import com.sistema.orcamento.Repository.BudgetRepository;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository repository;

    @Autowired
    private CostService costService;

    public Boolean verifyBudget(Long id) {
        Optional<Budget> budget = repository.findById(id);
        if (budget.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<BudgetDTO> getAllBudgets() {
        List<Budget> budgetEntityList = repository.findAll();
        List<BudgetDTO> budgetDTOList = new ArrayList<>();
        for (Budget b : budgetEntityList) {
            BudgetDTO dto = transformToBudgetDTO(b);
            budgetDTOList.add(dto);
        }
        return budgetDTOList;
    }

    public BudgetDTO getById(Long id) {
        Optional<Budget> budget = repository.findById(id);
        if (verifyBudget(id)) {
            // throw new BudgetNotFoundException("O orçamento com id " + id + " não foi
            // encontrado");
        }
        Budget entity = budget.get();
        BudgetDTO dto = transformToBudgetDTO(entity);
        return dto;
    }

    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        Budget budget = transformToBudget(budgetDTO);
        List<Cost> costList = costService.setCosts(budgetDTO.costList());
        budget.setCostList(costList);

        BigDecimal totalValue = calculateTotalBudgetValue(budget.getCostList());
        BigDecimal partsCost = calculatesPartsCost(costList);
        BigDecimal servicesCost = calculatesServiceCost(costList);
        BigDecimal thirdPartyCost = calculatesThirdPartyCost(costList);
        budget.setPartsCost(partsCost);

        System.out.println("ServicosCusto");

        budget.setServicesCost(servicesCost);
        budget.setThirdPartyCost(thirdPartyCost);

        budget = setDiscounts(budget);

        BigDecimal totalDiscounted = budget.getTotalDiscountedBudgetCost();
        if(totalDiscounted.equals(BigDecimal.ZERO)){
            totalDiscounted = totalValue;
            budget.setTotalDiscountedBudgetCost(totalDiscounted);
        }

        budget.setTotalBudgetCost(totalValue);
        budget = repository.save(budget);

        BudgetDTO dto = transformToBudgetDTO(budget);
        return dto;
    }

    public BudgetDTO updateBudget(Long id, BudgetDTO budgetDTO) {
        if (verifyBudget(id)) {
            // throw new BudgetNotFoundException("O orçamento com id " + id + " não foi
            // encontrado");
        }
        Budget budget = budgetDTO.toEntity();
        budget.setBudgetId(id);
        BudgetDTO dto = transformToBudgetDTO(budget);

        dto = createBudget(dto);

        return dto;
    }

    public String deleteBudget(Long id) {
        repository.deleteById(id);
        return "Orçamento Deletado com sucesso!";
    }

    public BigDecimal calculateTotalBudgetValue(List<Cost> costList) {
        BigDecimal totalBudgetValue = new BigDecimal(0);
        for (Cost cost : costList) {
            BigDecimal totalCostValue = cost.getTotalValue();
            totalBudgetValue = totalBudgetValue.add(totalCostValue);
        }
        return totalBudgetValue;
    }

    public BigDecimal calculatesPartsCost(List<Cost> costList) {
        BigDecimal partsCost = new BigDecimal(0);
        for (Cost cost : costList) {
            if (cost.getCostType().equals(CostType.PECA)) {
                BigDecimal totalCostValue = cost.getTotalValue();
                partsCost = partsCost.add(totalCostValue);
            }
        }
        return partsCost;
    }

    public BigDecimal calculatesServiceCost(List<Cost> costList) {
        BigDecimal servicesCost = new BigDecimal(0);
        for (Cost cost : costList) {
            if (cost.getCostType().equals(CostType.SERVICO)) {
                BigDecimal totalCostValue = cost.getTotalValue();
                servicesCost = servicesCost.add(totalCostValue);
            }
        }
        return servicesCost;
    }

    public BigDecimal calculatesThirdPartyCost(List<Cost> costList) {
        BigDecimal thirdPartyCost = new BigDecimal(0);
        for (Cost cost : costList) {
            if (cost.getCostType().equals(CostType.SERV_TERCEIROS)) {
                BigDecimal totalCostValue = cost.getTotalValue();
                thirdPartyCost = thirdPartyCost.add(totalCostValue);
            }
        }
        return thirdPartyCost;
    }

    public Budget transformToBudget(BudgetDTO dto) {
        // Budget budget = new Budget(dto.budgetId(), dto.clientName(), dto.vehicle(),
        // dto.phone(), dto.plate(), dto.year(), dto.partsCost(),
        // dto.servicesCost(),dto.thirdPartyCost(), dto.discountParts()
        // ,dto.discountServices() ,dto.discountThirdParty() , dto.totalBudgetCost(),
        // dto.totalDiscountedBudgetCost(), dto.discountThirdParty(), dto.costList());
        return dto.toEntity();
    }

    public BudgetDTO transformToBudgetDTO(Budget entity) {
        BudgetDTO dto = BudgetDTO.fromEntity(entity);
        return dto;
    }

    public Budget setDiscounts(Budget entity) {
        BigDecimal partsDiscount = entity.getDiscountParts();
        BigDecimal servicesDiscount = entity.getDiscountServices();
        BigDecimal thirdPartyDiscount = entity.getDiscountThirdParty();
        BigDecimal discountValue = new BigDecimal(0);
        BigDecimal totalDiscountedValue = entity.getTotalDiscountedBudgetCost();
        BigDecimal partsCost = entity.getPartsCost();
        BigDecimal servicesCost = entity.getServicesCost();
        BigDecimal thirdPartyCost = entity.getThirdPartyCost();

        if (partsDiscount == null) {
            partsDiscount = new BigDecimal(0);
        }
        if (servicesDiscount == null) {
            servicesDiscount = new BigDecimal(0);
        }
        if (thirdPartyDiscount == null) {
            thirdPartyDiscount = new BigDecimal(0);
        }
        if (partsDiscount.compareTo(BigDecimal.ONE) > 0) {
            partsCost = entity.getPartsCost();
            BigDecimal partsDiscountedValue = partsCost.subtract(partsDiscount
                                                        .divide(BigDecimal.valueOf(100))
                                                        .multiply(partsCost));
            totalDiscountedValue = totalDiscountedValue.add(partsDiscountedValue);
            System.out.println("Valor descontado após aplicar desconto de pela" + totalDiscountedValue);
            discountValue = partsCost.subtract(partsDiscountedValue);
            System.out.println("Valor total do desconto " + discountValue);
            entity.setPartsCost(partsDiscountedValue);
        }
        if (servicesDiscount.compareTo(BigDecimal.ONE) > 0) {
            servicesCost = entity.getServicesCost();
            BigDecimal servicesDiscountedValue = servicesCost.subtract(servicesDiscount
                                                            .divide(BigDecimal.valueOf(100))
                                                            .multiply(servicesCost));
            totalDiscountedValue = totalDiscountedValue.add(servicesDiscountedValue);
            BigDecimal discountServicesValue = servicesCost.subtract(servicesDiscountedValue);
            discountValue = discountValue.add(discountServicesValue);
            entity.setServicesCost(servicesDiscountedValue);
        }
        if (thirdPartyDiscount.compareTo(BigDecimal.ONE) > 0) {
            thirdPartyCost = entity.getThirdPartyCost();
            BigDecimal thirdPartyDiscountedValue = thirdPartyCost.subtract(thirdPartyDiscount
                                                                .divide(BigDecimal.valueOf(100))
                                                                .multiply(thirdPartyCost));
            totalDiscountedValue = totalDiscountedValue.add(thirdPartyDiscountedValue);
            BigDecimal discountThirdPartyValue = thirdPartyCost.subtract(thirdPartyDiscountedValue);
            discountValue = discountValue.add(discountThirdPartyValue);
            entity.setThirdPartyCost(thirdPartyDiscountedValue);
        }
        if(partsDiscount.compareTo(BigDecimal.ZERO) == 0){
            totalDiscountedValue = totalDiscountedValue.add(partsCost);
        }
        if (servicesDiscount.compareTo(BigDecimal.ZERO) == 0) {
            totalDiscountedValue = totalDiscountedValue.add(servicesCost);
        }
        if(thirdPartyDiscount.compareTo(BigDecimal.ZERO) == 0){
            totalDiscountedValue = totalDiscountedValue.add(thirdPartyCost);
        }
        entity.setTotalDiscountedBudgetCost(totalDiscountedValue);
        System.out.println("..................................................................................................................................................................................................................."
                + "ESTE É O VALOR DO DESCONTO TOTAL: " + discountValue);
        System.out.println(" TotalDiscountedBudgetCost: " + entity.getTotalDiscountedBudgetCost());
        return entity;
    }

}
