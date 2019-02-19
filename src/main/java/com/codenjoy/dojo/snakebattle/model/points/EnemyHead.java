package com.codenjoy.dojo.snakebattle.model.points;

import com.codenjoy.dojo.snakebattle.client.smart.analyze.Field;
import com.codenjoy.dojo.snakebattle.client.smart.analyze.StateOfBoard;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.Snake;

import java.awt.*;
import java.util.Map;

public enum EnemyHead implements ForceProperties {
    INSTANCE(0),
    MAX_VALUE(0), MAX_RANGE(0), MAX_DEC(0),
    MED_VALUE(0), MED_RANGE(0), MED_DEC(0),
    MIN_VALUE(0), MIN_RANGE(0), MIN_DEC(0);

    private int value;

    EnemyHead(int value) {
        this.value = value;
    }

    protected String path = "Points_EnemyHead";


    @Override
    public Map<String, String> getForcePoints(Point point) {
        StateOfBoard state = StateOfBoard.getInstance();
        Snake enemy = state.getSnakeByPart(point);

        if (state.isMeStronger(enemy)) {
            Field.getInstance().fillDirection(enemy.getHead(),enemy.getDirection(),3,1);
            return getForcePoints(2);
        }
        else Field.getInstance().fillDirection(enemy.getHead(),enemy.getDirection(),1,-2);

        if (enemy.getHeadType() == Elements.ENEMY_HEAD_EVIL) {
            return getForcePoints(0);
        }

        return getForcePoints(1);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public ValueNames getNames() {
        return new ValueNames(
                "ENEMY_HEAD",
                "ENEMY_HEAD_R",
                "ENEMY_HEAD_DEC");
    }

    @Override
    public void set(int value) {
        this.value = value;
    }

    @Override
    public void setByName(String name, int value) {
        valueOf(name).set(value);
    }

    @Override
    public int getByName(String name) {
        return valueOf(name).value;
    }
}
