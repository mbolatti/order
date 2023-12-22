package com.tui.proof.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetClientDTO {
  private Long id;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String phoneNumber;
}
