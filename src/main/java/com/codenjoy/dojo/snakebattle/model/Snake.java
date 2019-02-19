package com.codenjoy.dojo.snakebattle.model;

import com.codenjoy.dojo.services.Direction;

import java.awt.*;
import java.util.LinkedList;

public class Snake {
    private Elements headType;
    private boolean evil;
    private LinkedList<Point> body;
    private Direction direction;

    public double getDistance(Snake snake){
        return this.getHead().distance(snake.getHead());
    }

    public Snake() {
        evil = false;
        body = new LinkedList<>();
        direction = null;
        headType= Elements.ENEMY_HEAD_SLEEP;
    }

    public Elements getHeadType() {
        return headType;
    }

    public void setHeadType(Elements headType) {
        this.headType = headType;
    }

    public Snake(LinkedList body) {
        this();
        this.body = body;
    }


    public Snake(int size) {
        //временный конструктор для фиксации длины змеи
        this();
        for (int i = 0; i < size; i++) {
            body.add(null);
        }
    }

    public Point getHead() {
        if (body.size() > 0) return body.getFirst();
        else return new Point();
    }

    /**
     * Calculates how much the current snake is larger than the object with which it is compared.
     *
     * @param snake object to compare
     * @return positive values ​​- if the current snake is larger and negative - if less
     */
    public int compare(Snake snake) {
        return getSize() - snake.getSize();
    }

    /**
     * Calculate snake size
     * Look at List of body Points.
     *
     * @return int
     */
    public int getSize() {
        return body.size();
    }

    public boolean isThisSnake(Point point) {
        for (Point p : body) {
            if (point.equals(p)) return true;
        }
        return false;
    }

    public boolean isEvil() {
        return evil;
    }

    public void setEvil(boolean evil) {
        this.evil = evil;
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public void setBody(LinkedList<Point> body) {
        this.body = body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
