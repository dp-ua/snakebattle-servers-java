package com.codenjoy.dojo.snakebattle.model;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.printer.CharElements;

import java.awt.*;
import java.util.LinkedList;

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Elements implements CharElements {

    NONE(' '),         // пустое место
    WALL('☼'),         // а это стенка
    START_FLOOR('#'),  // место старта змей
    OTHER('?'),        // этого ты никогда не увидишь :)

    APPLE('○'),        // яблоки надо кушать от них становишься длинее
    STONE('●'),        // а это кушать не стоит - от этого укорачиваешься
    FLYING_PILL('©'),  // таблетка полета - дает суперсилы
    FURY_PILL('®'),    // таблетка ярости - дает суперсилы
    GOLD('$'),         // золото - просто очки

    // голова твоей змеи в разных состояниях и напрвлениях
    HEAD_DOWN('▼'),
    HEAD_LEFT('◄'),
    HEAD_RIGHT('►'),
    HEAD_UP('▲'),
    HEAD_DEAD('☻'),    // этот раунд ты проиграл
    HEAD_EVIL('♥'),    // ты скушал таблетку ярости
    HEAD_FLY('♠'),     // ты скушал таблетку полета
    HEAD_SLEEP('&'),   // твоя змейка ожидает начала раунда

    // хвост твоей змейки
    TAIL_END_DOWN('╙'),
    TAIL_END_LEFT('╘'),
    TAIL_END_UP('╓'),
    TAIL_END_RIGHT('╕'),
    TAIL_INACTIVE('~'),

    // туловище твоей змейки
    BODY_HORIZONTAL('═'),
    BODY_VERTICAL('║'),
    BODY_LEFT_DOWN('╗'),
    BODY_LEFT_UP('╝'),
    BODY_RIGHT_DOWN('╔'),
    BODY_RIGHT_UP('╚'),

    // змейки противников
    ENEMY_HEAD_DOWN('˅'),
    ENEMY_HEAD_LEFT('<'),
    ENEMY_HEAD_RIGHT('>'),
    ENEMY_HEAD_UP('˄'),
    ENEMY_HEAD_DEAD('☺'),   // этот раунд противник проиграл
    ENEMY_HEAD_EVIL('♣'),   // противник скушал таблетку ярости
    ENEMY_HEAD_FLY('♦'),    // противник скушал таблетку полета
    ENEMY_HEAD_SLEEP('ø'),  // змейка противника ожидает начала раунда

    // хвосты змеек противников
    ENEMY_TAIL_END_DOWN('¤'),
    ENEMY_TAIL_END_LEFT('×'),
    ENEMY_TAIL_END_UP('æ'),
    ENEMY_TAIL_END_RIGHT('ö'),
    ENEMY_TAIL_INACTIVE('*'),

    // туловище змеек противников
    ENEMY_BODY_HORIZONTAL('─'),
    ENEMY_BODY_VERTICAL('│'),
    ENEMY_BODY_LEFT_DOWN('┐'),
    ENEMY_BODY_LEFT_UP('┘'),
    ENEMY_BODY_RIGHT_DOWN('┌'),
    ENEMY_BODY_RIGHT_UP('└');

    final char ch;

    Elements(char ch) {
        this.ch = ch;
    }

    public boolean isGood() {
        switch (this) {

            case NONE:
            case APPLE:
            case STONE:
            case FLYING_PILL:
            case FURY_PILL:
            case GOLD:
                return true;
        }
        return false;
    }

    /**
     * При поиске тела противника проверяет - может ли быть такая последовательность элементов
     *
     * @param next
     * @return
     */
    public boolean canBeNext(Elements next) {
        boolean result = true;
        switch (this) {

            case ENEMY_HEAD_DOWN:
            case ENEMY_HEAD_UP:
            case ENEMY_BODY_VERTICAL:
                if (next == ENEMY_BODY_HORIZONTAL) result = false;
                break;
            case ENEMY_HEAD_LEFT:
            case ENEMY_HEAD_RIGHT:
            case ENEMY_BODY_HORIZONTAL:
                if (next == ENEMY_BODY_VERTICAL) result = false;
                break;

        }

        switch (next) {
            case ENEMY_TAIL_END_DOWN:
                if (this == ENEMY_HEAD_UP) result = true;

                if (this == ENEMY_BODY_LEFT_DOWN) result = true;
                if (this == ENEMY_BODY_RIGHT_DOWN) result = true;
                if (this == ENEMY_BODY_VERTICAL) result = true;

                break;

            case ENEMY_TAIL_END_LEFT:
                if (this == ENEMY_HEAD_RIGHT) result = true;

                if (this == ENEMY_BODY_LEFT_UP) result = true;
                if (this == ENEMY_BODY_LEFT_DOWN) result = true;
                if (this == ENEMY_BODY_HORIZONTAL) result = true;

                break;

            case ENEMY_TAIL_END_RIGHT:
                if (this == ENEMY_HEAD_LEFT) result = true;

                if (this == ENEMY_BODY_RIGHT_UP) result = true;
                if (this == ENEMY_BODY_RIGHT_DOWN) result = true;
                if (this == ENEMY_BODY_HORIZONTAL) result = true;

                break;

            case ENEMY_TAIL_END_UP:
                if (this == ENEMY_HEAD_DOWN) result = true;

                if (this == ENEMY_BODY_LEFT_UP) result = true;
                if (this == ENEMY_BODY_RIGHT_UP) result = true;
                if (this == ENEMY_BODY_VERTICAL) result = true;

                break;


        }


        return result;
    }

    public boolean isItMe() {
        switch (this) {
            case HEAD_DOWN:
            case HEAD_EVIL:
            case HEAD_LEFT:
            case HEAD_RIGHT:
            case HEAD_UP:
            case HEAD_DEAD:
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
                return true;
        }
        return false;
    }

    public boolean isEnemyTail() {
        switch (this) {
            case ENEMY_TAIL_END_DOWN:
            case ENEMY_TAIL_END_LEFT:
            case ENEMY_TAIL_END_UP:
            case ENEMY_TAIL_END_RIGHT:
            case ENEMY_TAIL_INACTIVE:
                return true;
        }
        return false;
    }

    public boolean isEnemyBody() {
        switch (this) {
            case ENEMY_BODY_HORIZONTAL:
            case ENEMY_BODY_VERTICAL:
            case ENEMY_BODY_LEFT_DOWN:
            case ENEMY_BODY_LEFT_UP:
            case ENEMY_BODY_RIGHT_DOWN:
            case ENEMY_BODY_RIGHT_UP:
                return true;
        }
        return false;
    }


    public boolean isEnemyHead() {
        switch (this) {
            case ENEMY_HEAD_DOWN:
            case ENEMY_HEAD_LEFT:
            case ENEMY_HEAD_RIGHT:
            case ENEMY_HEAD_UP:
            case ENEMY_HEAD_DEAD:
            case ENEMY_HEAD_EVIL:
            case ENEMY_HEAD_FLY:
            case ENEMY_HEAD_SLEEP:
                return true;
        }
        return false;
    }

    public boolean isEnemy() {
        switch (this) {
            case ENEMY_HEAD_DOWN:
            case ENEMY_HEAD_LEFT:
            case ENEMY_HEAD_RIGHT:
            case ENEMY_HEAD_UP:
            case ENEMY_HEAD_DEAD:
            case ENEMY_HEAD_EVIL:
            case ENEMY_HEAD_FLY:
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
                return true;
        }
        return false;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

}
