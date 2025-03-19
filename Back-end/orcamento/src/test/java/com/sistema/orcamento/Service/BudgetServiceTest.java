package com.sistema.orcamento.Service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sistema.orcamento.Model.Entity.Budget;
import com.sistema.orcamento.Model.Entity.Cost;
import com.sistema.orcamento.Repository.BudgetRepository;
import com.sistema.orcamento.Utils.FileUtil;
import com.sistema.orcamento.Utils.MapperUtil;


@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {
    
    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private BudgetRepository repository;

    @Mock
    private CostService costService;

    @Test
    public void test() throws Exception{
        String productJson = FileUtil.readFromFileToString("/budget/budget.json");
        Budget budget = MapperUtil.deserialize(Budget.class, productJson);

        List<Cost> costList = costService.setCosts(budget.getCostList());
        System.out.println(costList);

    }
}
