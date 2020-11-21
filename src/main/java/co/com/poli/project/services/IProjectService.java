package co.com.poli.project.services;

import co.com.poli.project.domain.Project;

import java.util.List;

public interface IProjectService {
    Project createNewProject(Project project);

    List<Project> getProjects();
}
