package co.com.poli.project.controllers;

import co.com.poli.project.domain.Project;
import co.com.poli.project.model.ErrorMessage;
import co.com.poli.project.services.IProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProjectController {

    @Autowired
    private IProjectService iProjectService;

    @PostMapping(value = "/project")
    public ResponseEntity<Project> addNewProject(@Valid @RequestBody Project project, BindingResult result){

        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
        }

        try{
            Project projectDB = iProjectService.createNewProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(projectDB);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Llave duplicada: el projectName y projectIdentifier deben ser valores únicos");
        }

    }

    @GetMapping(value = "/project")
    public ResponseEntity<?> showAllProjects(){
        List<Project> projectsDB = iProjectService.getProjects();

        if (projectsDB.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("La lista de proyectos está vacia");
        }
        return ResponseEntity.status(HttpStatus.OK).body(projectsDB);
    }


    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err -> { // mapeamos la lista de errores
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage()); // obtenemos el tipo de error y el mensaje
                    return error;
                }).collect(Collectors.toList()); // como retorna un string toca convertirlo a una lista

        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
