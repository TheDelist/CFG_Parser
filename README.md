# CFG_Parser

Hi 
In this project I made Context Free Grammer Parser for ambiguity words.


## Algorithm Strategy
* This project was made with Backtracking logic.
* I have made all possible derivations of the Grammar in the "CFG.txt" given to us while developing the algorithm.
* I have stored the paths of these derived words in an ArrayList.
* I wrote a search function for the input sentences entered into us and I found where the letters in the sentence were derived from with the Backtracking logic.
* Since I already know the paths, Parse Tree and Derivation Tree of the letters where they come from are shown.

## Derivation

![alt text](https://github.com/TheDelist/CFG_Parser/blob/master/inputandderivation.PNG?raw=true)

## Parse Tree

![alt text](https://github.com/TheDelist/CFG_Parser/blob/master/parsetree.PNG?raw=true)


## Warning

- Grammer with empty string in this code does not accept

