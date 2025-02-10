package com.hackerrank.api.service.impl;

import com.hackerrank.api.exception.BadRequestException;
import com.hackerrank.api.exception.ElementNotFoundException;
import com.hackerrank.api.model.Scan;
import com.hackerrank.api.repository.ScanRepository;
import com.hackerrank.api.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultScanService implements ScanService {
    private final ScanRepository scanRepository;

    @Autowired
    DefaultScanService(ScanRepository scanRepository) {
        this.scanRepository = scanRepository;
    }

    @Override
    public List<Scan> getAllScan() {
        return scanRepository.findAll().stream()
                .filter(scan -> !scan.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Scan createNewScan(Scan scan) {
        if (scan.getId() != null) {
            throw new BadRequestException("The ID must not be provided when creating a new Scan");
        }

        return scanRepository.save(scan);
    }

    @Override
    public Scan getScanById(Long id) {
        return scanRepository.findById(id).orElseThrow(() -> {
            throw new ElementNotFoundException("Resource not found with id " + id);
        });
    }

    @Override
    public void deleteById(Long id) {
        Scan scan = scanRepository.findById(id).orElseThrow(() -> {
            throw new ElementNotFoundException("Resource not found with id " + id);
        });
        scan.setDeleted(true);
        scanRepository.save(scan);
    }
    @Override
    public List<Scan> searchScanByDomainName(String domainName, String orderBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, orderBy);
        List<Scan> scans = scanRepository.findAllByDomainName(domainName, sort);
        if (scans.isEmpty()) {
            throw new BadRequestException("Resource not found");
        }
        return scans;
    }
}
