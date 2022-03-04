package com.example.project.service;

import com.example.project.db.repo.ClientInformationRepo;
import com.example.project.db.repo.ClientRepo;
import com.example.project.dto.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepo clientRepo;
    private final ClientInformationRepo clientInfoRepo;
    private final Converter converter;


}
