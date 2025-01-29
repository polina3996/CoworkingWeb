package coworking.controller;

import coworking.CheckEmptinessException;
import coworking.CheckMethods;
import coworking.dto.ReservationForm;
import coworking.model.Reservation;
import coworking.model.User;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Browse available spaces, make reservations and cancel bookings
 * - select a workspace
 * - enter booking details(ex. name, date, time(start/end))
 * - store the reservation details in memory
 * - see their reservations and cancel them by selecting the reservation ID
 */



@Controller
@RequestMapping("/customer")
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
    public String browseAvailableSpaces(Model model) {
        try{
            List<Workspace> availableWorkspaces = this.workspaceRepository.findAvailableWorkspaces();
            model.addAttribute("availableWorkspaces", availableWorkspaces);
            return "browseAvailableWorkspaces";
        }
        catch (NullPointerException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
    }
    }

    @GetMapping("/makeAReservation")
    public String makeAReservation(Model model){
        List<Workspace> availableWorkspaces = workspaceRepository.findAvailableWorkspaces();
        model.addAttribute("availableWorkspaces", availableWorkspaces);
        model.addAttribute("reservationForm", new ReservationForm());
        return "makeAReservation";
    }


    @PostMapping("/makeAReservation")
    public String makeAReservation(@ModelAttribute ReservationForm reservationForm, Model model) {
        try {
            Workspace workspace = this.workspaceRepository.findById(reservationForm.getWorkspaceId());
            if (workspace == null) {
                throw new IllegalArgumentException("Invalid workspace ID");
            }
            String userName = reservationForm.getUserName();
            LocalDate startDate = reservationForm.getStartDate();
            LocalDate endDate = reservationForm.getEndDate();

            this.reservationService.makeReservation(this.userRepository, workspace, userName, startDate, endDate);

            model.addAttribute("message", "Reservation made successfully!");
            return "reservationConfirmation";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error making reservation: " + e.getMessage());
            return "error";
        }
    }


    @GetMapping("/myReservations")
        public String askForName(Model model){
            List<User> users = this.userRepository.findAll();
            model.addAttribute("users", users);
            return "selectUser";
    }

    //method will receive selected user's name
    @PostMapping("/myReservations")
    public String viewMyReservations(@RequestParam("name") String name, Model model) {
        try {
            List<Reservation> myReservations = this.reservationRepository.findMyReservations(name);

            if (myReservations == null || myReservations.isEmpty()) {
                model.addAttribute("message", "You have no reservations yet.");
                return "myReservations";
            }

            model.addAttribute("reservations", myReservations);
            model.addAttribute("userName", name);

            return "myReservations";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while retrieving your reservations. Please try again.");
            return "error";
        }
    }


// ADD IT WITH LOGIN
//    @GetMapping("/myReservations")
//    public String viewMyReservations(@RequestParam("name") String name, Model model) {
//        try {
//            List<Reservation> myReservations = reservationRepository.findMyReservations(name);
//
//            if (myReservations == null || myReservations.isEmpty()) {
//                model.addAttribute("message", "You have no reservations yet.");
//                return "reservations/myReservations";
//            }
//
//            model.addAttribute("reservations", myReservations);
//            model.addAttribute("userName", name);
//        } catch (Exception e) {
//            model.addAttribute("error", "An error occurred while retrieving your reservations. Please try again.");
//        }
//
//        return "myReservations"; // JSP page to display reservations
//    }

    @PostMapping("/cancelMyReservation")
    public String removeReservation(@RequestParam("id") int id,
                                    RedirectAttributes redirectAttributes) {
        try {
            Reservation reservationToBeRemoved = reservationRepository.findById(id);
            if (reservationToBeRemoved == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "No reservations found with ID: " + id);
                return "redirect:/customer/myReservations";
            }
            this.reservationService.removeReservation(reservationToBeRemoved);

            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Reservation with ID %d removed successfully.", id));
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error while removing the reservation: " + e.getMessage());
            return "redirect:/customer/myReservations";
        }

        return "deleteConfirmation";
    }
}



