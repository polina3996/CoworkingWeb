package coworking.controller;

import coworking.dto.ReservationForm;
import coworking.model.Reservation;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Browse available spaces, make reservations and cancel bookings
 * - select a workspace
 * - enter booking details(ex. name, date, time(start/end))
 * - store the reservation details in memory
 * - see their reservations and cancel them by selecting the reservation ID
 */

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final WorkspaceRepository workspaceRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

    @Autowired
    public CustomerController(WorkspaceRepository workspaceRepository, ReservationRepository reservationRepository, ReservationService reservationService, UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }


    @GetMapping("/browseAvailableWorkspaces")
    public ResponseEntity<List<Workspace>> browseAvailableSpaces() {
        List<Workspace> availableWorkspaces = this.workspaceRepository.findByAvailabilityStatusTrue();
        return ResponseEntity.ok(availableWorkspaces);
    }


    @PostMapping("/makeAReservation")
    public ResponseEntity<?> makeAReservation(@RequestBody ReservationForm reservationForm) {
        try {
            Workspace workspace = this.workspaceRepository.findById(reservationForm.getWorkspaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid workspace ID"));

            String userName = reservationForm.getUserName();
            LocalDate startDate = reservationForm.getStartDate();
            LocalDate endDate = reservationForm.getEndDate();
            if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Invalid reservation dates.");
            }

            this.reservationService.makeReservation(workspace, userName, startDate, endDate);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Reservation made successfully!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/myReservations")
    public ResponseEntity<?> viewMyReservations(@RequestParam("name") String name) {
        List<Reservation> myReservations = this.reservationRepository.findByUser_Name(name);

        if (myReservations == null || myReservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No reservations for user: " + name);
        }

        return ResponseEntity.ok(myReservations);
    }


    @PostMapping("/cancelMyReservation")
    public ResponseEntity<?> removeReservation(@RequestParam("id") int id, @RequestParam("name") String name) {
        Reservation reservationToBeRemoved = this.reservationRepository.findById(id)
                .orElse(null);
        if (reservationToBeRemoved == null || !Objects.equals(reservationToBeRemoved.getUser().getName(), name)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No reservation found with ID: " + id);
        }
        this.reservationService.removeReservation(reservationToBeRemoved);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Reservation with ID " + id + " removed successfully.");
    }
}