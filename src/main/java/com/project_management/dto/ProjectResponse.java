
package com.project_management.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
  private Long id;
  private String name;
  private String description;
  private String owner;
  private boolean active;
}
