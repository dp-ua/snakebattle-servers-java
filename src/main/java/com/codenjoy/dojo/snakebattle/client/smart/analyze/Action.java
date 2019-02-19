package com.codenjoy.dojo.snakebattle.client.smart.analyze;

import com.codenjoy.dojo.services.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;

public class Action {

    private static int MAX_MOVES = 30;

    private static Action mInstance;

    private Action() {
    }

    public static Action getInstance() {
        if (mInstance == null) {
            mInstance = new Action();
        }
        return mInstance;
    }


    public Direction getNextMove() {
        StateOfBoard state = StateOfBoard.getInstance();


        PointStat startPoint = new PointStat(
                state.getMySnake().getHead(),
                0,
                0,
                STOP,
                state.getMySnake().getDirection());
        Map<Point, PointStat> mainMap = new HashMap<>();
        mainMap.put(startPoint.coords, startPoint);

        Map<Point, PointStat> workMap = new HashMap<>();
        workMap.put(startPoint.coords, startPoint);
        int count = 0;

        while (workMap.size() > 0 && count++ < MAX_MOVES) {

            List<PointStat> tempArray = new ArrayList<>();
            for (PointStat pStat : workMap.values()) {
                for (PointStat p : getNextPoints(pStat)) {
                    if (p != null && p.amount > 0) tempArray.add(p);
                }
            }

            if (tempArray.size()==0 && mainMap.size()==1) {
                PointStat[] array = getNextPoints(startPoint);
                Arrays.sort(array);
                return array[array.length-1].firstMove;
            }

            workMap.clear();
            for (PointStat pStat : tempArray) {
                if (pStat.getPoints() >= 0) {
                    if (!workMap.containsKey(pStat.coords)) workMap.put(pStat.coords, pStat);
                    else if (workMap.get(pStat.coords).compareTo(pStat) <= 0) workMap.put(pStat.coords, pStat);
                }
            }

            for (PointStat pStat : workMap.values()) {
                if (!mainMap.containsKey(pStat.coords)) mainMap.put(pStat.coords, pStat);
                else if (mainMap.get(pStat.coords).firstMove != STOP &&
                        mainMap.get(pStat.coords).compareTo(pStat) < 0) mainMap.put(pStat.coords, pStat);
            }
        }

        if (mainMap.size() == 1) return ACT;


        List<PointStat> list = new ArrayList(mainMap.values());
        Collections.sort(list, Collections.reverseOrder());
        if (list.get(0).countMove <= 2 && list.size() > 20 && list.get(0).amount<3) {

            Priority priority = new Priority();
            for (PointStat p : mainMap.values()) {
                priority.addToDirection(p.firstMove, 1);
            }
            return priority.getDirectionByMax();
        }
        return list.get(0).firstMove;


//        return Collections.max(mainMap.values()).firstMove;
    }

    private PointStat[] getNextPoints(PointStat pStat) {
        int[][] f = Field.getInstance().getOutField();
        PointStat[] result = new PointStat[3];
        Direction direction;
        if (pStat.beforeMove==null) {
            result = new PointStat[4];
            direction=RIGHT;
        } else direction=pStat.beforeMove.inverted();


        for (int i = 0; i < result.length; i++) {
            direction = direction.clockwise();
            Point p = new Point(direction.changeX(pStat.coords.x), direction.changeY(pStat.coords.y));
            if (p.x < 0 || p.x > StateOfBoard.getInstance().getRangeX() - 1 ||
                    p.y < 0 || p.y > StateOfBoard.getInstance().getRangeY() - 1) continue;

            int add = f[p.x][p.y]; //== 0 ? 1 : f[p.x][p.y];
            PointStat pointStat = new PointStat(
                    p,
                    pStat.amount + add,
                    pStat.countMove + 1,
                    pStat.firstMove == STOP ? direction : pStat.firstMove,
                    direction
            );
            result[i] = pointStat;
        }
        return result;
    }


    class PointStat implements Comparable {
        protected final Point coords;
        protected final int amount;
        protected final int countMove;
        protected final Direction firstMove;
        protected final Direction beforeMove;


        @Override
        public String toString() {
            return "PointStat{" +
                    "points=" + getPoints() +
                    ", coords=" + coords +
                    ", amount=" + amount +
                    ", countMove=" + countMove +
                    ", firstMove=" + firstMove +
                    ", beforeMove=" + beforeMove +

                    '}';
        }

        public PointStat(Point coords, int amount, int countMove, Direction firstMove, Direction beforeMove) {
            this.coords = coords;
            this.amount = amount;
            this.countMove = countMove;
            this.firstMove = firstMove;
            this.beforeMove = beforeMove;
        }

        public int getPoints() {
            int c = countMove == 0 ? 1 : countMove;
            return (int)(((double) amount /c )*100);

        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PointStat pointStat = (PointStat) o;
            return countMove == pointStat.countMove &&
                    coords.equals(pointStat.coords) &&
                    firstMove == pointStat.firstMove &&
                    beforeMove == pointStat.beforeMove;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coords, countMove, firstMove, beforeMove);
        }

        @Override
        public int compareTo(Object o) {
            int result = this.getPoints() - ((PointStat) o).getPoints();
            result = result != 0 ? result : ((PointStat) o).amount - this.amount;
            return result;
        }
    }
}
