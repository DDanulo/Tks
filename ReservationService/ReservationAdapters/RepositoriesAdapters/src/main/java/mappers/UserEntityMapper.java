package mappers;

import client.data.ClientEntity;

import domain.ReservationClient;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public ReservationClient EntityToUser(ClientEntity user) {
        if (user == null) return null;
        return ReservationClient.builder()
                .userId(user.getUserId() != null ? user.getUserId().toString() : null)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public ClientEntity toClientEntity(ReservationClient dto) {
        ClientEntity client = new ClientEntity();
        client.setEmail(dto.getEmail());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        return client;
    }

}