package dev.vabalas.kkkk;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/game")
public class GameStateController {
    private final SentenceRepository sentenceRepository;
    private final GameStateRepository gameStateRepository;

    @GetMapping("{gameId}")
    public List<GameState> getGameById(@PathVariable String gameId) {
        return gameStateRepository.findAll()
                .stream()
                .filter(gameState -> gameState.getGameId().equals(gameId))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<GameState> makeNewGame(@RequestBody NewGameRequest game) {
        var gameStateList = new ArrayList<GameState>();
        String gameId = generateId();
        for (int i = game.getPlayers().size() - 1; i >= 0; i--) {
            gameStateList.add(new GameState(
                    game.getPlayers().get(i),
                    gameId,
                    assignNextPlayer(game.getPlayers(), i),
                    Question.KAS));
        }
        return gameStateRepository.saveAll(gameStateList);
    }

    @PostMapping("{gameId}/shift-notes")
    public void shiftNotes(@PathVariable String gameId) {
        List<GameState> gameStateList = gameStateRepository.findAllByGameId(gameId);
        List<Question> questionList = gameStateList.stream()
                .map(GameState::getQuestion)
                .collect(Collectors.toList());
        if (questionsMatch(questionList)) {
            shiftContent(gameStateList);
            unlockGameStates(gameStateList);
            gameStateRepository.saveAll(gameStateList);
        } else {
            throw new QuestionsAreNotSameException("Questions are not same!");
        }
    }

    @PostMapping("{gameId}/move-content")
    public int moveContentToSentences(@PathVariable String gameId) {
        var sentenceList = new ArrayList<Sentence>();
        getGameById(gameId).forEach(gameState -> sentenceList.add(new Sentence(gameState.getContent())));
        sentenceRepository.saveAll(sentenceList);
        return sentenceList.size();
    }

    @PutMapping("{gameId}")
    public GameState addContent(@PathVariable String gameId, @RequestBody PlayerMoveRequest player) {
        GameState gameState = gameStateRepository.findByGameIdAndName(gameId, player.getName());
        if (!gameState.getQuestion().equals(Question.DONE) && !gameState.isLocked()) {
            gameState.setContent(gameState.getContent() + " " + player.getContent());
            gameState.setQuestion(nextQuestion(gameState.getQuestion()));
            gameState.setLocked(true);
            return gameStateRepository.save(gameState);
        }
        return gameState;
    }

    private String generateId() {
        String newId;
        while (true) {
            newId = RandomString.make(5);
            if (!gameStateRepository.existsByGameId(newId)) {
                return newId;
            }
        }
    }

    private String assignNextPlayer(List<String> players, int i) {
        if (i == players.size() - 1) {
            return players.get(0);
        }
        return players.get(i + 1);
    }

    private Question nextQuestion(Question question) {
        if (question.equals(Question.KA_PASAKE)) {
            return Question.DONE;
        }
        if (question.equals(Question.KAS_PAMATE)) {
            return Question.KA_PASAKE;
        }
        if (question.equals(Question.KA_VEIKE)) {
            return Question.KAS_PAMATE;
        }
        if (question.equals(Question.KAIP)) {
            return Question.KA_VEIKE;
        }
        if (question.equals(Question.SU_KUO)) {
            return Question.KAIP;
        }
        if (question.equals(Question.KUR)) {
            return Question.SU_KUO;
        }
        if (question.equals(Question.KADA)) {
            return Question.KUR;
        }
        if (question.equals(Question.KAS)) {
            return Question.KADA;
        }
        return Question.KAS;
    }

    private void unlockGameStates(List<GameState> gameStateList) {
        gameStateList.forEach(gameState -> gameState.setLocked(false));
    }

    private void shiftContent(List<GameState> gameStateList) {
        var map = new HashMap<String, String>();
        gameStateList.forEach(gameState -> map.put(gameState.getNextIs(), gameState.getContent()));
        gameStateList.forEach(gameState -> gameState.setContent(map.get(gameState.getName())));
    }

    private boolean questionsMatch(List<Question> questionList) {
        Question question = questionList.get(0);
        for (int i = 1; i < questionList.size(); i++) {
            if (!question.equals(questionList.get(i))) {
                return false;
            }
        }
        return true;
    }
}
