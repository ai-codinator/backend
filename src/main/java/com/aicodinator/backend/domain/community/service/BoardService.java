package com.aicodinator.backend.domain.community.service;

import com.aicodinator.backend.domain.community.domain.constant.BoardType;
import com.aicodinator.backend.domain.community.domain.entity.Board;
import com.aicodinator.backend.domain.community.repository.BoardRepository;
import com.aicodinator.backend.domain.region.domain.Region;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board findById(long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "요청하신 게시판을 찾을 수 없습니다."));
    }

    public List<Board> findByRegion(Region region) {
        return boardRepository.findByRegion(region);
    }

    public List<Board> findByRegionId(Long regionId) {
        return boardRepository.findByRegionId(regionId);
    }

    public List<Board> findByUser(User user) {
        return boardRepository.findBoardsForUserRegions(user);
    }

    public void createBoards(Region region) {
        if (findByRegion(region).size() >= 4) return;

        List<BoardType> boardTypes = List.of(BoardType.FREE, BoardType.QNA, BoardType.REVIEW, BoardType.PROMO);

        List<Board> boards = boardTypes.stream()
            .map(boardType ->
                Board.builder()
                    .region(region)
                    .boardType(boardType)
                    .build())
            .toList();
        boardRepository.saveAll(boards);
    }
}
