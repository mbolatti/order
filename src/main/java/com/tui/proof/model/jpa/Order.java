package com.tui.proof.model.jpa;

import com.tui.proof.model.dto.request.CreateOrderDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_order")
@Data
@NoArgsConstructor
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  private String deliveryAddress;

  private int quantityPilotes;

  private double unitPrice;

  @Temporal(TemporalType.TIMESTAMP)
  private Date orderDate;
}
