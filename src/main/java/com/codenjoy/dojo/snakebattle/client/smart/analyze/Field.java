package com.codenjoy.dojo.snakebattle.client.smart.analyze;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.snakebattle.model.points.*;
import com.codenjoy.dojo.snakebattle.model.Elements;

import java.awt.*;
import java.util.*;

public class Field {
    private static Field mInstance;
    private char[][] inField;
    private int[][] outField;

    private Field() {
    }

    public static Field getInstance() {
        if (mInstance == null) {
            mInstance = new Field();
        }
        return mInstance;
    }

    public void printField() {
        for (int y = outField.length - 1; y >= 0; y--) {
            System.out.printf("%2d ", y);
            for (int x = 0; x < outField.length; x++) {

                System.out.printf("%3d", outField[x][y]);
            }
            System.out.println();
        }
    }

    public void fillDirection(Point point, Direction direction, int moves, int value) {
        if (point.x < 0 || point.x > inField.length - 1 || point.y < 0 || point.y > inField.length - 1) return;
        if (!Direction.onlyDirections().contains(direction)) return;
        Direction wrongD = direction.inverted();
        Set<Point> set = new HashSet<>();
        set.add(point);
        for (int i = 0; i < moves; i++) {
            Set<Point> tempSet = new HashSet<>();
            for (Point p : set) {
                tempSet.add(new Point(wrongD.clockwise().changeX(p.x), wrongD.clockwise().changeY(p.y)));
                tempSet.add(new Point(direction.changeX(p.x), direction.changeY(p.y)));
                tempSet.add(new Point(direction.clockwise().changeX(p.x), direction.clockwise().changeY(p.y)));
            }
            set.addAll(tempSet);
        }
        set.remove(point);
        for (Point p : set) {
            fillPoint(p, value);
        }
    }

    private void fillPoint(Point point, int value) {
        if (point.x < 0 || point.x > inField.length - 1 || point.y < 0 || point.y > inField.length - 1) return;
        outField[point.x][point.y] += value;
    }

    private void fillRange(Point point, int value, int range, int rangeDec) {
        if (point.x < 0 || point.x > inField.length - 1 || point.y < 0 || point.y > inField.length - 1) return;
        if (range == 0) {
            fillPoint(point, value);
            return;
        }

        for (int i = point.x - range; i <= point.x + range; i++) {
            for (int j = point.y - range; j <= point.y + range; j++) {
                Point p = new Point(i, j);
                double dist = point.distance(p);
                if (dist <= range) fillPoint(p, value - (int) (rangeDec * dist));
            }
        }

    }


    public void analyze() {
        if (inField.length == 0) throw new ArrayStoreException("Cant analyze empty field");
        outField = new int[inField.length][inField[0].length];

        for (int[] i : outField)
            Arrays.fill(i, None.INSTANCE.getByName("MED_VALUE"));


        ForceProperties work;

        for (int i = 0; i < inField.length; i++) {
            for (int j = 0; j < inField[i].length; j++) {
                Point point = new Point(i, j);
                Elements e = Elements.valueOf(inField[i][j]);

                switch (e) {
                    case APPLE:
                        work = Apple.INSTANCE;
                        break;
                    case FLYING_PILL:
                        work = Fly.INSTANCE;
                        break;
                    case FURY_PILL:
                        work = Fury.INSTANCE;
                        break;
                    case GOLD:
                        work = Gold.INSTANCE;
                        break;
                    case NONE:
                        work = None.INSTANCE;
                        break;
                    case STONE:
                        work = Stone.INSTANCE;
                        break;
                    case WALL:
                    case START_FLOOR:
                    case OTHER:
                    case ENEMY_HEAD_DEAD:
                    case ENEMY_TAIL_INACTIVE:
                        work = Wall.INSTANCE;
                        break;
                    default:
                        work = None.INSTANCE;
                }


                if (e.isItMe()) work = Me.INSTANCE;

                if (e.isEnemyHead())
                    work = EnemyHead.INSTANCE;
                if (e.isEnemyBody()) work = EnemyBody.INSTANCE;
                if (e.isEnemyTail()) work = EnemyTail.INSTANCE;

                try {


                    if (work != None.INSTANCE) fillRange(point,
                            work.getByName(work.getForcePoints(point).get(work.getNames().atFirst)),
                            work.getByName(work.getForcePoints(point).get(work.getNames().atSecond)),
                            work.getByName(work.getForcePoints(point).get(work.getNames().atThird))
                    );
                } catch (Exception ex) {
                    System.out.println("Element: " + e);
                    System.out.println(work.getClass().getName() + " i:" + i + " j:" + j + " e:" + e.ch());
                    ex.printStackTrace();
                }


            }
        }

    }


    public char[][] getInField() {
        return inField;
    }

    public void setInField(char[][] inField) {
        this.inField = inField;
    }

    public int[][] getOutField() {
        return outField;
    }
}
