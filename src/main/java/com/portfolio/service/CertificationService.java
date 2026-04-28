package com.portfolio.service;

import com.portfolio.dto.CertificationDto;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Certification;
import com.portfolio.repository.CertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificationService {

    @Autowired
    private CertificationRepository certificationRepository;

    public List<CertificationDto.Response> getAllCertifications() {
        return certificationRepository.findAllByOrderByDisplayOrderAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CertificationDto.Response getCertificationById(Long id) {
        Certification cert = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + id));
        return toResponse(cert);
    }

    public CertificationDto.Response createCertification(CertificationDto.Request request) {
        return toResponse(certificationRepository.save(toEntity(request)));
    }

    public CertificationDto.Response updateCertification(Long id, CertificationDto.Request request) {
        Certification cert = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + id));
        mapRequestToEntity(request, cert);
        return toResponse(certificationRepository.save(cert));
    }

    public void deleteCertification(Long id) {
        if (!certificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Certification not found with id: " + id);
        }
        certificationRepository.deleteById(id);
    }

    // ---- Mappers ----

    private Certification toEntity(CertificationDto.Request req) {
        return Certification.builder()
                .name(req.getName())
                .issuingOrganization(req.getIssuingOrganization())
                .credentialId(req.getCredentialId())
                .credentialUrl(req.getCredentialUrl())
                .issueDate(req.getIssueDate())
                .expiryDate(req.getExpiryDate())
                .badgeImageUrl(req.getBadgeImageUrl())
                .displayOrder(req.getDisplayOrder())
                .build();
    }

    private void mapRequestToEntity(CertificationDto.Request req, Certification cert) {
        cert.setName(req.getName());
        cert.setIssuingOrganization(req.getIssuingOrganization());
        cert.setCredentialId(req.getCredentialId());
        cert.setCredentialUrl(req.getCredentialUrl());
        cert.setIssueDate(req.getIssueDate());
        cert.setExpiryDate(req.getExpiryDate());
        cert.setBadgeImageUrl(req.getBadgeImageUrl());
        cert.setDisplayOrder(req.getDisplayOrder());
    }

    private CertificationDto.Response toResponse(Certification c) {
        CertificationDto.Response res = new CertificationDto.Response();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setIssuingOrganization(c.getIssuingOrganization());
        res.setCredentialId(c.getCredentialId());
        res.setCredentialUrl(c.getCredentialUrl());
        res.setIssueDate(c.getIssueDate());
        res.setExpiryDate(c.getExpiryDate());
        res.setBadgeImageUrl(c.getBadgeImageUrl());
        res.setDisplayOrder(c.getDisplayOrder());
        return res;
    }
}
