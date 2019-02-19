package com.codenjoy.dojo.snakebattle.model.points;

import com.codenjoy.dojo.snakebattle.client.smart.analyze.StateOfBoard;
import com.codenjoy.dojo.snakebattle.model.Elements;

import java.awt.*;
import java.util.Map;

public enum EnemyBody implements ForceProperties {
    INSTANCE(0),
    MAX_VALUE(0), MAX_RANGE(0), MAX_DEC(0),
    MED_VALUE(0), MED_RANGE(0), MED_DEC(0),
    MIN_VALUE(0), MIN_RANGE(0), MIN_DEC(0);

    private int value;
    protected String path="Points_EnemyBody";


    @Override
    public Map<String, String> getForcePoints(Point point) {
        StateOfBoard state = StateOfBoard.getInstance();

        if (state.getMySnake().getHeadType()==Elements.HEAD_EVIL) {
            if (state.getSnakeByPart(point).getHeadType()!=Elements.ENEMY_HEAD_EVIL) return getForcePoints(2);
            else return getForcePoints(0);

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
                "ENEMY_BODY",
                "ENEMY_BODY_R",
                "ENEMY_BODY_DEC");
    }

    EnemyBody(int value) {
        this.value = value;
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
