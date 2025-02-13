package coworking.repository;
import coworking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser_Name(String name);

    List<Reservation> findByWorkspace_Id(int workspaceId);


}
