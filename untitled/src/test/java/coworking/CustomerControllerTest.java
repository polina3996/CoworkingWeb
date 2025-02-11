package coworking;

import coworking.controller.CustomerController;
import coworking.dto.ReservationForm;
import coworking.model.Reservation;
import coworking.model.UserEntity;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserEntityRepository;
import coworking.repository.WorkspaceRepository;
import coworking.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@WebMvcTest(CustomerController.class)
@WithMockUser(username = "user", roles = {"USER"})
public class CustomerControllerTest {

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
    private CustomerController customerController;

    private Workspace workspace1 = new Workspace();
    private Workspace workspace2 = new Workspace();
    private Reservation reservation = new Reservation();
    private ReservationForm reservationForm = new ReservationForm();
    private UserEntity user = new UserEntity();

    @BeforeEach
    void setUp(){
        workspace1.setId(1);
        workspace1.setType("Desk");
        workspace1.setPrice(10.0);
        workspace1.setAvailabilityStatus(true);

        workspace2.setId(2);
        workspace2.setType("Open");
        workspace2.setPrice(15.0);
        workspace2.setAvailabilityStatus(false);

        reservationForm.setWorkspaceId(1);
        reservationForm.setStartDate(LocalDate.of(2025, 2, 10));
        reservationForm.setEndDate(LocalDate.of(2025, 2, 12));

        user.setName("user");

        reservation.setId(1);
        reservation.setWorkspace(workspace2);
        reservation.setUser(user);
            }

    @Test
    public void givenWorkspaces_whenBrowseAvailableSpaces_thenReturnAvailableWorkspace() throws Exception {
        when(workspaceRepository.findByAvailabilityStatusTrue()).thenReturn(Collections.singletonList(workspace1));

        mockMvc.perform(get("/api/customer/browseAvailableWorkspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("Desk"));

        verify(workspaceRepository, times(1)).findByAvailabilityStatusTrue();
    }

    @Test
    public void givenValidData_whenMakeAReservation_thenReservationMade() throws Exception {
        when(workspaceRepository.findById(1)).thenReturn(Optional.of(workspace1));
        doNothing().when(reservationService).makeReservation(any(), anyString(), any(), any());

        mockMvc.perform(post("/api/customer/makeAReservation")
                        .contentType("application/json")
                        .content("{\"workspaceId\": 1, \"startDate\": \"2025-03-03\", \"endDate\": \"2025-03-04\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Reservation made successfully!"));
        verify(workspaceRepository, times(1)).findById(1);
        verify(reservationService, times(1)).makeReservation(any(), eq("user"), any(), any());
    }

    @Test
    public void givenInvalidWorkspace_whenMakeAReservation_thenReturnBadRequest() throws Exception {
        when(workspaceRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/customer/makeAReservation")
                        .contentType("application/json")
                        .content("{\"workspaceId\": 1, \"startDate\": \"2025-02-10\", \"endDate\": \"2025-02-12\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Invalid workspace ID"));

        verify(workspaceRepository, times(1)).findById(1);
    }

    @Test
    public void givenInvalidDates_whenMakeAReservation_thenReturnBadRequest() throws Exception {
        when(workspaceRepository.findById(1)).thenReturn(Optional.of(workspace1));

        mockMvc.perform(post("/api/customer/makeAReservation")
                        .contentType("application/json")
                        .content("{\"workspaceId\": 1, \"startDate\": \"2025-02-10\", \"endDate\": \"2025-02-05\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Invalid reservation dates."));

        verify(workspaceRepository, times(1)).findById(1);
    }

    @Test
    public void givenReservation_whenViewMyReservations_thenReservationsReturned() throws Exception {
        when(reservationRepository.findByUser_Name("user")).thenReturn(Collections.singletonList(reservation));

        mockMvc.perform(get("/api/customer/myReservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].workspace.id").value(2));

        verify(reservationRepository, times(1)).findByUser_Name("user");
    }

    @Test
    public void givenNoReservation_whenViewMyReservations_thenReturnNotFound() throws Exception {
        when(reservationRepository.findByUser_Name("user")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/customer/myReservations"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No reservations for user: user"));

        verify(reservationRepository, times(1)).findByUser_Name("user");
    }

    @Test
    public void givenReservation_whenCancelMyReservation_thenReservationRemoved() throws Exception {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        mockMvc.perform(post("/api/customer/cancelMyReservation")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation with ID 1 removed successfully."));

        verify(reservationService, times(1)).removeReservation(reservation);
    }
}
