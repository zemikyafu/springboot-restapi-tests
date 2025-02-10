package com.hackerrank.api.controller;

import com.hackerrank.api.model.Scan;
import com.hackerrank.api.service.ScanService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLEngineResult;
import java.util.List;

@RestController
@RequestMapping("/scan")
public class ScanController {
    private final ScanService scanService;

    @Autowired
    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Scan> getAllScan() {
        return scanService.getAllScan();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Scan createScan(@RequestBody Scan scan) {
        return scanService.createNewScan(scan);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scan> getScan(@PathVariable Long id) {
        Scan scan = scanService.getScanById(id);
        return new ResponseEntity<>(scan, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteScan(@PathVariable Long id) {
        scanService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/{domainName}")
    public ResponseEntity<List<Scan>> searchScan(@PathVariable String domainName,
                                                 @RequestParam(value = "orderBy", required = false, defaultValue = "numPages") String orderBy) {
        List<Scan> scans = scanService.searchScanByDomainName(domainName, orderBy);
        return ResponseEntity.ok(scans);
    }
}
