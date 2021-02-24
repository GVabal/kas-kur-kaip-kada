package dev.vabalas.kkkk;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/sentences")
public class SentencesController {
    private final SentenceRepository sentenceRepository;

    @GetMapping
    public List<Sentence> getAll() {
        return sentenceRepository.findAll();
    }

    @GetMapping("{id}")
    public Sentence getOneById(@PathVariable Long id) {
        return sentenceRepository.findById(id)
                .orElseThrow(() -> new NoSuchSentenceException("No sentence with id " + id));
    }
}
