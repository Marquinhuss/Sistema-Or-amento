package com.sistema.orcamento.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.orcamento.Model.Entity.Cost;

public interface CostRepository extends JpaRepository<Cost , Long>{
    
}
