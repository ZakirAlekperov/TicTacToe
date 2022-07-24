/**
 * Игра крестики-нолики на доске 3х3
 */

import javax.swing.*;
import java.awt.*;

import static java.awt.Font.*;

public class TicTacToeGUI {

    JPanel windowsContent;
    JPanel playing_field;
    JButton[] squares = new JButton[9];
    JFrame frame;
    JButton newGameButton;
    JLabel score;

    int emptySquaresLeft = 9;

    public TicTacToeGUI(){

        windowsContent = new JPanel();

        BorderLayout borderLayout = new BorderLayout();
        windowsContent.setLayout(borderLayout);

        Font labelFont = new Font(MONOSPACED, BOLD, 30);
        Font buttonFont = new Font(MONOSPACED, BOLD, 40);

        newGameButton = new JButton("Новая игра");
        newGameButton.setFont(buttonFont);
        score = new JLabel();
        score.setText("0");
        score.setFont(labelFont);


        windowsContent.add("North", newGameButton);
        windowsContent.add("South", score);

        for(int i=0; i<9; i++){
            squares[i] = new JButton("");
        }

        playing_field = new JPanel();
        GridLayout gridLayout = new GridLayout(3,3);
        playing_field.setLayout(gridLayout);

        for(int i=0; i<squares.length; i++){
            playing_field.add(squares[i]);
        }

        windowsContent.add("Center", playing_field);
        frame = new JFrame("Крестики-нолики");
        frame.setContentPane(windowsContent);
        frame.setSize(300, 400);
        frame.setVisible(true);

        TicTacToeEngine ticTacToeEngine = new TicTacToeEngine(this);

        for(int i=0; i< squares.length; i++){
            squares[i].addActionListener(ticTacToeEngine);
            squares[i].setBackground(Color.ORANGE);
        }
        newGameButton.addActionListener(ticTacToeEngine);
    }

    public static void main(String[] args) {
        TicTacToeGUI ticTacToeGUI = new TicTacToeGUI();
    }
}
