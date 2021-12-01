package com.katafrakt.towerdefence.pfa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.katafrakt.towerdefence.map.Map;

public abstract class AbstractGridGraph implements IndexedGraph<Node> {
    private static final String TAG = AbstractGridGraph.class.getSimpleName();
    protected Map map;

    protected IndexedAStarPathFinder<Node> pathFinder;
    protected GridHeuristic gridHeuristic = new GridHeuristic();
    protected PathSmoother<Node, Vector2> pathSmoother;
    protected RaycastCollisionDetector<Vector2> raycastCollisionDetector;


    private final BitmapFont bitmapFont = new BitmapFont();

    public AbstractGridGraph(Map map) {
        this.map = map;
        createConnections();
        bitmapFont.setUseIntegerPositions(false);
        bitmapFont.getData().setScale(0.2f);

        pathFinder = new IndexedAStarPathFinder<>(this);
    }

    protected abstract void createConnections();

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Node node : map.nodes) {
            for (int i = 0; i < 6; i++) {
                shapeRenderer.setColor(node.type.color);
                shapeRenderer.triangle(node.vertices[(2 * i) % 12], node.vertices[(2 * i + 1) % 12], node.vertices[(2 * i + 2) % 12], node.vertices[(2 * i + 3) % 12], node.x, node.y);
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for (Node node : map.nodes) {
            shapeRenderer.polygon(node.vertices);
        }
        shapeRenderer.end();


        shapeRenderer.setColor(Color.BLACK);
        spriteBatch.begin();
        for (Node node : map.nodes) {
            bitmapFont.draw(spriteBatch, node.tableX + "," + node.tableY + "\n"/*+node.enemyEntities.size*/, node.x - 2, node.y + 2);
            //bitmapFont.draw(spriteBatch,"E: "+node.enemyEntities.size,node.x - 2, node.y + 2);
        }
        for (ObjectMap.Entry<Node, Integer> entry : map.endRemain) {
            Node node = entry.key;
            int i = entry.value;
            //bitmapFont.draw(spriteBatch, "\n" + i, node.x - 2, node.y + 2);
        }
        spriteBatch.end();
    }

    @Override
    public int getIndex(Node node) {
        return map.nodes.indexOf(node, true);
    }

    @Override
    public int getNodeCount() {
        return map.nodes.size;
    }

    public SmoothableGraphPath<Node, Vector2> getSmoothPath(Node startPoint, Node endPoint) {
        SmoothableGraphPath<Node, Vector2> path = new DefaultSmoothableGraphPath();
        pathFinder.searchNodePath(startPoint, endPoint, gridHeuristic, path);
        pathSmoother.smoothPath(path);
        return path;
    }

    public DefaultGraphPath<Node> getPath(Node startNode, Node endNode) {
        DefaultGraphPath<Node> path = new DefaultGraphPath<>();
        pathFinder.searchNodePath(startNode, endNode, gridHeuristic, path);
        return path;
    }

    private static class GridHeuristic implements Heuristic<Node> {

        @Override
        public float estimate(Node node, Node endNode) {
            return node.dst(endNode);
        }
    }

    protected static class DefaultSmoothableGraphPath extends DefaultGraphPath<Node> implements SmoothableGraphPath<Node, Vector2> {

        @Override
        public Vector2 getNodePosition(int index) {
            return get(index).posVector;
        }

        @Override
        public void swapNodes(int index1, int index2) {
            nodes.swap(index1, index2);
        }

        @Override
        public void truncatePath(int newLength) {
            nodes.truncate(newLength);
        }
    }
}
