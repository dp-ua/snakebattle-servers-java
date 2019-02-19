package com.codenjoy.dojo.snakebattle.client.smart.analyze;

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


import com.codenjoy.dojo.snakebattle.client.Settings;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.Snake;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StateOfBoard {
    private static StateOfBoard mInstance;
    private int enemiesSize;
    private Snake mySnake;
    private List<Snake> enemies;
    private Elements head;
    private char[][] field;
    private boolean errorEnemySize;
    private int rangeX;
    private int rangeY;

    private StateOfBoard() {
    }

    public static StateOfBoard getInstance() {
        if (mInstance == null) {
            mInstance = new StateOfBoard();
        }
        return mInstance;
    }

public Snake getClosestEnemyToPoint(Point point) {
    Snake me = getMySnake();
    List<Snake> list = getEnemies();
    Settings s = Settings.getInstance();

    Snake res = new Snake();
    double minDist = Double.POSITIVE_INFINITY;

    for (Snake snake : list) {
        double d = point.distance(snake.getHead());
            if (d < minDist) {
                minDist = d;
                res = snake;

            }
        }
    return res;
    }

    public int getRangeX() {
        return rangeX;
    }

    public Snake getNearestSmallSnake() {
        Snake me = getMySnake();
        List<Snake> list = getEnemies();
        Settings s = Settings.getInstance();

        double minDist = s.ATTACK_DISTANCE_SCAN + 5;
        Snake res = new Snake();
        if (list.size() == 0) return res;
        for (Snake snake : list) {
            if (isMeStronger(snake)) {
                double d = me.getDistance(snake);
                if (d < s.ATTACK_DISTANCE_SCAN && d < minDist) {
                    minDist = d;
                    res = snake;

                }
            }
        }
        return res;

    }

    public List<Snake> getSnakeAttackMe() {
        Snake me = getMySnake();
        List<Snake> list = new ArrayList<>();
        if (me.getDirection() == null) return list;
        for (Snake s : getEnemies()) {
            if (!(me.getDirection() == s.getDirection())) continue;
            List<Elements> l = new ArrayList<>();
            switch (me.getDirection()) {
                case UP:
                case DOWN:
                    l.add(Elements.valueOf(field[s.getHead().x - 1][s.getHead().y]));
                    l.add(Elements.valueOf(field[s.getHead().x + 1][s.getHead().y]));
//                    l.add(Elements.valueOf(field[s.getHead().x - 2][s.getHead().y]));
//                    if (s.getHead().x < 28) {
//                        l.add(Elements.valueOf(field[s.getHead().x + 2][s.getHead().y]));
//                    }

                    break;
                case LEFT:
                case RIGHT:
                    l.add(Elements.valueOf(field[s.getHead().x][s.getHead().y + 1]));
                    l.add(Elements.valueOf(field[s.getHead().x][s.getHead().y - 1]));
//                    if (s.getHead().y > 1) l.add(Elements.valueOf(field[s.getHead().x][s.getHead().y - 2]));
//                    if (s.getHead().y < 28) l.add(Elements.valueOf(field[s.getHead().x][s.getHead().y + 2]));

                    break;
            }
            if (l.contains(Elements.BODY_HORIZONTAL) || l.contains(Elements.BODY_VERTICAL)) list.add(s);
        }
        return list;
    }

    public Elements getElementByPoint(Point point) {
        Elements result = Elements.OTHER;
        try {
            result = Elements.valueOf(getField()[point.x][point.y]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return result;
    }

    public void setRange(int rangeX, int rangeY) {
        this.rangeX = rangeX;
        this.rangeY = rangeY;
    }

    public int getRangeY() {
        return rangeY;
    }

    public void setRangeY(int rangeY) {
        this.rangeY = rangeY;
    }

    public Snake getLongetsEnemy() {
        Snake longest = new Snake();

        for (Snake s : enemies) {
            if (longest.compare(s) > 0) {
                longest = s;
            }
        }
        return longest;
    }

    public int getEnemyCount() {
        return enemies.size();
    }

    public boolean isMeStronger(Snake enemy) {
        if (getMySnake().isEvil()) {
            if (!enemy.isEvil()) return true;
        } else if (enemy.isEvil()) return false;
        if (getMySnake().compare(enemy) >= 2 && !isErrorEnemySize()) return true;

        if (getEnemyCount() == 1 && getMySnake().getSize() > enemiesSize + 2) return true;

        else return false;
    }

    public boolean isErrorEnemySize() {
        return errorEnemySize;
    }

    public void setErrorEnemySize(boolean errorEnemySize) {
        this.errorEnemySize = errorEnemySize;
    }

    public Snake getSnakeByPart(Point point) {
        for (Snake s : getEnemies()) {
            if (s.isThisSnake(point)) return s;
        }
        return new Snake(mySnake.getSize() + 5);
    }

    public int getEnemiesSize() {
        return enemiesSize;
    }

    public void setEnemiesSize(int enemiesSize) {
        this.enemiesSize = enemiesSize;
    }

    public Snake getMySnake() {
        return mySnake;
    }

    public void setMySnake(Snake mySnake) {
        this.mySnake = mySnake;
    }

    public List<Snake> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Snake> enemies) {
        this.enemies = enemies;
    }

    public Elements getHead() {
        return head;
    }


    public void setHead(Elements head) {
        this.head = head;
    }

    public char[][] getField() {
        return field;
    }

    public void setField(char[][] field) {
        this.field = field;
        setRange(field.length,field.length);
        if (Settings.getInstance().WRITE_BOARD) {
            for (int i = 29; i >= 0; i--) {
                System.out.printf("%2d", +i);
                for (int j = 0; j <= 29; j++) {
                    System.out.print(field[j][i]);
                }
                System.out.println();

            }
        }

    }
}
