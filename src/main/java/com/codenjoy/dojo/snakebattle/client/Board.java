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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.client.smart.analyze.Field;
import com.codenjoy.dojo.snakebattle.client.smart.analyze.StateOfBoard;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.Snake;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.snakebattle.model.Elements.*;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {


    public int getSleepHeads() {
        return get(ENEMY_HEAD_SLEEP).size();
    }

    public int getRightHeads() {
        return get(ENEMY_HEAD_RIGHT).size();
    }

    private LinkedList<java.awt.Point> getEnemyBodyByHead(Point head) {
        LinkedList<java.awt.Point> list = new LinkedList<>();
        Direction d = STOP;
        switch (getAt(head)) {
            case ENEMY_HEAD_DOWN:
                d = DOWN;
                break;
            case ENEMY_HEAD_UP:
                d = UP;
                break;
            case ENEMY_HEAD_LEFT:
                d = LEFT;
                break;
            case ENEMY_HEAD_RIGHT:
                d = RIGHT;
                break;
        }

        list.add(new java.awt.Point(head.getX(), head.getY()));

        if (d == STOP) {
            list.addAll(getEnemyBodyByHead_OLD(head));
        } else {
            int count = 0;
            boolean breakW = false;
            boolean err = false;
            int x = head.getX();
            int y = head.getY();
            while (!breakW) {

                x = d.inverted().changeX(x);
                y = d.inverted().changeY(y);
                Elements e = getAt(x, y);
                java.awt.Point p = new java.awt.Point(x, y);

                switch (e) {
                    case ENEMY_HEAD_DOWN:
                    case ENEMY_HEAD_LEFT:
                    case ENEMY_HEAD_RIGHT:
                    case ENEMY_HEAD_UP:
                    case ENEMY_HEAD_DEAD:
                    case ENEMY_HEAD_EVIL:
                    case ENEMY_HEAD_FLY:
                    case ENEMY_HEAD_SLEEP:
                        breakW = true;
                        err = true;
                    case ENEMY_BODY_HORIZONTAL:
                    case ENEMY_BODY_VERTICAL:
                        break;
                    case ENEMY_BODY_LEFT_DOWN:
                        d = d == LEFT ? UP : RIGHT;
                        break;
                    case ENEMY_BODY_LEFT_UP:
                        d = d == LEFT ? DOWN : RIGHT; //
                        break;
                    case ENEMY_BODY_RIGHT_DOWN:
                        d = d == RIGHT ? UP : LEFT; //
                        break;
                    case ENEMY_BODY_RIGHT_UP:
                        d = d == RIGHT ? DOWN : LEFT;//
                        break;
                    case ENEMY_TAIL_END_DOWN:
                    case ENEMY_TAIL_END_LEFT:
                    case ENEMY_TAIL_END_UP:
                    case ENEMY_TAIL_END_RIGHT:
                    case ENEMY_TAIL_INACTIVE:
                        breakW = true;
                        break;
                    default:
                        err = true;
                        breakW = true;
                        System.out.println("[Парсинг чужой змеи] Получен элемент " + e);
                }
                count++;
                if (!err || count > 1000) {
                    list.add(p);
                    x = p.x;
                    y = p.y;
                } else {
                    System.out.println("[Парсинг чужой змеи] Неудача. Взял старый алгоритм. ");
                    return getEnemyBodyByHead_OLD(head);
                }
            }

        }
        return list;
    }

    private LinkedList<java.awt.Point> getEnemyBodyByHead_OLD(Point head) {
        char[][] field = getField();

        LinkedList<java.awt.Point> list = new LinkedList<>();
        list.add(new java.awt.Point(head.getX(), head.getY()));

        int x = head.getX();
        int y = head.getY();


        boolean goNext = true;
        int count = 0;
        while (goNext) {
            if (count > 1000) {
                System.out.println("[Просчет длины противника] Ушел в цикл. Ошибка");
                break;
            }


            List<java.awt.Point> around = new LinkedList<>();
            around.add(new java.awt.Point(x + 1, y));
            around.add(new java.awt.Point(x, y - 1));
            around.add(new java.awt.Point(x - 1, y));
            around.add(new java.awt.Point(x, y + 1));


            boolean getHead = false;
            boolean goodADD = false;
            Elements now = Elements.valueOf(field[x][y]);

            for (java.awt.Point p : around) {
                if (p.x < 0 || p.x > 29) continue;
                if (p.y < 0 || p.y > 29) continue;
                if (list.contains(p)) continue;

                Elements next = Elements.valueOf(field[p.x][p.y]);
                if (!next.isEnemy()) continue;
                if (!now.canBeNext(next)) continue;
                if (next.isEnemyHead()) getHead = true;
                if (next.isEnemyBody()) {
                    list.add(p);
                    x = p.x;
                    y = p.y;
                    goodADD = true;
                    break;
                }
                if (next.isEnemyTail()) {
                    list.add(p);
                    goNext = false;
                    goodADD = true;
                    break;
                }
            }
            if (getHead && !goodADD) {
                System.out.println("[Просчет длины противника] Ошибка. Встретилась еще одна голова в конце");
                goNext = false;
            }
            count++;
        }
        return list;
    }


    public StateOfBoard analyzeBoard() {
        StateOfBoard state = StateOfBoard.getInstance();
        Settings s = Settings.getInstance();

        state.setEnemiesSize(getEnemySnakesSize());
        char[][] f = getField();


        //загружаем и применяем карту запретных точек
        for (java.awt.Point p : s.BLOCKS) {
            if (p.x < 0 || p.x > 29 || p.y < 0 || p.y > 29) continue;
            f[p.x][p.y] = OTHER.ch();
        }

        state.setField(f);
        try {
            state.setHead(getAt(getMe()));
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("[Не могу спарсить свою голову]");
        }

        Snake me = new Snake(getMySnake());

        me.setHeadType(getAt(getMe()));
        switch (me.getHeadType()) {
            case HEAD_EVIL:
                me.setEvil(true);
                break;
            case HEAD_DOWN:
                me.setDirection(DOWN);
                break;
            case HEAD_RIGHT:
                me.setDirection(RIGHT);
                break;
            case HEAD_UP:
                me.setDirection(UP);
                break;
            case HEAD_LEFT:
                me.setDirection(LEFT);
                break;
        }

        state.setMySnake(me);


        int tempSize = 0;
        List<Snake> enemies = new ArrayList<>();
        for (Point p : getEnemyHeads()) {
            Snake enemy = new Snake(getEnemyBodyByHead(p));
            enemy.setHeadType(getAt(p));
            switch (enemy.getHeadType()) {

                case ENEMY_HEAD_EVIL:
                    enemy.setEvil(true);
                    break;
                case ENEMY_HEAD_DOWN:
                    enemy.setDirection(DOWN);
                    break;
                case ENEMY_HEAD_RIGHT:
                    enemy.setDirection(RIGHT);
                    break;
                case ENEMY_HEAD_UP:
                    enemy.setDirection(UP);
                    break;
                case ENEMY_HEAD_LEFT:
                    enemy.setDirection(LEFT);
                    break;
            }
            enemies.add(enemy);
            tempSize += enemy.getSize();
        }

        state.setEnemies(enemies);
        state.setEnemiesSize(getEnemySnakesSize());

        state.setErrorEnemySize(state.getEnemiesSize() != tempSize);
        if (state.isErrorEnemySize())
            System.out.println("[GET STATE] Длина змей. у меня: " + tempSize + " Всего: " + state.getEnemiesSize());

        Field field = Field.getInstance();
        field.setInField(f);
        Calendar start = Calendar.getInstance();
        field.analyze();
        System.out.printf("Время на анализ карты: %dms %n", Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis());
        //field.printField();

        return state;
    }


    @Override
    public char[][] getField() {
        char[][] field = new char[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                field[i][j] = getAt(i, j).ch();
            }
        }
        return field;
    }

    public Elements[][] getFieldElements() {
        Elements[][] field = new Elements[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                field[i][j] = getAt(i, j);
            }
        }
        return field;
    }

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {

        return isAt(x, y, WALL, START_FLOOR, ENEMY_HEAD_SLEEP, ENEMY_TAIL_INACTIVE, TAIL_INACTIVE);
    }

    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    public Point getMe() {
        return getMyHead().get(0);
    }

    public boolean isGameOver() {
        return getMyHead().isEmpty();
    }


    private List<Point> getEnemyHeads() {
        return get(ENEMY_HEAD_DOWN, ENEMY_HEAD_LEFT, ENEMY_HEAD_RIGHT, ENEMY_HEAD_UP,
                ENEMY_HEAD_EVIL, ENEMY_HEAD_FLY, ENEMY_HEAD_SLEEP, ENEMY_HEAD_DEAD);
    }

    private List<Point> getMyHead() {
        return get(HEAD_DOWN, HEAD_LEFT, HEAD_RIGHT, HEAD_UP, HEAD_SLEEP, HEAD_EVIL, HEAD_FLY);
    }

    public boolean isStoneAt(int x, int y) {
        return isAt(x, y, STONE);
    }


    public LinkedList<java.awt.Point> getMySnake() {
        LinkedList<java.awt.Point> list = new LinkedList<>();
        java.awt.Point me = new java.awt.Point(getMe().getX(), getMe().getY());
        char[][] field = getField();

        list.add(me);


        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                switch (Elements.valueOf(field[i][j])) {

                    case TAIL_END_DOWN:
                    case TAIL_END_LEFT:
                    case TAIL_END_UP:
                    case TAIL_END_RIGHT:
                    case TAIL_INACTIVE:
                    case BODY_HORIZONTAL:
                    case BODY_VERTICAL:
                    case BODY_LEFT_DOWN:
                    case BODY_LEFT_UP:
                    case BODY_RIGHT_DOWN:
                    case BODY_RIGHT_UP:
                        list.add(new java.awt.Point(i, j));
                        break;
                    default:
                }
            }
        }

        return list;
    }


    public int getMySnakeSize() {

        int count = 0;
        char[][] field = getField();
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                switch (Elements.valueOf(field[i][j])) {
                    case HEAD_LEFT:
                    case HEAD_RIGHT:
                    case HEAD_UP:
                    case HEAD_DEAD:
                    case HEAD_EVIL:
                    case HEAD_FLY:
                    case HEAD_SLEEP:
                    case TAIL_END_DOWN:
                    case TAIL_END_LEFT:
                    case TAIL_END_UP:
                    case TAIL_END_RIGHT:
                    case TAIL_INACTIVE:
                    case BODY_HORIZONTAL:
                    case BODY_VERTICAL:
                    case BODY_LEFT_DOWN:
                    case BODY_LEFT_UP:
                    case BODY_RIGHT_DOWN:
                    case BODY_RIGHT_UP:
                        count++;
                        break;
                    default:
                }
            }
        }
        return count;
    }

    public Elements getAt(java.awt.Point point) {
        return getAt(point.x, point.y);
    }

    public int getEnemySnakesSize() {
        int count = 0;
        char[][] field = getField();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                switch (Elements.valueOf(field[i][j])) {
                    case ENEMY_HEAD_DOWN:
                    case ENEMY_HEAD_LEFT:
                    case ENEMY_HEAD_RIGHT:
                    case ENEMY_HEAD_UP:
                    case ENEMY_HEAD_DEAD:
                    case ENEMY_HEAD_EVIL:
                    case ENEMY_HEAD_FLY:
                    case HEAD_EVIL:
                    case HEAD_FLY:
                    case HEAD_SLEEP:
                    case HEAD_DEAD:
                    case ENEMY_HEAD_SLEEP:
                    case ENEMY_TAIL_END_DOWN:
                    case ENEMY_TAIL_END_LEFT:
                    case ENEMY_TAIL_END_UP:
                    case ENEMY_TAIL_END_RIGHT:
                    case ENEMY_TAIL_INACTIVE:
                    case ENEMY_BODY_HORIZONTAL:
                    case ENEMY_BODY_VERTICAL:
                    case ENEMY_BODY_LEFT_DOWN:
                    case ENEMY_BODY_LEFT_UP:
                    case ENEMY_BODY_RIGHT_DOWN:
                    case ENEMY_BODY_RIGHT_UP:
                        count++;
                        break;
                }
            }
        }
        return count;
    }

}
