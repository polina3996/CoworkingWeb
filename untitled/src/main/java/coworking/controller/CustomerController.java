package coworking.controller;

import coworking.dto.ReservationForm;
import coworking.model.Reservation;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserEntityRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public CustomerController(WorkspaceRepository workspaceRepository, ReservationRepository reservationRepository, ReservationService reservationService, UserEntityRepository userEntityRepository) {
        this.workspaceRepository = workspaceRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.userEntityRepository = userEntityRepository;
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

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You must be logged in to make a reservation.");
            }
            String userName = authentication.getName(); //get user's name from current session
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

    @GetMapping("/myReservations")
    public ResponseEntity<?> viewMyReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You must be logged in to make a reservation.");
        }
        String userName = authentication.getName();
        List<Reservation> myReservations = this.reservationRepository.findByUser_Name(userName);

        if (myReservations == null || myReservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No reservations for user: " + userName);
        }

        return ResponseEntity.ok(myReservations);
    }


    @PostMapping("/cancelMyReservation")
    public ResponseEntity<?> removeReservation(@RequestParam("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You must be logged in to make a reservation.");
        }
        String userName = authentication.getName();
        Reservation reservationToBeRemoved = this.reservationRepository.findById(id)
                .orElse(null);
        if (reservationToBeRemoved == null || !Objects.equals(reservationToBeRemoved.getUser().getName(), userName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No reservation found with ID: " + id);
        }
        this.reservationService.removeReservation(reservationToBeRemoved);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Reservation with ID " + id + " removed successfully.");
    }
}