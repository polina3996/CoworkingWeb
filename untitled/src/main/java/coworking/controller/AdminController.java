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
    //When the GET request to /addWorkspace is made, the controller sends an empty Workspace object to the view.
    //The JSP renders a form based on this empty object.
    public String showAddWorkspaceForm(Model model) {
        // model - Represents the Model interface provided by Spring MVC.
        // It acts as a container to store data that the controller sends to the view.
        model.addAttribute("workspace", new Workspace());//A method to add key-value
        // pairs to the model. The key is the name used in the view (e.g., JSP),
        // and the value is the actual object you want to pass.

        //"workspace" - This is the key (or name) by which the data is referenced in the view (JSP).
        //In the JSP, you can access this object using ${workspace} (e.g., in JSTL or form elements).
        //<form action="addWorkspace" method="post">
        //    <input type="text" name="type" value="${workspace.type}">
        //</form>

        //new Workspace() - This is the value (or the actual object) being passed.
        //A new, empty instance of the Workspace class is created and added to the model.
        //This object will be used in the JSP as a backing object for a form, meaning it will hold the user’s input data.
        return "addWorkspace";
    }

    @PostMapping("/addWorkspace")
    //When the user fills out the form and submits it, Spring automatically binds the form data
    // to the Workspace object using its property names (e.g., type and price).
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
            RedirectAttributes redirectAttributes//RedirectAttributes are used to pass messages (success or error) between requests during redirection.
    ) {
        Workspace workspaceToBeRemoved = this.workspaceRepository.findById(id);

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
            List<Reservation> reservations = this.reservationRepository.findReservations();
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
            Workspace workspace = workspaceRepository.findById(id);
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
        Workspace workspaceToBeUpdated = this.workspaceRepository.findById(id);

        if (workspaceToBeUpdated == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No coworking space found with ID: " + id);
            return "redirect:/updateWorkspace";
        }

        workspaceToBeUpdated.setType(type);
        workspaceToBeUpdated.setPrice(price);
        this.workspaceRepository.update(workspaceToBeUpdated);
        redirectAttributes.addFlashAttribute("successMessage",
                String.format("Coworking space with ID %d removed successfully.", id));

        return "updateConfirmation";
    }


    @PostMapping("/removeReservation")
    public String removeReservation(@RequestParam("id") int id,
                                    RedirectAttributes redirectAttributes) {
        try {
            Reservation reservationToBeRemoved = reservationRepository.findById(id);
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















