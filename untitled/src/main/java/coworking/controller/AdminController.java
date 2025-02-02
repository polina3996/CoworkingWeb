package coworking.controller;

import coworking.CheckEmptinessException;
import coworking.CheckMethods;
import coworking.model.Reservation;
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
import java.util.List;


/**
 * Manages spaces and view all bookings
 * Add, remove or update coworking spaces
 */

@Controller
@RequestMapping("/admin")
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


    @GetMapping("/addWorkspace")
    public String showAddWorkspaceForm(Model model) {
        model.addAttribute("workspace", new Workspace());
        return "addWorkspace";
    }

    @PostMapping("/addWorkspace")
    public String addWorkspace(@ModelAttribute("workspace") Workspace workspace, Model model) {
        try {
            this.workspaceRepository.save(workspace);
            model.addAttribute("message", "Workspace added successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error adding workspace: " + e.getMessage());
        }
        return "addWorkspace";
    }



    @GetMapping("/removeWorkspace")
    public String showRemoveWorkspacePage(Model model) {
        List<Workspace> workspaces = this.workspaceRepository.findAll();
        model.addAttribute("workspaces", workspaces);
        return "removeWorkspace";
    }

    @PostMapping("/removeWorkspace")
    public String removeCoworkingSpace(
            @RequestParam("id") int id,
            RedirectAttributes redirectAttributes
    ) {
        Workspace workspaceToBeRemoved = this.workspaceRepository.findById(id)
                .orElse(null);

        if (workspaceToBeRemoved == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No coworking space found with ID: " + id);
            return "redirect:/removeWorkspace";
        }

        this.workspaceRepository.delete(workspaceToBeRemoved);
        redirectAttributes.addFlashAttribute("successMessage",
                String.format("Coworking space with ID %d removed successfully.", id));

        return "deleteConfirmation";
    }

    @GetMapping("/viewReservations")
    public String viewAllReservations(Model model) {
        try {
            List<Reservation> reservations = this.reservationRepository.findAll();
            CheckMethods.checkEmptiness(reservations, "reservations");
            model.addAttribute("reservations", reservations);
            return "viewReservations";
        } catch (CheckEmptinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }


    @GetMapping("/updateWorkspace")
    public String showUpdateWorkspacePage(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            Workspace workspace = workspaceRepository.findById(id)
                    .orElse(null);
            if (workspace == null) {
                model.addAttribute("errorMessage", "No coworking space found with ID: " + id);
            } else {
                model.addAttribute("workspace", workspace);
            }
        } else {
            List<Workspace> workspaces = workspaceRepository.findAll();
            model.addAttribute("workspaces", workspaces);
        }
        return "updateWorkspace";
    }

    @PostMapping("/updateWorkspace")
    public String updateCoworkingSpace(
            @RequestParam("id") int id,
            @RequestParam("type") String type,
            @RequestParam("price") double price,
            RedirectAttributes redirectAttributes) {
        Workspace workspaceToBeUpdated = this.workspaceRepository.findById(id)
                .orElse(null);

        if (workspaceToBeUpdated == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No coworking space found with ID: " + id);
            return "redirect:/updateWorkspace";
        }

        workspaceToBeUpdated.setType(type);
        workspaceToBeUpdated.setPrice(price);
        this.workspaceRepository.save(workspaceToBeUpdated);
        redirectAttributes.addFlashAttribute("successMessage",
                String.format("Coworking space with ID %d removed successfully.", id));

        return "updateConfirmation";
    }


    @PostMapping("/removeReservation")
    public String removeReservation(@RequestParam("id") int id,
                                    RedirectAttributes redirectAttributes) {
        try {
            Reservation reservationToBeRemoved = reservationRepository.findById(id)
                    .orElse(null);
            if (reservationToBeRemoved == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "No reservations found with ID: " + id);
                return "redirect:/admin/viewReservations";
            }
            this.reservationService.removeReservation(reservationToBeRemoved);

            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Reservation with ID %d removed successfully.", id));
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error while removing the reservation: " + e.getMessage());
            return "redirect:/admin/viewReservations";
        }

        return "deleteConfirmation";
    }

}















