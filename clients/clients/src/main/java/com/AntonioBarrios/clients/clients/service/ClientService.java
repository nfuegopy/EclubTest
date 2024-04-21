package com.AntonioBarrios.clients.clients.service;

import com.AntonioBarrios.clients.clients.model.Client;
import com.AntonioBarrios.clients.clients.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientService {
    public final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado por ID: " + id));
    }

    public Client updateClientPartially(Long id, Map<String, Object> updates) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        client.setName((String) value);
                        break;
                    case "lastname":
                        client.setLastname((String) value);
                        break;
                }
            });
            return clientRepository.save(client);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepository.delete(client);
    }

}
