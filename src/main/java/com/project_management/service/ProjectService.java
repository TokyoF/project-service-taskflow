
package com.project_management.service;

import com.project_management.events.ProjectEvent;
import com.project_management.utils.JsonUtils;
import com.project_management.dto.ProjectRequest;
import com.project_management.dto.ProjectResponse;
import com.project_management.exception.ProjectNotFoundException;
import com.project_management.model.ProjectEntity;
import com.project_management.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final KafkaTemplate<String, String> kafkaTemplate;

  /**
   * Crea un nuevo proyecto.
   * 
   * @param request Datos del proyecto.
   * @return Proyecto creado.
   */
  public ProjectResponse createProject(ProjectRequest request) {
    ProjectEntity project = ProjectEntity.builder()
        .name(request.getName())
        .description(request.getDescription())
        .owner(request.getOwner())
        .active(true)
        .build();

    ProjectEntity savedProject = projectRepository.save(project);
    this.kafkaTemplate.send("project-topic",
        JsonUtils.toJson(new ProjectEvent(savedProject.getId(), savedProject.getName(), savedProject.getOwner(),
            savedProject.isActive())));

    return convertToResponse(savedProject);
  }

  /**
   * Obtiene todos los proyectos.
   * 
   * @return Lista de proyectos.
   */
  public List<ProjectResponse> getAllProjects() {
    return projectRepository.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  /**
   * Obtiene un proyecto por ID.
   * 
   * @param id Identificador del proyecto.
   * @return Proyecto encontrado.
   * @throws ProjectNotFoundException Si el proyecto no existe.
   */
  public ProjectResponse getProjectById(Long id) {
    ProjectEntity project = projectRepository.findById(id)
        .orElseThrow(() -> new ProjectNotFoundException("Proyecto no encontrado con ID: " + id));
    return convertToResponse(project);
  }

  /**
   * Actualiza un proyecto existente.
   * 
   * @param id      Identificador del proyecto.
   * @param request Datos a actualizar.
   * @return Proyecto actualizado.
   * @throws ProjectNotFoundException Si el proyecto no existe.
   */
  public ProjectResponse updateProject(Long id, ProjectRequest request) {
    ProjectEntity project = projectRepository.findById(id)
        .orElseThrow(() -> new ProjectNotFoundException("Proyecto no encontrado con ID: " + id));

    project.setName(request.getName());
    project.setDescription(request.getDescription());
    project.setOwner(request.getOwner());

    ProjectEntity updatedProject = projectRepository.save(project);

    return convertToResponse(updatedProject);
  }

  /**
   * Elimina un proyecto por ID.
   * 
   * @param id Identificador del proyecto.
   * @throws ProjectNotFoundException Si el proyecto no existe.
   */
  public void deleteProject(Long id) {
    if (!projectRepository.existsById(id)) {
      throw new ProjectNotFoundException("Proyecto no encontrado con ID: " + id);
    }
    projectRepository.deleteById(id);
  }

  /**
   * Convierte una entidad ProjectEntity a un DTO ProjectResponse.
   * 
   * @param project Entidad del proyecto.
   * @return DTO de respuesta.
   */
  private ProjectResponse convertToResponse(ProjectEntity project) {
    return ProjectResponse.builder()
        .id(project.getId())
        .name(project.getName())
        .description(project.getDescription())
        .owner(project.getOwner())
        .active(project.isActive())
        .build();
  }
}
