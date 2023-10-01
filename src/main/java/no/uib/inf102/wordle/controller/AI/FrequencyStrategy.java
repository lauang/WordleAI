package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;


/**
 * This strategy finds the word within the possible words which has the highest expected
 * number of green matches.
 */
public class FrequencyStrategy implements IStrategy {

    private WordleWordList guesses;

    public FrequencyStrategy() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
            return guesses.findBestGuess();
        }
        else {
            return guesses.findBestGuess();
        }
    }


    @Override
    public void reset() {
        guesses = new WordleWordList();
    }
} 