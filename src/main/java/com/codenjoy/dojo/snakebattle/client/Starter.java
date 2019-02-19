package com.codenjoy.dojo.snakebattle.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.snakebattle.client.smart.analyze.Action;
import com.codenjoy.dojo.snakebattle.client.smart.analyze.Priority;
import com.codenjoy.dojo.snakebattle.client.smart.analyze.StateOfBoard;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.Snake;


import java.awt.*;
import java.util.*;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.snakebattle.model.Elements.*;


public class Starter {
    private Board board;

    private Point lastPosition = new Point();
    private Direction lastAnswer = STOP;

    private int countBotsMove = 0;

    private boolean isBotsRunAway() {
        Settings s = Settings.getInstance();
        if (board.getSleepHeads() == s.ENEMY_MAX_COUNT) countBotsMove = 0;
        if (board.getRightHeads() == s.ENEMY_MAX_COUNT) countBotsMove++;
        else countBotsMove = 0;

        if (countBotsMove > s.BOTS_COUNT_MOVE) {
            System.out.println("ТУТ ВСЕ БОТЫ, ВАЛИМ");
            return true;
        }
        return false;
    }

    public String getAction() {
        Settings s = Settings.getInstance();
        String log = "[Get Action]";

        String result = "";
        Direction direction;

        //проверка на ботов. Ливаем комнату если они есть
        if (isBotsRunAway()) {
            result = "ACT(0)";
            direction = STOP;
        } else if (board.isGameOver()) {
            //если вдруг не парсится голова
            s.update();
            System.out.println(log + "Типо конец, но мы походим");

            Direction d = lastAnswer;

            Point p = new Point(d.changeX(lastPosition.x), d.changeY(lastPosition.y));
            if (d == STOP) {
                result = "";
            } else
                for (int i = 0; i < 4; i++) {
                    if (board.isBarrierAt(d.changeX(p.x), d.changeY(p.y))) d = d.clockwise();
                    else break;
                }
            direction = d;
        } else {
            StateOfBoard state = board.analyzeBoard();

            Calendar start = Calendar.getInstance();
            Direction newAnalyze = Action.getInstance().getNextMove();
            System.out.println("Ходим: " + newAnalyze);
            System.out.printf("Время всего на анализ и просчет хода: %dms %n", Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis());

            direction = newAnalyze;

            lastPosition = state.getMySnake().getHead();


        }
        lastAnswer = direction;

        //тут можно вставить выкидывание камней под яростью, если нужно
        if (direction==STOP) return result;else return direction.toString();
    }


    public Starter(Board board) {
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}

