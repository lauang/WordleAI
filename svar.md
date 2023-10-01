# Runtime Analysis
For each method of the tasks give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented new methods not listed you must add these as well, e.g. any helper methods. You need to show how you analyzed any methods used by the methods listed below.**

The runtime should be expressed using these three parameters:
   * `n` - number of words in the list allWords
   * `m` - number of words in the list possibleWords
   * `k` - number of letters in the wordleWords


## Task 1 - matchWord
* `WordleAnswer::matchWord`: O(k^2)
    * This method is showing us the match between the guess and the answer. It contains three for-loops, which I will analyse the runtime for each of those. 
    * The first for-lopp is interating through the wordlenght (which is the lenght of the answer), which has the lenght 5 O(k). Inside this loop, we have a if-statement which is O(1), becuse it is just compairing two chars. Then the answer stirng is re-adjusted to a substring, where we remove the current char from the string if it is correct letter. In worst case, this is also O(k). That means our first loop is O(k^2).
    * The second loop is cheching if the char if the guess is at the wrong possition. This loop is also iterating through the wordLenght O(k). Inside the lopp is two if-statements. The first checks if the current char in the feedback is a correct letter, and if it is, then go to the next char. This time-complexity is O(1). The second if-statement is checking if the answer contains the spesific char, which is in worst case O(k). Inside this if-statement, we first make the feeback at this position the answertype WRONG_POSITION (O(1)), then we find the index of the current char (O(1))!!!!!!!!, and then we make a substring, which is O(k) in worst case. That means our second loop is (O(k^2)).
    * The third loop is very simple. It is iterating through feedbak.lenght, which is O(k), and then the if-statement fills in the answer type WRONG, if it is empty. This makes the third loop O(k).
    * To summerize, the matchWord method has a time complexity of O(k^2) + O(k^2) + O(k), which is equal to O(k^2).


## Task 2 - EliminateStrategy
* `WordleWordList::eliminateWords`: O(m*k^2)
    * In this method, there is a empty list of strings and a for-each loop.
    * The for-each loop is going through all the words in possible answers. This has the time complexity of O(m) in worst case (after the first guess). Inside the loop there is a if-statement, which is checking if it is a possible word given the feedback, and if it is, then add the word to the newPossibleAnswers. The isPossibleWord method is using our matchWord method, which had a time complexity of O(k^2). Therefore, the loop is (O(m) + O(k^2)). At last, we set possibleWords to be equal to newPossibleWord, O(1).
    * To summerize, our eliminateWords method has O(m) + O(k^2) time complexity, which is equal to O(m*k^2).

## Task 3 - FrequencyStrategy
* `FrequencyStrategy::makeGuess`: O(m*k)
    * In my makeGuess method I have a if-else-statement. If feedback is null, we call on the findBestGuess method on the WordleWordList object, guesses. I will first analyse this case where feedback is null.
    * In our findBestGuess method, we have one single for-loop and a dobble for-loop. In the single for-loop we are creating 5 different HasMaps, and adding them to a list of HashMaps. For creating those HashMaps, I have made a method named indexFrequency, which is finding the most used chars given the index. In indexFrequency, we are looping through every word in possible answers. The time complexity of this method is O(m), becuase of this loop. Now we go back to the frist loop in findBestGuess. GIven that the indexFrequency takes O(m), and we do this 5 times (O(k)), we know that this first loop takes O(m*k) time.
    * The dobble for-loop is first going through every word in possibleAnswers, O(m), and then going through every char in a word, O(k). The if-statement in this dobble for-loop, is O(1).
    * The last if statement is also O(1).
    * To summerize, our findBestGuess has the time complexity of O(m*k).
    * Now that we have analysed the case where feedback is null (O(m*k)), we can analyse the case where feedback is not equal to null. We first make a call on the eliminateWords method, which we have already analysed O(m*k^2). Then we do the same as if feedback was null O(m*k), however now the possible answers list have become smaller, since we did a call on eliminateWords.
    * If feedback is null the time complexity for makeGuess is O(m*k) and when feedback isnt null, the time complexity is O(m*k^2).  



# Task 4 - Make your own (better) AI
For this task you do not need to give a runtime analysis. 
Instead, you must explain your code. What was your idea for getting a better result? What is your strategy?

* I thought frequencyStrategy was a very fast algorythm for finding the right answer. However, when the first guess was made, and the respons to the chars (either green og yellow) was less then two, it used a lot of tries to guess the word. I wanted myAI to use this second guess to gather as much information, and find the second best guess if the feedback was lower than two, and if it was the second guess.

* My findSecondBestWord method is taking in a feedback as a parameter, and using this in another method named logUsedLetters. This method is simply making a list with used chars from the first guess. Then back to the findSecondBestGuess method. Here I'm just looping through possible answers and and skipping every word that has a letter from the first guess. If the word does not have any of the used letters, it calculates a score for the current word. The word with the highest score, is then returned.

* My method is using 3.455 on 200 games, while the frequency is using 3.500 on 200 games. So it is faster.

* I know that my findSecondBestGuess is nearly identical to findBestGuess, and this is bad re-usage of code. I should have find a way to make them use the same method, but give the method different lists, depending on which best guess I needed. 
