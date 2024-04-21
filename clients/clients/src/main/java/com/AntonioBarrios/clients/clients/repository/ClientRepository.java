package com.AntonioBarrios.clients.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AntonioBarrios.clients.clients.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
