package com.roomo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRequest {

    /**
     * The role to assign to the user. Must be either "lister" or "looker"
     */
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(lister|looker)$", message = "Role must be either 'lister' or 'looker'")
    private String role;
}
