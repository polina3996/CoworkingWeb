package coworking.controller;

import coworking.model.Reservation;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Manages spaces and view all bookings
 * Add, remove or update coworking spaces
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final WorkspaceRepository workspaceRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final UserRepository userRepository;


    @Autowired
    public AdminController(WorkspaceRepository workspaceRepository, ReservationRepository reservationRepository, ReservationService reservationService, UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }


    @GetMapping("/viewWorkspaces")
    public ResponseEntity<List<Workspace>> viewAllWorkspaces() {
        List<Workspace> workspaces = this.workspaceRepository.findAll();
        return ResponseEntity.ok(workspaces);
    }

    @PostMapping("/addWorkspace")
    public ResponseEntity<Workspace> addWorkspace(@RequestBody Workspace workspace) {
        Workspace savedWorkspace = this.workspaceRepository.save(workspace);
        return ResponseEntity.ok(savedWorkspace);
    }

    @PostMapping("/removeWorkspace")
    public ResponseEntity<?> removeCoworkingSpace(
            @RequestParam("id") int id) {
        Workspace workspaceToBeRemoved = this.workspaceRepository.findById(id)
                .orElse(null);

        if (workspaceToBeRemoved == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No coworking space found with ID: " + id);
        }

        this.workspaceRepository.delete(workspaceToBeRemoved);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Coworking space with ID " + id + " removed successfully.");
    }
    @GetMapping("/viewReservations")
    public ResponseEntity<List<Reservation>> viewAllReservations() {
        List<Reservation> reservations = this.reservationRepository.findAll();
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/updateWorkspace")
    public ResponseEntity<?> updateCoworkingSpace(
            @RequestParam("id") int id,
            @RequestParam("type") String type,
            @RequestParam("price") double price) {
        Workspace workspaceToBeUpdated = this.workspaceRepository.findById(id)
                .orElse(null);

        if (workspaceToBeUpdated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No coworking space found with ID: " + id);
        }

        workspaceToBeUpdated.setType(type);
        workspaceToBeUpdated.setPrice(price);
        this.workspaceRepository.save(workspaceToBeUpdated);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Coworking space with ID " + id + " updated successfully.");
    }

    @PostMapping("/removeReservation")
    public ResponseEntity<?> removeReservation(@RequestParam("id") int id) {
        Reservation reservationToBeRemoved = reservationRepository.findById(id)
                .orElse(null);
        if (reservationToBeRemoved == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No reservation found with ID: " + id);
        }
        this.reservationService.removeReservation(reservationToBeRemoved);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Reservation with ID " + id + " removed successfully.");
    }
}















