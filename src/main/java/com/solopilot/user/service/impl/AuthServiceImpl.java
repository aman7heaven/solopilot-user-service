package com.solopilot.user.service.impl;

import com.autopilot.config.exception.ApplicationException;
import com.autopilot.config.exception.ApplicationExceptionTypes;
import com.autopilot.utils.StringUtils;
import com.portfolio.service.JwtService;
import com.solopilot.user.dto.payload.RegisterPayload;
import com.solopilot.user.entity.admin.Admin;
import com.portfolio.entity.AdminToken;
import com.solopilot.user.repository.IAdminRepository;
import com.portfolio.repository.IAdminTokenRepository;
import com.solopilot.user.service.IAuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {

    private final JwtService jwtService;
    private final IAdminRepository adminRepository;
    private final IAdminTokenRepository adminTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            JwtService jwtService,
            IAdminRepository adminRepository,
            IAdminTokenRepository adminTokenRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
        this.adminTokenRepository = adminTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String registerAdmin(RegisterPayload payload) {
        Admin existingAdmin = adminRepository.findFirstByOrderByIdAsc();
        if (existingAdmin != null) {
            throw new ApplicationException(ApplicationExceptionTypes.ADMIN_ALREADY_EXISTS);
        }

        Admin newAdmin = new Admin();
        newAdmin.setUuid(StringUtils.generateUUID());
        newAdmin.setFirstName(payload.getFirstName());
        newAdmin.setLastName(payload.getLastName());
        newAdmin.setUsername(payload.getUsername());
        newAdmin.setEmail(payload.getEmail());
        newAdmin.setPassword(passwordEncoder.encode(payload.getPassword()));

        adminRepository.save(newAdmin);

        String token = jwtService.generateToken(newAdmin.getEmail(), List.of("ADMIN", "USER"));
        saveAdminToken(newAdmin, token);

        return token;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String loginAdmin(String emailOrUsername, String password) {
        Admin admin = adminRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.INVALID_AUTH_DETAIL));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new ApplicationException(ApplicationExceptionTypes.INVALID_AUTH_DETAIL);
        }

        String token = jwtService.generateToken(admin.getEmail(), List.of("ADMIN", "USER"));
        saveAdminToken(admin, token);

        return token;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void logoutAdmin(String token) {
        AdminToken adminToken = adminTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.INVALID_AUTH_DETAIL));

        adminToken.setRevoked(true);
        adminTokenRepository.save(adminToken);
    }

    private void saveAdminToken(Admin admin, String token) {
        AdminToken adminToken = AdminToken.builder()
                .admin(admin)
                .token(token)
                .revoked(false)
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusHours(1))
                .build();

        adminTokenRepository.save(adminToken);
    }
}