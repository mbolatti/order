package com.tui.proof.model.dto.request;

import com.tui.proof.model.jpa.Client;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder(builderMethodName = "of")
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClientDTO extends CreateClientDTO {
    @Min(value = 0, message="The client id is mandatory")
    private Long id;
}
