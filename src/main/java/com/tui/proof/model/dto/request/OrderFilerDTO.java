package com.tui.proof.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@NoArgsConstructor
@Data
@AllArgsConstructor(staticName="of")
public class OrderFilerDTO {
    private String firstName;
    private String lastName;
    private String email;

    public boolean isEmpty(){
        return ObjectUtils.isEmpty(firstName) && ObjectUtils.isEmpty(lastName) && ObjectUtils.isEmpty(email);
    }
}
