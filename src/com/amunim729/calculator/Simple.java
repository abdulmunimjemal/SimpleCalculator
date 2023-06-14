package com.amunim729.calculator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;

public class Simple{

    private JFrame frame;
    private JPanel display;
    private JTextArea view;
    private JPanel bottons;
    JButton buttons[];

    public static void main(String[] args) {
        Simple simple = new Simple();
        simple.displayCalc();
    }

    public void displayCalc() {
        // Build the grame
        frame = new JFrame();
        frame.setSize(330, 500);
        frame.setTitle("Simple Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon image = new ImageIcon("C:\\Users\\Abdulmunim\\Desktop\\Java\\Calculator\\src\\com\\amunim729\\calculator\\icon.jpg");
        frame.setIconImage(image.getImage());
        frame.setResizable(false);
        frame.setBackground(new Color(45, 45, 45));

        // build the screen panel
        display = new JPanel();
        view = new JTextArea();
        view.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        view.setFont(new Font("Agency FB", Font.PLAIN, 25));
        view.setText("0");
        view.setLineWrap(true);
        view.setEditable(false);
//        view.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
        view.setColumns(21);
        view.setRows(2);
        display.setBackground(new Color(45,45,45));
        display.add(view);
        frame.getContentPane().add(BorderLayout.NORTH, display);

        // build bottons panel
        bottons = new JPanel();
        bottons.setLayout(new GridLayout(5,4));

        buttons = new JButton[20];
        buttons[2] = makeButton("0");
        buttons[3] = makeButton("OFF");
        buttons[0] = makeButton("=");
        buttons[1] = makeButton("DEL");
        buttons[16] = makeButton("/");
        buttons[4] = makeButton("+");
        buttons[8] = makeButton("-");
        buttons[12] = makeButton("*");

        buttons[7] = makeButton("1");
        buttons[6] = makeButton("2");
        buttons[5] = makeButton("3");

        buttons[11] = makeButton("4");
        buttons[10] = makeButton("5");
        buttons[9] = makeButton("6");

        buttons[15] = makeButton("7");
        buttons[14] = makeButton("8");
        buttons[13] = makeButton("9");

        buttons[17] = makeButton("CLS");
        buttons[18] = makeButton(")");
        buttons[19] = makeButton("(");


        for (int i = 20; i > 0; i--) {
            bottons.add(buttons[i-1]);
        }

        frame.getContentPane().add(BorderLayout.CENTER, bottons);

        frame.getContentPane().setBackground(new Color(45,45,45));
        frame.setVisible(true);

    }

    private JButton makeButton(String val) {
        JButton buttons = new JButton(val);
        buttons.setBackground(new Color(80,80,80));
        buttons.setForeground(Color.CYAN);
        buttons.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        if (val.equals("=")) {
            buttons.addActionListener(event -> calculate());
        } else if (val.equals("CLS")){
            buttons.addActionListener(event -> view.setText("0"));
        } else if (val.equals("OFF") || val.equals("ON")){
            buttons.addActionListener(event -> switchOff(buttons.getText()));
        } else if (val.equals("DEL")){
            buttons.addActionListener(event -> view.setText(view.getText().substring(0,view.getText().length()-1)));
        } else {
            buttons.addActionListener((event) -> {
                if (view.getText().equals("0")) {
                    view.setText(val);
                } else {
                    view.setText(view.getText() + val);
                }
            });
        }
        return buttons;
    }

    public void calculate() {
        String s = view.getText();
        Map<Character, Integer> operators = new HashMap<>();
        operators.put('+', 1);
        operators.put('-', 1);
        operators.put('*', 2);
        operators.put('/', 2);

        Stack<Integer> operands = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isDigit(ch)) {
                num = num * 10 + (ch - '0');
                if (i + 1 == s.length() || !Character.isDigit(s.charAt(i + 1))) {
                    operands.push(num);
                    num = 0;
                }
            } else if (ch == '(') {
                operatorStack.push(ch);
            } else if (ch == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    applyOperator(operands, operatorStack);
                }
                operatorStack.pop(); // Discard '('
            } else if (operators.containsKey(ch)) {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '('
                        && operators.get(operatorStack.peek()) >= operators.get(ch)) {
                    applyOperator(operands, operatorStack);
                }
                operatorStack.push(ch);
            }
        }
        while (!operatorStack.isEmpty()) {
            applyOperator(operands, operatorStack);
        }
        view.setText(Integer.toString(operands.pop()));
    }

    private static void applyOperator(Stack<Integer> operands, Stack<Character> operators) {
        char operator = operators.pop();
        int rightOperand = operands.pop();
        int leftOperand = operands.pop();
        int result = 0;
        switch (operator) {
            case '+':
                result = leftOperand + rightOperand;
                break;
            case '-':
                result = leftOperand - rightOperand;
                break;
            case '*':
                result = leftOperand * rightOperand;
                break;
            case '/':
                result = leftOperand / rightOperand;
                break;
        }
        operands.push(result);
    }

    public void switchOff(String val) {
        if (val.equals("OFF")) {
            for (JButton button : buttons) {
                if (button.getText().equals("OFF")) {
                    button.setText("ON");
                } else {
                    button.setEnabled(false);
                }
            }
        } else {
            for (JButton button : buttons) {
                if (button.getText().equals("ON")) {
                    button.setText("OFF");
                } else {
                    button.setEnabled(true);
                }
            }
        }
    }
}