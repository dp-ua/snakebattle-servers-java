package com.codenjoy.dojo.snakebattle.model.points;


public enum None implements ForceProperties {

    INSTANCE(0),
    MAX_VALUE(0),MAX_RANGE(0),MAX_DEC(0),
    MED_VALUE(0),MED_RANGE(0),MED_DEC(0),
    MIN_VALUE(0),MIN_RANGE(0),MIN_DEC(0);

    private int value;
    protected String path="Points_None";

    None(int value) {
        this.value = value;
          }



    @Override
    public String getPath() {
        return path;
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

    @Override
    public ValueNames getNames() {
        return new ValueNames(
                "NONE",
                "NONE_R",
                "NONE_DEC");
    }

}
