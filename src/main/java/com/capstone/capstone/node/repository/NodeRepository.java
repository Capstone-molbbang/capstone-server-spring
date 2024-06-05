package com.capstone.capstone.node.repository;

import com.capstone.capstone.node.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    public List<Node> findByPathNumber (int pathNumber);
}
