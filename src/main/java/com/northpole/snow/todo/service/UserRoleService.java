package com.northpole.snow.todo.service;
import org.springframework.stereotype.Service;
import com.northpole.snow.todo.domain.AdministratorRepository;
import com.northpole.snow.todo.domain.Pasazer;

@Service
public class UserRoleService {
    
    private final AdministratorRepository adminRepo;

    public UserRoleService(AdministratorRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public boolean isAdmin(Pasazer user) {
        return adminRepo.findByPasazerid(user).isPresent();
    }
}