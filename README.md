﻿# Connect Four Game 

Implementation of a Connect Four game for 2 players using JavaFX, following the Model-View-Controller (MVC) design pattern:

* **Model:** `ConnectFourLogic` handles the game rules, board state, and win conditions.
* **View:** The user interface is defined in the `ConnectFour.fxml` file.
* **Controller:** `ConnectFourController` manages user interactions and updates the UI.

![Connect Four Game Demo](https://github.com/omer-lebel/Connect-Four-JavaFx/blob/master/demo.gif?raw=true)


## Running the Game

To run the game, make sure you have [JDK 11](https://jdk.java.net/) or higher and [JavaFX SDK](https://openjfx.io/) installed. Them from your command line:
> Replace `"/path/to/javafx-sdk/lib"` with the actual path to your JavaFX SDK lib directory.

```bash

# Compile
javac --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml *.java

# Run
java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml ConnectFourApp

```


## License

[MIT](https://choosealicense.com/licenses/mit/)

