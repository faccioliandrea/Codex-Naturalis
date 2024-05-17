package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.view.gui.components.BoardGridPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OpponentBoardController implements Initializable {
    @FXML
    Group boardGroup;
    @FXML
    Label titleLabel;

    private String opponentName;
    private ArrayList<CardInfo> board;

    public OpponentBoardController(String opponentName, ArrayList<CardInfo> board) {
        this.opponentName = opponentName;
        this.board = board;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BoardGridPane boardGridPane = new BoardGridPane(0.8);
        boardGroup.getChildren().add(boardGridPane);
        titleLabel.setText(opponentName + "'s board");
        boardGridPane.setupBoard(board, false);
    }
}
