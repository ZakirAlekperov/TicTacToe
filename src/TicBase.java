/**
 * Игра крестики-нолики на доске 3Х3
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.Font.BOLD;
import static java.awt.Font.MONOSPACED;

public class TicBase implements ActionListener {
    JButton squares[] = new JButton[9];
    JFrame frame;
    JButton newGameButton;
    JPanel windowsContent = new JPanel();
    JPanel playing_field = new JPanel();
    JLabel score;
    int emptySquaresLeft = 9;

    public TicBase() {

        BorderLayout borderLayout = new BorderLayout();
        windowsContent.setLayout(borderLayout);
        windowsContent.setBackground(Color.CYAN);

        //Создаем шрифты для кнопок и метка счета
        Font labelFont = new Font(MONOSPACED, BOLD, 30);
        Font buttonFont = new Font(MONOSPACED, BOLD, 40);

        newGameButton = new JButton("Новая игра");
        newGameButton.setFont(buttonFont);

        //Создаем метку для счета и задаем ее шрифт
        score = new JLabel();
        score.setText("Ваш ход");
        score.setFont(labelFont);

        windowsContent.add("North", newGameButton);
        windowsContent.add("South", score);

        for (int i = 0; i < 9; i++) {
            squares[i] = new JButton("");
        }

        //Размещаем кнопки на игровом поле
        GridLayout gridLayout = new GridLayout(3, 3);
        playing_field.setLayout(gridLayout);

        for (int i = 0; i < squares.length; i++) {
            playing_field.add(squares[i]);
        }

        windowsContent.add("Center", playing_field);
        frame = new JFrame("Крестики-нолики");
        frame.setContentPane(windowsContent);
        frame.setSize(300, 400);
        frame.setVisible(true);

        for (int i = 0; i < squares.length; i++) {
            squares[i].addActionListener(this);
            squares[i].setBackground(Color.ORANGE);
        }
        newGameButton.addActionListener(this);
    }

    /**
     * Этот метод будет обрабатывать все события
     *
     * @param e объект
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton theButton = (JButton) e.getSource();

        //Это кнопка Новая игра?

        if (theButton == newGameButton) {
            for (int i = 0; i < squares.length; i++) {
                squares[i].setEnabled(true);
                squares[i].setText("");
                squares[i].setBackground(Color.ORANGE);
            }
            emptySquaresLeft = 9;
            score.setText("Ваш ход");
            newGameButton.setEnabled(true);

            return;
        }
        String winner = "";

        //Это одна из клеток?

        for (JButton square : squares) {
            if (theButton == square) {
                square.setText("X");
                winner = lookForWinner();

                if (!"".equals(winner)) {
                    endTheGame();
                } else {
                    computerMove();
                    winner = lookForWinner();
                    if (!"".equals(winner)) {
                        endTheGame();
                    }
                }
                break;
            }

        }//Конец цикла for

        if (winner.equals("X")) {
            score.setText("Вы выиграли!");
        } else if (winner.equals("0")) {
            score.setText("Вы проиграли!");
        } else if (winner.equals("T")) {
            score.setText("Ничья!");
        }
    }//Конец метода actionPerformed

    /**
     * Этот метод вызывается после каждого хода, чтобы узнать,
     * есть ли победитель. Он проверяет каждый ряд, колонку и диагональ, чтобы найти
     * три клетки с одинаковыми надписями
     *
     * @return "X", "0", "T" - ничья, "" - еще нет победителя
     */

    String lookForWinner() {
        String theWinner = "";
        emptySquaresLeft--;
        if (emptySquaresLeft == 0) {
            return "T"; //Это ничья, Т от tie
        }

        //Проверяем ряд 1
        if (!squares[0].getText().equals("") &&
                squares[0].getText().equals(squares[1].getText()) &&
                squares[0].getText().equals(squares[2].getText())) {
            theWinner = squares[0].getText();
            hightlightWinner(0, 1, 2);
        }
        //Проверяем ряд 2
        if (!squares[3].getText().equals("") &&
                squares[3].getText().equals(squares[4].getText()) &&
                squares[3].getText().equals(squares[5].getText())) {
            theWinner = squares[3].getText();
            hightlightWinner(3, 4, 5);
        }
        //Проверяем ряд 3
        if (!squares[6].getText().equals("") &&
                squares[6].getText().equals(squares[7].getText()) &&
                squares[6].getText().equals(squares[8].getText())) {
            theWinner = squares[6].getText();
            hightlightWinner(6, 7, 8);
        }
        //Проверяем колонку 1
        if (!squares[0].getText().equals("") &&
                squares[0].getText().equals(squares[3].getText()) &&
                squares[0].getText().equals(squares[6].getText())) {
            theWinner = squares[0].getText();
            hightlightWinner(0, 3, 6);
        }
        //Проверяем колонку 2
        if (!squares[1].getText().equals("") &&
                squares[1].getText().equals(squares[4].getText()) &&
                squares[1].getText().equals(squares[7].getText())) {
            theWinner = squares[1].getText();
            hightlightWinner(1, 4, 7);
        }
        //Проверяем колонку 3
        if (!squares[2].getText().equals("") &&
                squares[2].getText().equals(squares[5].getText()) &&
                squares[2].getText().equals(squares[8].getText())) {
            theWinner = squares[2].getText();
            hightlightWinner(2, 5, 8);
        }
        //Проверяем первую диагональ
        if (!squares[0].getText().equals("") &&
                squares[0].getText().equals(squares[4].getText()) &&
                squares[0].getText().equals(squares[8].getText())) {
            theWinner = squares[0].getText();
            hightlightWinner(0, 4, 8);
        }
        //Проверяем вторую диагональ
        if (!squares[2].getText().equals("") &&
                squares[2].getText().equals(squares[4].getText()) &&
                squares[2].getText().equals(squares[6].getText())) {
            theWinner = squares[2].getText();
            hightlightWinner(2, 4, 6);
        }
        return theWinner;
    }

    /**
     * Этот метод принимает набор правил, чтобы найти лучший компьютерный ход.
     * Если хороштй ход не найден, выбирается случайная клетка
     */

    void computerMove() {
        int selectedSquare;

        //Сначала компьютер пытается найти пустую клетку
        //рядом с двумя клетками с ноликами, чтобы выиграть

        selectedSquare = findEmptySquare("0");
        //Если он не может найти два нолика, то хотя бы
        //Попытается не дать выиграть опоненту
        if (selectedSquare == -1) {
            selectedSquare = findEmptySquare("X");
        }
        //Если selectedSquare по прежнему равен -1, то попытается занять центр

        if ((selectedSquare == -1) && (squares[4].getText().equals(""))) {
            selectedSquare = 4;
        }
        //Иначе - занимаем случайню клетку
        if (selectedSquare == -1) {
            selectedSquare = getRandomSquare();
        }
        squares[selectedSquare].setText("0");
    }

    /**
     * Этот метод проверяет каждый ряд, колонку и диагональ чтобы узнать, есть ли в ней две клетки
     * с одинаковыми надписями и пустой клеткой.
     *
     * @param args передается Х - для пользователя и 0 для компьютера
     * @return количество свободных клеток
     * или -1 если не найдено две клетки с одинаковым надписями
     */

    int findEmptySquare(String player) {
        int weight[] = new int[9];

        for (int i = 0; i < weight.length; i++) {
            if (squares[i].getText().equals("0")) {
                weight[i] = -1;
            } else if (squares[i].getText().equals("X")) {
                weight[i] = 1;
            } else {
                weight[i] = 0;
            }
        }
        int twoWeights = player.equals("0") ? -2 : 2;

        //Проверяем есть ли в ряду 1 две одинаковые клетки и одна пустая

        if (weight[0] + weight[1] + weight[2] == twoWeights) {
            if (weight[0] == 0) {
                return 0;
            } else if (weight[1] == 0) {
                return 1;
            } else {
                return 2;
            }
        }
        //Проверяем есть ли в ряду 2 две одинаковые клетки и одна пустая

        if (weight[3] + weight[4] + weight[5] == twoWeights) {
            if (weight[3] == 0) {
                return 3;
            } else if (weight[4] == 0) {
                return 4;
            } else {
                return 5;
            }
        }
        //Проверяем есть ли в ряду 3 две одинаковые клетки и одна пустая

        if (weight[6] + weight[7] + weight[8] == twoWeights) {
            if (weight[6] == 0) {
                return 6;
            } else if (weight[7] == 0) {
                return 7;
            } else {
                return 8;
            }

        }
        //Проверяем есть ли в колонке 1 две одинаковые клетки и одна пустая

        if (weight[0] + weight[3] + weight[6] == twoWeights) {
            if (weight[0] == 0) {
                return 0;
            } else if (weight[3] == 0) {
                return 3;
            } else {
                return 6;
            }
        }

            //Проверяем есть ли в колонке 2 две одинаковые клетки и одна пустая

            if (weight[1] + weight[4] + weight[7] == twoWeights) {
                if (weight[1] == 0) {
                    return 1;
                } else if (weight[4] == 0) {
                    return 4;
                } else {
                    return 7;
                }
            }

            //Проверяем есть ли в колонке 3 две одинаковые клетки и одна пустая

            if (weight[2] + weight[5] + weight[8] == twoWeights) {
                if (weight[2] == 0) {
                    return 2;
                } else if (weight[5] == 0) {
                    return 5;
                } else {
                    return 8;
                }
            }

            //Проверяем есть ли в диагонали 1 две одинаковые клетки и одна пустая

            if (weight[0] + weight[4] + weight[8] == twoWeights) {
                if (weight[0] == 0) {
                    return 0;
                } else if (weight[4] == 0) {
                    return 4;
                } else {
                    return 8;
                }
            }

            //Проверяем есть ли в диагонали 2 две одинаковые клетки и одна пустая

            if (weight[2] + weight[4] + weight[6] == twoWeights) {
                if (weight[2] == 0) {
                    return 2;
                } else if (weight[4] == 0) {
                    return 4;
                } else {
                    return 6;
                }
            }
            //Не еайдено двух соседних одинаковых клеток

            return -1;
        }

    /**
     * Этот метод выбирает любую пустую клетку
      * @return случайно выбранный номер клетки
     */

    int getRandomSquare(){

        boolean gotEmptySquare = false;

        int selectedSquare = -1;

        do{
            selectedSquare = (int) (Math.random()*9);
            if(squares[selectedSquare].getText().equals("")){
                gotEmptySquare = true; // чтобы выйти из цикла
            }
        }while (!gotEmptySquare);

        return selectedSquare;
    }//конец метода getRandomSquare()

    /**
     * Этот метод выделяет выигрывшую линию
     * @param args клетки которые нужны выделить
     */
    void hightlightWinner(int win1, int win2, int win3){
        squares[win1].setBackground(Color.CYAN);
        squares[win2].setBackground(Color.CYAN);
        squares[win3].setBackground(Color.CYAN);
    }

    //делаем недоступными клеткии доступной кнопку "Новая игра"
    void endTheGame(){

        newGameButton.setEnabled(true);

        for (int i = 0; i < squares.length; i++) {
            squares[i].setEnabled(false);
        }
    }

    public static void main (String[]args){
                    TicBase ticBase = new TicBase();
                }
}
