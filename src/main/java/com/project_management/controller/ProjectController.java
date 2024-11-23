
package com.project_management.controller;

import com.project_management.dto.ProjectRequest;
import com.project_management.dto.ProjectResponse;
import com.project_management.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
@Validated
public class ProjectController {

  private final ProjectService projectService;

  /**
   * Crea un nuevo proyecto.
   * 
   * @param request DTO con datos del proyecto.
   * @return Proyecto creado.
   */
  @PostMapping
  public ResponseEntity<ProjectResponse> createProject(@RequestBody @Validated ProjectRequest request) {
    ProjectResponse project = projectService.createProject(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(project);
  }

  /**
   * Obtiene todos los proyectos.
   * 
   * @return Lista de proyectos.
   */
  @GetMapping
  public ResponseEntity<List<ProjectResponse>> getAllProjects() {
    List<ProjectResponse> projects = projectService.getAllProjects();
    return ResponseEntity.ok(projects);
  }

  /**
   * Obtiene un proyecto por su ID.
   * 
   * @param id Identificador del proyecto.
   * @return Proyecto encontrado.
   */
  @GetMapping("/{id}")
  public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
    ProjectResponse project = projectService.getProjectById(id);
    return ResponseEntity.ok(project);
  }

  /**
   * Actualiza un proyecto existente.
   * 
   * @param id      Identificador del proyecto.
   * @param request DTO con datos a actualizar.
   * @return Proyecto actualizado.
   */
  @PutMapping("/{id}")
  public ResponseEntity<ProjectResponse> updateProject(
      @PathVariable Long id, @RequestBody @Validated ProjectRequest request) {
    ProjectResponse project = projectService.updateProject(id, request);
    return ResponseEntity.ok(project);
  }

  /**
   * Elimina un proyecto por su ID.
   * 
   * @param id Identificador del proyecto.
   * @return Respuesta sin contenido.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
    projectService.deleteProject(id);
    return ResponseEntity.noContent().build();
  }
}
