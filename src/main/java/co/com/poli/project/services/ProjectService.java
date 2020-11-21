package co.com.poli.project.services;

import co.com.poli.project.domain.Project;
import co.com.poli.project.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements  IProjectService{

    @Autowired
    private IProjectRepository iProjectRepository;

    @Override
    public Project createNewProject(Project project) {
        return iProjectRepository.save(project);
    }

    @Override
    public List<Project> getProjects() {
        return iProjectRepository.findAll();
    }
}
