package com.aicodinator.backend.domain.community.controller;

import com.aicodinator.backend.domain.community.domain.entity.Board;
import com.aicodinator.backend.domain.community.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private BoardService boardService;

    /*@GetMapping
    public List<Board> findMyBoards(@AuthenticationPrincipal User user) {
        return boardService.findByUser(user);
    }*/

    @GetMapping("/regions/{regionId}/boards")
    public ResponseEntity<List<Board>> findBoardsByRegion(@PathVariable Long regionId) {
        return ResponseEntity.ok(boardService.findByRegionId(regionId));
    }

    // TODO: CustomOAuth2UserPrincipal로 변경
}
