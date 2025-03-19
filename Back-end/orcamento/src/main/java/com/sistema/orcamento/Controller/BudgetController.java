package com.sistema.orcamento.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.orcamento.Model.Entity.Budget;
import com.sistema.orcamento.Model.DTO.BudgetDTO;
import com.sistema.orcamento.Service.BudgetService;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {
    
    @Autowired 
    private BudgetService service;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping()
    public ResponseEntity<List<BudgetDTO>> getAllBudgets(){
        List<BudgetDTO> budgetList = service.getAllBudgets();

        return new ResponseEntity<>(budgetList, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<BudgetDTO>> getBudgetById(@PathVariable Long id){
        BudgetDTO dto = service.getById(id);

        return new ResponseEntity<>(Optional.of(dto), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping()
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody Budget budget){
        System.out.println("..................................................................................................................................................................................................................."
        + "CHAMADA PARA API CREATE BUDGET ESTE É O ORÇAMENTO: " + budget);
        BudgetDTO dto = BudgetDTO.fromEntity(budget);
    
        // System.out.println("..................................................................................................................................................................................................................."
        // + "CHAMADA PARA API CREATE BUDGET ESTE É O ORÇAMENTO: " + dto);

        BudgetDTO returnBudget = service.createBudget(dto);
        System.out.println("..................................................................................................................................................................................................................."
        + "CHAMADA PARA API CREATE BUDGET ESTE É O ORÇAMENTO DEPOIS: " + returnBudget);
        return new ResponseEntity<>(returnBudget, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable Long id, @RequestBody Budget budget){
        BudgetDTO budgetDTO = BudgetDTO.fromEntity(budget);
        System.out.println("..................................................................................................................................................................................................................."
         + "CHAMADA PARA API UPDATE BUDGET ESTE É O ORÇAMENTO ANTES DO UPDATE: " + budgetDTO);
        budgetDTO = service.updateBudget(id, budgetDTO);
        System.out.println("..................................................................................................................................................................................................................."
         + "CHAMADA PARA API UPDATE BUDGET ESTE É O ORÇAMENTO DEPOIS DO UPDATE: " + budgetDTO);
        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetDTO> deleteBudget(@PathVariable Long id){
        BudgetDTO deletedDTO = service.getById(id);
        service.deleteBudget(id);
        return new ResponseEntity<>(deletedDTO, HttpStatus.OK);
    }

}
