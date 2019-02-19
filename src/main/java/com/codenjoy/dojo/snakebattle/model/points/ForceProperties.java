package com.codenjoy.dojo.snakebattle.model.points;

import com.codenjoy.dojo.snakebattle.client.smart.analyze.StateOfBoard;
import com.codenjoy.dojo.snakebattle.model.Snake;

import java.awt.*;
import java.util.*;
import java.util.List;

public interface ForceProperties {

    static Set<ForceProperties> getImplements() {
        Set<ForceProperties> set = new HashSet<>();
        set.add(Apple.INSTANCE);
        set.add(EnemyBody.INSTANCE);
        set.add(EnemyHead.INSTANCE);
        set.add(EnemyTail.INSTANCE);
        set.add(Fly.INSTANCE);
        set.add(Fury.INSTANCE);
        set.add(Gold.INSTANCE);
        set.add(Me.INSTANCE);
        set.add(None.INSTANCE);
        set.add(Stone.INSTANCE);
        set.add(Wall.INSTANCE);

        return set;
    }

    default Map<String, String> getForcePoints(Point point) {
        StateOfBoard state = StateOfBoard.getInstance();

        Snake closestEnemy = state.getClosestEnemyToPoint(point);
        double distEn = closestEnemy.getHead().distance(point);
        double distMe = state.getMySnake().getHead().distance(point);
        if (distEn <= distMe && distEn < 6)
            return getForcePoints(0);

        return getForcePoints(1);
    }

    /**
     * @param choice get Params:
     *               0 - for getMin
     *               1- getMed
     *               2 - getMax
     * @return
     */
    default Map<String, String> getForcePoints(int choice) {
        Map<String, String> map = new HashMap<>();
        String[] keys = getNames().fields();
        String[] values;
        switch (choice) {
            case 0:
                values = getMin().fields();
                break;
            case 1:
                values = getMed().fields();
                break;
            case 2:
                values = getMax().fields();
                break;
            default:
                values = getMed().fields();
                break;
        }
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    String getPath();

    void set(int value);

    void setByName(String name, int value);

    int getByName(String name);

    ValueNames getNames();

    default ValueNames getMax() {
        return new ValueNames(params().get(0), params().get(1), params().get(2));
    }

    default ValueNames getMed() {
        return new ValueNames(params().get(3), params().get(4), params().get(5));
    }

    default ValueNames getMin() {
        return new ValueNames(params().get(6), params().get(7), params().get(8));
    }

    /**
     * Базовые параметры Енума, реализующего данный интерфейс. Нужны для загрузки правильных значений из файла настроек
     *
     * @return
     */
    default List<String> params() {
        List<String> list = new ArrayList<>();

        list.addAll(Arrays.asList(new String[]{
                "MAX_VALUE", "MAX_RANGE", "MAX_DEC",
                "MED_VALUE", "MED_RANGE", "MED_DEC",
                "MIN_VALUE", "MIN_RANGE", "MIN_DEC"}));
        return list;
    }

    class ValueNames {
        public final String atFirst;
        public final String atSecond;
        public final String atThird;

        public ValueNames(String atFirst, String atSecond, String atThird) {
            this.atFirst = atFirst;
            this.atSecond = atSecond;
            this.atThird = atThird;
        }

        public String[] fields() {
            return new String[]{atFirst, atSecond, atThird};
        }
    }
}
