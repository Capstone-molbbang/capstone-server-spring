package com.capstone.capstone.node.controller;

import com.capstone.capstone.node.domain.Node;
import com.capstone.capstone.node.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping("/nodes/root1")
    public ResponseEntity<?> getCustomNodesForRoot1() {
        return ResponseEntity.ok().body(nodeService.getCustomNodesForRoot1());
    }

    @GetMapping("/nodes/root2")
    public ResponseEntity<?> getCustomNodesForRoot2() {
        return ResponseEntity.ok().body(nodeService.getCustomNodesForRoot2());
    }

}
