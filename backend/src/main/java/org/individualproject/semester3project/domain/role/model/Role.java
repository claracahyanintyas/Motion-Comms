package org.individualproject.semester3project.domain.role.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @Data
@NoArgsConstructor @AllArgsConstructor
public class Role {
    Long id;
    String name;
}
