package com.example.project.api;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutClassDto;
import com.example.project.service.WorkoutClassService;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutClassService workoutClassService;
    private final Converter converter;

    @GetMapping("/workout/by_id/{id}")
    public ResponseEntity<WorkoutClassDto> getWorkoutById(@PathVariable long id) {
        return ResponseEntity.ok().body(workoutClassService.getById(id));
    }

    @PostMapping("/workout/save")
    public ResponseEntity<WorkoutClassDto> saveWorkoutToDatabase(@RequestBody WorkoutClassDto workoutClassDto) {
        URI uri =
                URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/workout/save").toUriString());
        return ResponseEntity.created(uri).body(workoutClassService.save(workoutClassDto));
    }

    @GetMapping("/workout/by_name")
    public ResponseEntity<WorkoutClassDto> getWorkoutByName(@RequestParam String name) {
        return ResponseEntity.ok().body(workoutClassService.getByName(name));
    }

    @PostMapping("/workout/delete/{id}")
    public ResponseEntity<WorkoutClassDto> makeWorkoutUnavailable(@PathVariable Long id) {
        return ResponseEntity.accepted().body(workoutClassService.deleteById(id));
    }

    @GetMapping("/workout/available")
    public ResponseEntity<List<WorkoutClassDto>> getAllAvailable() {
        return ResponseEntity.ok().body(workoutClassService.getAllAvailable());
    }

    @GetMapping("/workout/all")
    public ResponseEntity<List<WorkoutClassDto>> getAll() {
        return ResponseEntity.ok().body(workoutClassService.getAll());
    }

    @PatchMapping("/workout/update")
    public ResponseEntity<WorkoutClassDto> updateWorkoutById(@RequestParam(name = "id") Long id,
                                                             @RequestBody WorkoutClassDto workoutClassDto) throws JsonMappingException {
        WorkoutClassDto workoutReturned = workoutClassService.updateById(id, workoutClassDto);
        return ResponseEntity.accepted().body(workoutReturned);
    }

    @PatchMapping("/workout/delete_client")
    public ResponseEntity<ClientDto> deleteClientFromWorkout(@RequestParam(name = "workoutId") Long workoutId,
                                                             @RequestParam(name = "clientId") Long clientId) {
        ClientDto clientDto = workoutClassService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId);
        return ResponseEntity.accepted().body(clientDto);
    }

//    @PatchMapping("/workout/delete_instructor")
//    public ResponseEntity<InstructorDto> deleteInstructorFromWorkout(@RequestParam(name = "workoutId") Long workoutId,
//                                                                     @RequestParam(name = "instructorId") Long instructorId) {
//        InstructorDto instructorDto =
//                workoutClassService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId);
//        return ResponseEntity.accepted().body(instructorDto);
//    }

    @GetMapping("/workout/active_clients")
    public ResponseEntity<List<ClientDto>> showActiveClientsCounter(@RequestParam String name) {
        List<ClientDto> clients = workoutClassService.getActiveClientsByWorkoutName(name);
        return ResponseEntity.ok().body(clients);
    }

//    @GetMapping("/workout/active_instructors")
//    public ResponseEntity<List<InstructorDto>> showActiveInstructorsCounter(@RequestParam String name) {
//        List<InstructorDto> instructors = workoutClassService.getActiveInstructorsByWorkoutName(name);
//        return ResponseEntity.ok().body(instructors);
//    }
}
