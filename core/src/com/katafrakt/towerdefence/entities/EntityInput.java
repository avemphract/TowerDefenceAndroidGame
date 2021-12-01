package com.katafrakt.towerdefence.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.FocusableComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;

public class EntityInput {
    private static final String TAG=EntityInput.class.getSimpleName();
    public Listener listener;

    private Entity focusedEntity;
    private final PooledEngine pooledEngine;
    private final Camera camera;

    public EntityInput(Camera camera,PooledEngine pooledEngine) {
        this.camera = camera;
        this.pooledEngine = pooledEngine;
        listener=new Listener();
    }

    private Entity getEntityFromCoordinate(float x, float y){
        for (Entity entity: pooledEngine.getEntities()){
            if (!FocusableComponent.MAPPER.has(entity))
                continue;
            BoundComponent boundComponent=BoundComponent.MAPPER.get(entity);
            TransformComponent transformComponent=TransformComponent.MAPPER.get(entity);
            if (boundComponent!=null){
                if ((transformComponent.x-boundComponent.x/2<x&&x<transformComponent.x+boundComponent.x/2) && (transformComponent.y-boundComponent.y/2<y&&y<transformComponent.y+boundComponent.y/2))
                    return entity;
            }
            else
                if ((transformComponent.x-2<x&&x<transformComponent.x+2) && (transformComponent.y-2<y&&y<transformComponent.y+2))
                    return entity;
        }
        return null;
    }
    Vector2 worldPos=new Vector2();
    Vector3 temp=new Vector3();
    private Vector2 getWorldPosition(float x, float y){
        temp=camera.unproject(temp.set(x,y,0));
        return worldPos.set(temp.x,temp.y);
    }


    public class Listener implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector2 worldPos=getWorldPosition(screenX,screenY);
            Entity entity=getEntityFromCoordinate(worldPos.x,worldPos.y);
            //Gdx.app.log(TAG,"Pos: "+worldPos.x+","+worldPos.y);
            if (focusedEntity!=null&&PlayerAiComponent.MAPPER.has(focusedEntity)){
                MessageManager.getInstance().dispatchMessage(EntityType.PLAYER.ordinal(),worldPos);
                return true;
            }
            if (entity!=focusedEntity){
                if (entity==null)
                    focusedEntity=null;
                else
                    focusedEntity=entity;
            }
            else {
                focusedEntity=null;
            }
            return true;

        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }

    }
}
