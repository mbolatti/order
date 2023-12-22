package com.tui.proof.model.dto.request;

import com.tui.proof.model.jpa.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientDTO {

  @NotEmpty(message = "The first name is mandatory")
  protected String firstName;
  @NotEmpty(message = "The last name is mandatory")
  protected String lastName;
  @Email(message = "Email must be valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  protected String emailAddress;
  @Pattern(regexp = "^\\d{9}$", message = "Phone number must contain 9 digits")
  protected String phoneNumber;

}
