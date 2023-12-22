package com.tui.proof.repository;

import com.tui.proof.model.jpa.Client;
import com.tui.proof.model.jpa.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
