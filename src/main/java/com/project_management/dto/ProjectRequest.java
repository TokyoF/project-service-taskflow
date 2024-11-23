
package com.project_management.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
  private String name;
  private String description;
  private String owner;
}
