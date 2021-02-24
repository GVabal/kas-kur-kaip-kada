package dev.vabalas.kkkk;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@Entity
public class GameState {
    @Id
    private String name;

    private String gameId;
    private String content;
    private String nextIs;
    private boolean isLocked;

    @Enumerated(EnumType.STRING)
    private Question question;

    public GameState(String name, String gameId, String nextIs, Question question) {
        this.name = name;
        this.gameId = gameId;
        this.nextIs = nextIs;
        this.question = question;
        this.content = "";
        this.isLocked = false;
    }
}
