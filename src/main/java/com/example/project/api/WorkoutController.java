package com.example.project.api;

import com.example.project.converter.Converter;
import com.example.project.dto.ClientDto;
import com.example.project.dto.InstructorDto;
import com.example.project.dto.WorkoutDto;
import com.example.project.service.WorkoutService;
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

    private final WorkoutService workoutService;
    private final Converter converter;

    @GetMapping("/workout/by_id/{id}")
    public ResponseEntity<WorkoutDto> getWorkoutById(@PathVariable long id) {
        return ResponseEntity.ok().body(workoutService.getById(id));
    }

    @PostMapping("/workout/save")
    public ResponseEntity<WorkoutDto> saveWorkoutToDatabase(@RequestBody WorkoutDto workoutDto) {
        URI uri =
                URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/workout/save").toUriString());
        return ResponseEntity.created(uri).body(workoutService.save(workoutDto));
    }

    @GetMapping("/workout/by_name")
    public ResponseEntity<WorkoutDto> getWorkoutByName(@RequestParam String name) {
        return ResponseEntity.ok().body(workoutService.getByName(name));
    }

    @PostMapping("/workout/delete/{id}")
    public ResponseEntity<WorkoutDto> makeWorkoutUnavailable(@PathVariable Long id) {
        return ResponseEntity.accepted().body(workoutService.deleteById(id));
    }

    @GetMapping("/workout/available")
    public ResponseEntity<List<WorkoutDto>> getAllAvailable() {
        return ResponseEntity.ok().body(workoutService.getAllAvailable());
    }

    @GetMapping("/workout/all")
    public ResponseEntity<List<WorkoutDto>> getAll() {
        return ResponseEntity.ok().body(workoutService.getAll());
    }

    @PatchMapping("/workout/update")
    public ResponseEntity<WorkoutDto> updateWorkoutById(@RequestParam(name = "id") Long id,
                                                        @RequestBody WorkoutDto workoutDto) throws JsonMappingException {
        WorkoutDto workoutReturned = workoutService.updateById(id, workoutDto);
        return ResponseEntity.accepted().body(workoutReturned);
    }

    @PatchMapping("/workout/delete_client")
    public ResponseEntity<ClientDto> deleteClientFromWorkout(@RequestParam(name = "workoutId") Long workoutId,
                                                             @RequestParam(name = "clientId") Long clientId) {
        ClientDto clientDto = workoutService.deleteClientFromWorkoutByWorkoutIdAndClientId(workoutId, clientId);
        return ResponseEntity.accepted().body(clientDto);
    }

    @PatchMapping("/workout/delete_instructor")
    public ResponseEntity<InstructorDto> deleteInstructorFromWorkout(@RequestParam(name = "workoutId") Long workoutId,
                                                                     @RequestParam(name = "instructorId") Long instructorId) {
        InstructorDto instructorDto =
                workoutService.deleteInstructorFromWorkoutByWorkoutIdAndInstructorId(workoutId, instructorId);
        return ResponseEntity.accepted().body(instructorDto);
    }

    @GetMapping("/workout/active_clients")
    public ResponseEntity<List<ClientDto>> showActiveClientsCounter(@RequestParam String name) {
        List<ClientDto> clients = workoutService.getActiveClientsByWorkoutName(name);
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/workout/active_instructors")
    public ResponseEntity<List<InstructorDto>> showActiveInstructorsCounter(@RequestParam String name) {
        List<InstructorDto> instructors = workoutService.getActiveInstructorsByWorkoutName(name);
        return ResponseEntity.ok().body(instructors);
    }
}
