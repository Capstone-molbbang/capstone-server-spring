package com.capstone.capstone.node.controller;

import com.capstone.capstone.node.domain.Node;
import com.capstone.capstone.node.service.NodeService;
import lombok.RequiredArgsConstructor;
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
    public List<Node> getCustomNodesForRoot1() {
        return nodeService.getCustomNodesForRoot1();
    }

    @GetMapping("/nodes/root2")
    public List<Node> getCustomNodesForRoot2() {
        return nodeService.getCustomNodesForRoot2();
    }

}
