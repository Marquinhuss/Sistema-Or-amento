package com.sistema.orcamento.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.orcamento.Model.Entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long>{

}
