
package com.project_management.exception;

public class ProjectNotFoundException extends RuntimeException {
  public ProjectNotFoundException(String message) {
    super(message);
  }
}