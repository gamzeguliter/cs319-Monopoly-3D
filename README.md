# cs319-3D

CS 319: Object-Oriented Software Engineering  
First Report  
September 30, 2020  

Öykü Irmak Hatipoğlu - 21802791  
Cansu Moran - 21803665  
Yiğit Gürses - 21702746  
Melisa Taşpınar - 21803668  
Elif Gamze Güliter - 21802870

   We will implement Monopoly in our project. The game will be played by 2-8 users, where users will play on one computer, taking turns. 
We came up with two different ideas on how to implement game actions. First option is a manual game where the users will only be given a prompt (ex: Pay 100K to bank or move 2 squares) and it will be the users’ responsibility to move their pawns or fulfill tasks. This will eventually allow cheating in the game as one can place his/her pawn on another place or one can give an insufficient amount of money. The second option is an automated game, in which certain game actions that don't require user input such as moving the pawns, drawing money from users or implementing the chance card prompts will be automatically done by the program. Other actions that depend on the user’s choice like choosing whether they would like to buy/sell a place or whether they would like to build houses/hotels, will be implemented using buttons. In the automated version, users will not be bothered by controlling their pawns since the game handles all. In addition, since the obligatory game tasks will be done by the computer, this version doesn’t allow bending of the rules. On the other hand, in the manual version, users will have more freedom which is more similar to an actual board game. For example, users will change the places of their pawns by mouse actions or when a card is drawn, the action that the card requires will be performed by the user instead of the game itself. The decision to use which version has not been finalized yet. Apart from manual game mode, to make the game more realistic, the dice will be thrown by pressing and shaking the mouse just like how people shake their hands to throw dice while they play original Monopoly.

  The main innovation we offer is incorporating a “Board Maker” mode in our game. This mode will allow users to create unique boards by adding new tiles or editing the existing ones, creating custom decks, and changing the theme and size of the board. In addition, they will be able to change the logic of the game under certain limitations. The users will be able to add multiple custom events to tiles and cards and set when they trigger. For example when the player rests on a tile, it can send them ‘n’ steps forward or make them wait ‘m’ turns or the combination of the two. This process will happen through a GUI and will not require any technical knowledge. The final boards will be stored in single folders and can be shared with other users.
