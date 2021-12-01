package com.katafrakt.towerdefence.pfa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.map.Map;

public class GraphAlly extends AbstractGridGraph {
    private static final String TAG=GraphAlly.class.getSimpleName();

    public GraphAlly(Map map) {
        super(map);
        raycastCollisionDetector=new WalkableRaycastCollisionDetector();
        pathSmoother=new PathSmoother<>(raycastCollisionDetector);
    }

    @Override
    protected void createConnections() {

        for (Node node:map.nodes){
            if (!isConnected(node))
                continue;

            if (map.nodeMap.get(node.tableY+1).get(node.tableX)!=null && isConnected(map.nodeMap.get(node.tableY+1).get(node.tableX)) ){
                node.allyConnection.add(new GridConnection(node,map.nodeMap.get(node.tableY+1).get(node.tableX)));
            }
            if (map.nodeMap.get(node.tableY).get(node.tableX+1)!=null && isConnected(map.nodeMap.get(node.tableY).get(node.tableX+1)) ){
                node.allyConnection.add(new GridConnection(node,map.nodeMap.get(node.tableY).get(node.tableX+1)));
            }
            if (map.nodeMap.get(node.tableY-1).get(node.tableX+1)!=null && isConnected(map.nodeMap.get(node.tableY-1).get(node.tableX+1)) ){
                node.allyConnection.add(new GridConnection(node,map.nodeMap.get(node.tableY-1).get(node.tableX+1)));
            }
            if (map.nodeMap.get(node.tableY-1).get(node.tableX)!=null && isConnected(map.nodeMap.get(node.tableY-1).get(node.tableX)) ){
                node.allyConnection.add(new GridConnection(node,map.nodeMap.get(node.tableY-1).get(node.tableX)));
            }
            if (map.nodeMap.get(node.tableY).get(node.tableX-1)!=null && isConnected(map.nodeMap.get(node.tableY).get(node.tableX-1)) ){
                node.allyConnection.add(new GridConnection(node,map.nodeMap.get(node.tableY).get(node.tableX-1)));
            }
            if (map.nodeMap.get(node.tableY+1).get(node.tableX-1)!=null && isConnected(map.nodeMap.get(node.tableY+1).get(node.tableX-1)) ){
                node.allyConnection.add(new GridConnection(node,map.nodeMap.get(node.tableY+1).get(node.tableX-1)));
            }
        }
    }

    private static boolean isConnected(Node node){
        return node.type == Node.Type.ENEMY_PATH || node.type == Node.Type.PLAIN_TILE;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.allyConnection;
    }

    protected class WalkableRaycastCollisionDetector implements RaycastCollisionDetector<Vector2> {

        @Override
        public boolean collides(Ray<Vector2> ray) {
            Vector2 rot=ray.end.cpy().mulAdd(ray.start,-1);
            rot.scl(1/rot.len());
            for (float i=0;i<ray.start.dst(ray.end);i+=0.2f){
                //Gdx.app.log(TAG,(ray.start.x+rot.x*i)+","+(ray.start.y+rot.y*i));
                Node node= map.findNode(ray.start.x+rot.x*i,ray.start.y+rot.y*i);
                if (node==null || node.type==Node.Type.SEA_TILE)
                    return true;
            }
            return false;
        }

        @Override
        public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> ray) {
            Vector2 rot=ray.end.cpy().mulAdd(ray.start,-1);
            rot.scl(1/rot.len());
            for (float i=0;i<ray.start.dst(ray.end);i+=0.1f){
                Gdx.app.debug(TAG,(ray.start.x+rot.x)+","+(ray.start.y+rot.y));
                Node node= map.findNode(ray.start.x+rot.x,ray.start.y+rot.y);
                if (node==null || node.type==Node.Type.SEA_TILE){
                    outputCollision.point=new Vector2(ray.start.x+rot.x,ray.start.y+rot.y);
                    return true;
                }
            }
            return false;
        }
    }
}
