package com.capstone.capstone.node.controller;

import com.capstone.capstone.node.domain.Node;
import com.capstone.capstone.node.dto.NodeResponse;
import com.capstone.capstone.node.service.NodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class NodeController {

    private final NodeService nodeService;

    @GetMapping("/nodes/root1")
    public ResponseEntity<?> getCustomNodesForRoot1() {
        List<Node> root1 = nodeService.getCustomNodesForRoot1();
        List<NodeResponse> root1DTO = root1.stream()
                .map(node -> new NodeResponse(node.getLongitude(), node.getLatitude()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(root1DTO);
    }

    @GetMapping("/nodes/root2")
    public ResponseEntity<?> getCustomNodesForRoot2() {
        List<Node> root2 = nodeService.getCustomNodesForRoot2();
        List<NodeResponse> root2DTO = root2.stream()
                .map(node -> new NodeResponse(node.getLongitude(), node.getLatitude()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(root2DTO);

    }

}
