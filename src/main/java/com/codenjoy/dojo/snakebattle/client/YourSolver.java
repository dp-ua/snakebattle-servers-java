package com.codenjoy.dojo.snakebattle.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

import java.util.Calendar;

import static java.lang.Thread.sleep;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    private Starter starter;


    public YourSolver(Dice dice) {
        this.dice = dice;
        starter = new Starter(null);
    }

    @Override
    public String get(Board board) {
        this.board = board;
        starter.setBoard(board);

        //if (board.isGameOver()) return "";

        String answer = "";
        Calendar start = Calendar.getInstance();
        answer = starter.getAction();
        System.out.println("Время принятия решения: " + (Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis()) + "ms");
        System.out.println("[Решение] " + answer);
        return answer;

    }


    public static void main(String[] args) throws InterruptedException {

        Settings s = Settings.getInstance();

        System.out.println(s.URL);
        int count = 0;

        while (count < s.CONNECTION) {
            try {
                MyRunner.runClient(
                        // paste here board page url from browser after registration
                        s.URL,
                        new YourSolver(new RandomDice()),
                        new Board());
                break;
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println("[Главная] делаю реконнект");
                sleep(s.TIMEOUT);
            }
        }
    }

    class MyRunner extends WebSocketRunner {
        public MyRunner(Solver solver, ClientBoard board) {
            super(solver, board);
            ATTEMPTS = 100;

        }


    }

}
