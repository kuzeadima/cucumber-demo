package com.thekuzea.experimental.domain.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RoleResource {

    String id;

    String name;
}
