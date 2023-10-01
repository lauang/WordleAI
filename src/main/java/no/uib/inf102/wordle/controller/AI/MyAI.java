package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;


/**
 * This strategy finds the word within the possible words which has the highest expected
 * number of green matches, and if the first guess contains less than 2 green or yellow matches,
 * it will use the metod findSecondBestGuess to find the second best guess.
 */
public class MyAI implements IStrategy {
    private WordleWordList guesses;
    private int guessCount = 0;

    public MyAI() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        String guess = "";
        if (feedback != null) {
            int grennYellowCount = guesses.numberOfMatches(feedback);

            if (grennYellowCount < 2 && guessCount == 1){
                guessCount++;
                guess = guesses.findSecondBestGuess(feedback);
                guesses.eliminateWords(feedback);
            }
            else {
                guessCount++;
                guesses.eliminateWords(feedback);
                guess = guesses.findBestGuess();
            }
        }
        else {
            guessCount++;
            guess = guesses.findBestGuess();
        }
        return guess;
    }

    @Override
    public void reset() {
         guesses = new WordleWordList();
         guessCount = 0;
    }
    
}
