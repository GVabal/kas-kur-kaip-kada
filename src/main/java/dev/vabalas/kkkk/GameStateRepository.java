package dev.vabalas.kkkk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameStateRepository extends JpaRepository<GameState, String> {
    GameState findByGameIdAndName(String gameId, String name);
    boolean existsByGameId(String gameId);
    List<GameState> findAllByGameId(String gameId);
}
