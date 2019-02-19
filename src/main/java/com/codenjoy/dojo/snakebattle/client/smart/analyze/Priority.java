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

import com.codenjoy.dojo.services.Direction;

import java.util.HashMap;
import java.util.Map;

public class Priority {
    @Override
    public String toString() {
        return "Priority{" +
                "up=" + up +
                ", right=" + right +
                ", down=" + down +
                ", left=" + left +
                '}';
    }

    public int up;
    public int right;
    public int down;
    public int left;

    public Priority() {
        this.up = 0;
        this.right = 0;
        this.down = 0;
        this.left = 0;
    }

    public boolean isEmpty() {
        if (up!=0 || right!=0 || left!=0 ||right!=0) return false;
        return true;
    }

    public void addToDirection(Direction direction, int points) {
        addToDirection(points, direction);
    }

    public void add(Priority priority) {
        this.up += priority.up;
        this.right += priority.right;
        this.down += priority.down;
        this.left += priority.left;

    }

    /**
     * divides all coefficients by the specified number
     *
     * @param d double
     */
    public void div(double d) {
        if (d != 0) {
            up /= d;
            right /= d;
            down /= d;
            left /= d;
        }
    }

    public void addToDirection(int points, Direction... directions) {
        for (Direction d : directions
        ) {
            switch (d) {
                case LEFT:
                    left += points;
                    break;
                case RIGHT:
                    right += points;
                    break;
                case UP:
                    up += points;
                    break;
                case DOWN:
                    down += points;
                    break;
            }
        }
    }


    public void setX(int plus) {
        left += plus;
        right += plus;
    }

    public void setY(int plus) {
        up += plus;
        down += plus;
    }

    public Direction getDirectionByMax() {
        Map<Integer, Direction> map = new HashMap<>();
        map.put(up, Direction.UP);
        map.put(down, Direction.DOWN);
        map.put(left, Direction.LEFT);
        map.put(right, Direction.RIGHT);
        int max = Math.max(Math.max(up, down), Math.max(left, right));
        return map.get(max);
    }
}
