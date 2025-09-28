package com.example.fitnesstracker.service;

import com.example.fitnesstracker.model.Admin;
import com.example.fitnesstracker.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository repo;

    public AdminService(AdminRepository repo) {
        this.repo = repo;
    }

    public List<Admin> getAllAdmins() { return repo.findAll(); }
    public Admin saveAdmin(Admin admin) { return repo.save(admin); }
    public void deleteAdmin(Long id) { repo.deleteById(id); }
}
