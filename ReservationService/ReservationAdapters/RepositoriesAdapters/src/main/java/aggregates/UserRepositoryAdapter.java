package aggregates;

import domain.ReservationClient;

import infrastructure.client.FindClientPort;

import lombok.RequiredArgsConstructor;
import mappers.UserEntityMapper;
import org.bson.types.ObjectId;
import client.repo.ClientRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements FindClientPort{
    private final ClientRepository clientRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<ReservationClient> findById(String id) {
        return clientRepository.findById(new ObjectId(id)).map(userEntityMapper::EntityToUser);
    }

    @Override
    public Optional<ReservationClient> findByLogin(String login) {
        return clientRepository.findByLogin(login).map(userEntityMapper::EntityToUser);
    }

    @Override
    public List<ReservationClient> findAll() {
        return clientRepository.findAll().stream().map(userEntityMapper::EntityToUser).toList();
    }

}
