package com.sistema.orcamento.Controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sistema.orcamento.Model.DTO.BudgetDTO;
import com.sistema.orcamento.Service.BudgetService;


@WebMvcTest(BudgetController.class)
public class BudgetControllerTest {
    

    @Autowired
    private BudgetController budgetController;

    @MockBean
    private BudgetService budgetService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
    }

    @Test
    public void must_return_200OK_get_all() throws Exception{
        List<BudgetDTO> budgetList = new ArrayList<>();
        
        var requestBuilder = MockMvcRequestBuilders.get("/api/budget");
        when(this.budgetService.getAllBudgets()).thenReturn(budgetList);

        this.mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void must_return_budget_get_by_id() throws Exception{
        List<BudgetDTO> budgetList = new ArrayList<>();
        
        var requestBuilder = MockMvcRequestBuilders.get("/api/budget");
        when(this.budgetService.getAllBudgets()).thenReturn(budgetList);

        this.mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

   
}
