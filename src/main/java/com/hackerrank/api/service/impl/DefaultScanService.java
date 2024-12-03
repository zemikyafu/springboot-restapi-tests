package com.hackerrank.api.service.impl;

import com.hackerrank.api.exception.BadRequestException;
import com.hackerrank.api.model.Scan;
import com.hackerrank.api.repository.ScanRepository;
import com.hackerrank.api.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
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
    return null;
  }

  @Override
  public void deleteById(Long id) {

  }
}
