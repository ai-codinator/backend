package com.aicodinator.backend.domain.community.controller;

import com.aicodinator.backend.domain.community.domain.dto.response.BoardResponse;
import com.aicodinator.backend.domain.community.domain.entity.Board;
import com.aicodinator.backend.domain.community.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    /*@GetMapping
    public List<Board> findMyBoards(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return boardService.findByUser(oAuth2User.getUser());
    }*/

    @GetMapping("/regions/{regionId}/boards")
    public ResponseEntity<List<BoardResponse>> findBoardsByRegion(@PathVariable Long regionId) {
        return ResponseEntity.ok(boardService.findByRegionId(regionId));
    }

    /**
     * 테스트를 위한 API
     * 실제로 Board 생성은 리포트 생성 시점에 서비스 로직에서 진행
     */
    @PostMapping("/regions/{regionId}/boards")
    public ResponseEntity<Void> createBoardsByRegion(@PathVariable Long regionId) {
        boardService.createBoards(regionId);
        return ResponseEntity.ok().build();
    }
}
