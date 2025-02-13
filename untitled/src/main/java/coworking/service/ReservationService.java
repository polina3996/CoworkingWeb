package coworking.service;

import coworking.dto.ReservationSubject;
import coworking.model.Reservation;
import coworking.model.UserEntity;
import coworking.model.Workspace;
import coworking.repository.ReservationRepository;
import coworking.repository.UserEntityRepository;
import coworking.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserEntityRepository userEntityRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ReservationSubject reservationSubject;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              UserEntityRepository userEntityRepository,
                              WorkspaceRepository workspaceRepository, ReservationSubject reservationSubject) {
        this.reservationRepository = reservationRepository;
        this.userEntityRepository = userEntityRepository;
        this.workspaceRepository = workspaceRepository;
        this.reservationSubject = reservationSubject;
    }

    public void makeReservation(Workspace workspaceToBeReserved, String name, LocalDate start, LocalDate end) {
        if (!workspaceToBeReserved.getAvailabilityStatus()) {
            throw new IllegalStateException("Workspace is already reserved.");
        }

        UserEntity user = this.userEntityRepository.findByName(name);
        Reservation reservation = new Reservation(workspaceToBeReserved, user, start, end);
        workspaceToBeReserved.setAvailabilityStatus(false);

        this.reservationRepository.save(reservation);
        this.workspaceRepository.save(workspaceToBeReserved);
        this.reservationSubject.addObserver(new UserObserver(user.getName()));
    }


    public void removeReservation(Reservation reservationToBeRemoved) {
        Workspace workspace = reservationToBeRemoved.getWorkspace();
        workspace.setAvailabilityStatus(true);

        this.reservationRepository.delete(reservationToBeRemoved);
        this.workspaceRepository.save(workspace);
        this.reservationSubject.notifyObservers(reservationToBeRemoved.getId());
}
}
