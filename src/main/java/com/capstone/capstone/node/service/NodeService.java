package com.capstone.capstone.node.service;

import com.capstone.capstone.node.domain.Node;
import com.capstone.capstone.node.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final NodeRepository nodeRepository;

    public List<Node> getCustomNodesForRoot1() {
        return nodeRepository.findByPathNumber(1);
    }

    public List<Node> getCustomNodesForRoot2() {
        return nodeRepository.findByPathNumber(2);
    }

    public List<Node> getCustomNodesForRoot3() {
        return nodeRepository.findByPathNumber(3);
    }

    public List<Node> getCustomNodesForRoot4() {
        return nodeRepository.findByPathNumber(4);
    }
}
