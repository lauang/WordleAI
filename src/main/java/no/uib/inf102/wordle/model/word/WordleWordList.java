package no.uib.inf102.wordle.model.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import no.uib.inf102.wordle.resources.GetWords;

/**
 * This class describes a structure of two lists for a game of Wordle: The list
 * of words that can be used as guesses and the list of words that can be possible answers.
 */
public class WordleWordList {

	/**
	 * All words in the game. These words can be used as guesses.
	 */
	private List<String> allWords;

	/**
	 * A subset of <code>allWords</code>. <br>
	 * </br>
	 * These words can be the answer to a wordle game.
	 */
	private List<String> possibleAnswers;

	/**
	 * Create a WordleWordList that uses the full words and limited answers of the
	 * GetWords class.
	 */
	public WordleWordList() {
		this(GetWords.ALL_WORDS_LIST, GetWords.ANSWER_WORDS_LIST);
	}

	/**
	 * Create a WordleWordList with the given <code>words</code> as both guesses and
	 * answers.
	 * 
	 * @param words
	 */
	public WordleWordList(List<String> words) {
		this(words, words);
	}

	/**
	 * Create a WordleWordList with the given lists as guessing words and answers.
	 * <code>answers</code> must be a subset of <code>words</code>.
	 * 
	 * @param words   The list of words to be used as guesses
	 * @param answers The list of words to be used as answers
	 * @throws IllegalArgumentException if the given <code>answers</code> were not a
	 *                                  subset of <code>words</code>.
	 */
	public WordleWordList(List<String> words, List<String> answers) {
		HashSet<String> wordsSet = new HashSet<String>(words);
		if (!wordsSet.containsAll(answers))
			throw new IllegalArgumentException("The given answers were not a subset of the valid words.");

		this.allWords = new ArrayList<>(words);
		this.possibleAnswers = new ArrayList<>(answers);
	}

	/**
	 * Get the list of all guessing words.
	 * 
	 * @return all words
	 */
	public List<String> getAllWords() {
		return allWords;
	}

	/**
	 * Returns the list of possible answers.
	 * 
	 * @return
	 */
	public List<String> possibleAnswers() {
		return possibleAnswers;
	}

	/**
	 * Eliminates words from the possible answers list using the given
	 * <code>feedback</code>
	 * 
	 * @param feedback
	 */
	public void eliminateWords(WordleWord feedback) { //O(m*k^2)
		//Make a new list of possible answers
		List<String> newPossibleAnswers = new ArrayList<String>();

		//Check if each word is a possible answer
		for(String word : possibleAnswers) { //O(m)
			if(WordleWord.isPossibleWord(word, feedback)){ //O(k^2)
				newPossibleAnswers.add(word); //O(1)
			}
		}
		//Set the possible answers to the new list
		possibleAnswers = newPossibleAnswers; //O(1)
	}

	/**
	 * Returns the best guess word from the list of possible answers.
	 *
	 * @return best guess
	 */
	public String findBestGuess() { //O(m*k)
        List<HashMap<Character, Integer>> indexFrequencies = new ArrayList<>(); //O(1)

        for (int i = 0; i < 5; i++){ //O(m*k)
            indexFrequencies.add(indexFrequency(i)); //O(m)
        }
    
        int max = 0;
        String bestGuess = null;

        for (String word : possibleAnswers) { //O(m*k)
            int poeng = 0;

            for(int i = 0; i < word.length(); i++){ //O(k)
                char c = word.charAt(i); //O(1)
                int freq = indexFrequencies.get(i).get(c); //O(1)
                if (freq != 0){
                    poeng += freq;
                }
            }
            if (poeng > max){
                max = poeng;
                bestGuess = word;
            }
        }
        return bestGuess;
    }


	/**
	 * Returns a HashMap with the frequency of each character at the given index.
	 * @param index
	 * @return HashMap<Character, Integer>
	 */
    public HashMap<Character, Integer> indexFrequency (int index){ //O(m)
        HashMap<Character, Integer> indexFrequency = new HashMap<Character, Integer>(); //O(1)

        for (String word : possibleAnswers) { //O(m)
            char c = word.charAt(index); //O(1)
            indexFrequency.put(c, indexFrequency.getOrDefault(c, 0) + 1);
        }

        return indexFrequency;
    }

	/////////////////////////////////
	//METHODS FOR SECOND BEST GUESS//
	/////////////////////////////////

	/**
	 * Returns the second best guess for the given <code>feedback</code>. 
	 * 
	 * @param feedback the feedback from the first guess
	 * @return second best guess
	 */
	public String findSecondBestGuess(WordleWord feedback) {
		List<Character> usedLetters = logUsedLetters(feedback); //O(k)
        List<HashMap<Character, Integer>> indexFrequencies = new ArrayList<>(); //O(1)

        for (int i = 0; i < 5; i++){ //O(m*k)
            indexFrequencies.add(indexFrequency(i)); //O(m)
        }
    
        int max = 0;
        String bestGuess = null;

		for(String word : possibleAnswers) { //O(m)
			boolean containsLetterFromFirstGuess = false;
			for(char c : word.toCharArray()){ //O(k)
				if (usedLetters.contains(c)){
					containsLetterFromFirstGuess = true;
					break;
				}
			}

			if (containsLetterFromFirstGuess){
				continue;
			}

			int poeng = 0;
            for(int i = 0; i < word.length(); i++) { //O(k)
                char c = word.charAt(i);
                int freq = indexFrequencies.get(i).get(c);
                if (freq != 0){
                    poeng += freq;
                }
            }
            if (poeng > max){
                max = poeng;
                bestGuess = word;
            }
        }
        return bestGuess;
    }

	/**
	 * Returns a list of all the letters used in the given <code>feedback</code>.
	 * @param feedback the feedback from the first guess
	 * @return list of used letters
	 */
	private List<Character> logUsedLetters(WordleWord feedback){ //O(k)
		List<Character> usedLetters = new ArrayList<Character>(); //O(1)
		for (WordleCharacter character : feedback){ //O(k)
			usedLetters.add(character.letter);
		}
		return usedLetters;
	}

	/**
	 * Returns the number of matches from the given <code>feedback</code>, both green and yellow matches.
	 * 
	 * @param feedback
	 * @return number of matches
	 */
	public int numberOfMatches(WordleWord feedback){ //O(k)
		int grennYellowCount = 0;
            for(WordleCharacter character : feedback){ //O(k)
                if(character.answerType == AnswerType.CORRECT || character.answerType == AnswerType.WRONG_POSITION){
                    grennYellowCount++;
                }
            }
		return grennYellowCount;
	}

    

	/**
	 * Returns the amount of possible answers in this WordleWordList
	 * 
	 * @return size of
	 */
	public int size() {
		return possibleAnswers.size();
	}

	/**
	 * Removes the given <code>answer</code> from the list of possible answers.
	 * 
	 * @param answer
	 */
	public void remove(String answer) {
		possibleAnswers.remove(answer);

	}

	/**
	 * Returns the word length in the list of valid guesses.
	 * @return
	 */
	public int wordLength() {
		return allWords.get(0).length();
	}
}
