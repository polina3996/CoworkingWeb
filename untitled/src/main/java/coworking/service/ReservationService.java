package coworking.service;

import coworking.model.Reservation;
import coworking.model.User;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserRepository;
import coworking.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              UserRepository userRepository,
                              WorkspaceRepository workspaceRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public void makeReservation(Workspace workspaceToBeReserved, String name, LocalDate start, LocalDate end) {
        User user = this.userRepository.findByName(name);
        if (user == null){
            user = new User(name);
            this.userRepository.save(user);
        }
        Reservation reservation = new Reservation(workspaceToBeReserved, user, start, end);
        workspaceToBeReserved.setAvailabilityStatus(false);

        this.reservationRepository.save(reservation);
        this.workspaceRepository.save(workspaceToBeReserved);
    }


    public void removeReservation(Reservation reservationToBeRemoved) {
        Workspace workspace = reservationToBeRemoved.getWorkspace();
        workspace.setAvailabilityStatus(true);

        this.reservationRepository.delete(reservationToBeRemoved);
        this.workspaceRepository.save(workspace);
}
}
