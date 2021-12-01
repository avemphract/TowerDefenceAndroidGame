package com.katafrakt.towerdefence.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.katafrakt.towerdefence.pfa.Node;

public class MapLoader {
    private static final String TAG = MapLoader.class.getSimpleName();
    private static final String MAP_SIZE_X = "mapSizeX";
    private static final String MAP_SIZE_Y = "mapSizeY";

    private MapLoader() {
    }

    static JsonReader jsonReader = new JsonReader();

    public static Map mapLoader(int level) {
        JsonValue base = jsonReader.parse(Gdx.files.internal("maps/map" + level + ".json"));
        int mapSizeX = base.get("nodes").get(0).size;
        int mapSizeY = base.get("nodes").size;
        int[][] intArray = new int[mapSizeX][mapSizeY];
        for (int y = 0; y < mapSizeY; y++) {
            int[] value = base.get("nodes").get(y).asIntArray();
            for (int x = 0; x < mapSizeX; x++) {
                intArray[x][y] = value[x];
            }
        }
        //int[] startCoordinate = base.get("startNode").asIntArray();
        //int[] endCoordinate = base.get("endNode").asIntArray();

        Gdx.app.log(TAG, base.get("nodes").size + "," + base.get("nodes").get(0).size);
        Array<Node> nodeArray = new Array<>();
        Node startNode = null, endNode = null;
        for (int j = 0; j < mapSizeY; j++) {
            for (int i = 0; i < mapSizeX; i++) {
                int tableX = i - j / 2;
                int tableY = j;
                Node node = new Node(tableX, tableY, intArray[i][j] % 10);
                nodeArray.add(node);
                System.out.print(intArray[i][j]+",");;
                if (intArray[i][j] / 10 == 1)
                    startNode = node;
                if (intArray[i][j] / 10 == 2)
                    endNode = node;
            }
        }
        Gdx.app.log(TAG, "Start: "+startNode.toString());
        Gdx.app.log(TAG, "End: "+endNode.toString());

        for (int i = 0; i < mapSizeX + 1; i++) {
            nodeArray.add(new Node(i, -1, 4));
            nodeArray.add(new Node(i - mapSizeY / 2, mapSizeY, 4));
        }
        int x = mapSizeX + 1;
        int down = 0;
        for (int i = 0; i < mapSizeY; i++) {
            if (i % 2 == 0)
                down--;
            nodeArray.add(new Node(x + down, i, 4));
            nodeArray.add(new Node(+down, i, 4));
        }

        return new Map(nodeArray, startNode, endNode);
    }
}
