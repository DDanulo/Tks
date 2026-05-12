package infrastructure.client;

import domain.ReservationClient;

import java.util.List;
import java.util.Optional;

public interface FindClientPort {
    public Optional<ReservationClient> findById(String id);

    public Optional<ReservationClient> findByLogin(String login);

    public List<ReservationClient> findAll();
}
