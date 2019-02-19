package com.codenjoy.dojo.snakebattle.client;

import com.codenjoy.dojo.snakebattle.model.points.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Settings {

    //настройки по-умолчанию
    public int ENEMY_MAX_COUNT = 4;
    public int ATTACK_DISTANCE_SCAN = 10;
    public int BOTS_COUNT_MOVE = 3;
    public boolean WRITE_BOARD = false;

    public int CONNECTION = 100;
    public int TIMEOUT = 10000;

    public String URL;

    public List<Point> BLOCKS;

    private String mainFile = "BotSettings";
    private String blocksFile = "Blocks";
    private boolean LOAD_BLOCKS = false;

    public List<Point> loadBlocks() {
        List<Point> list = new ArrayList<>();
        if (!LOAD_BLOCKS) return list;
        ResourceBundle r;
        try {
            r = ResourceBundle.getBundle(blocksFile);

            int count = Integer.parseInt(r.getString("POINTS"));

            for (int i = 0; i < count; i++) {
                String[] s = r.getString("POINT" + i).split(",");
                if (s.length != 2) continue;
                int x = Integer.parseInt(s[0]);
                int y = Integer.parseInt(s[1]);
                Point point = new Point(x, y);
                list.add(point);

            }

        } catch (MissingResourceException e) {
            System.out.println("[Settings] " + e.getMessage());
            System.out.println("[Settings] Не смог загрузить блокировки");
            return list;
        }

        return list;
    }

    public void update() {
        ResourceBundle r;
        try {
//            загружаем карту блокировок
            BLOCKS = loadBlocks();

            //загружаем карту поинтов для каждого класса точек
            for (ForceProperties p : ForceProperties.getImplements()) {
                r = ResourceBundle.getBundle(p.getPath());
                for (String s : p.params()) {
                    p.setByName(s, Integer.parseInt(r.getString(s)));
                }
            }

            r = ResourceBundle.getBundle(mainFile);

            URL = r.getString("URL");

            ENEMY_MAX_COUNT=Integer.parseInt((r.getString("ENEMY_MAX_COUNT")));
            BOTS_COUNT_MOVE = Integer.parseInt((r.getString("BOTS_COUNT_MOVE")));

            LOAD_BLOCKS = Boolean.getBoolean(r.getString("LOAD_BLOCKS"));

            ATTACK_DISTANCE_SCAN = Integer.parseInt(r.getString("ATTACK_DISTANCE_SCAN"));

            CONNECTION = Integer.parseInt(r.getString("CONNECTION"));
            TIMEOUT = Integer.parseInt(r.getString("TIMEOUT"));

        } catch (MissingResourceException e) {
            System.out.println("[Settings] " + e.getMessage());
            System.out.println("[Settings] Не загрузились. Работают стандартные");
            return;
        }
        System.out.println("[Settings] Обновил настройки");

    }

    private static Settings mInstance;

    private Settings() {
        update();
    }

    public static Settings getInstance() {
        if (mInstance == null) {
            mInstance = new Settings();
        }
        return mInstance;
    }

}
