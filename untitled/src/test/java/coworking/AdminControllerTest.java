package coworking;

import coworking.config.JwtUtil;
import coworking.controller.AdminController;
import coworking.model.Reservation;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserEntityRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

@WebMvcTest(AdminController.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkspaceRepository workspaceRepository;

    @MockitoBean
    private ReservationRepository reservationRepository;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private AdminController adminController;

    private Workspace workspace = new Workspace();
    private Reservation reservation = new Reservation();

    @BeforeEach
    void setUp(){
        workspace.setId(1);
        workspace.setType("Desk");
        workspace.setPrice(10.0);

        reservation.setId(1);
        reservation.setWorkspace(workspace);
    }

    @Test
    public void givenWorkspace_whenViewAllWorkspaces_thenReturnWorkspaces() throws Exception {
        when(workspaceRepository.findAll()).thenReturn(Collections.singletonList(workspace));

        mockMvc.perform(get("/api/admin/viewWorkspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Desk"))
                .andExpect(jsonPath("$[0].price").value(10.0));
        verify(workspaceRepository, times(1)).findAll();
    }

    @Test
    public void givenWorkspace_whenAddWorkspace_thenReturnWorkspace() throws Exception {
        when(workspaceRepository.save(any(Workspace.class))).thenReturn(workspace);

        mockMvc.perform(post("/api/admin/addWorkspace")
                        .contentType("application/json")
                        .content("{\"type\": \"Desk\", \"price\": 10.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Desk"))
                .andExpect(jsonPath("$.price").value(10.0));
        verify(workspaceRepository, times(1)).save(any(Workspace.class));

    }

    @Test
    public void givenWorkspace_whenRemoveWorkspace_thenWorkspaceDeleted() throws Exception {
        when(workspaceRepository.findById(1)).thenReturn(Optional.of(workspace));

        mockMvc.perform(post("/api/admin/removeWorkspace")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Coworking space with ID 1 removed successfully."));

        verify(workspaceRepository, times(1)).delete(workspace);
    }

    @Test
    public void givenReservation_whenViewAllReservations_thenReturnReservation() throws Exception {
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        mockMvc.perform(get("/api/admin/viewReservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].workspace.id").value(1));
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void givenWorkspace_whenUpdateWorkspace_thenWorkspaceUpdated() throws Exception {
        when(workspaceRepository.findById(1)).thenReturn(Optional.of(workspace));

        mockMvc.perform(post("/api/admin/updateWorkspace")
                        .param("id", "1")
                        .param("type", "Private Office")
                        .param("price", "50.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Coworking space with ID 1 updated successfully."));

        verify(workspaceRepository, times(1)).save(workspace);
    }

    @Test
    public void givenReservation_whenRemoveReservation_thenReservationRemoved() throws Exception {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        mockMvc.perform(post("/api/admin/removeReservation")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation with ID 1 removed successfully."));

        verify(reservationService, times(1)).removeReservation(reservation);
    }
}