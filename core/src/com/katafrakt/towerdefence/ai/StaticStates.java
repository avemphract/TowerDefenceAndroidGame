package com.katafrakt.towerdefence.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ashley.components.ai.AiComponent;

public class StaticStates {
    public static Pool<Stun> stunPool=new Pool<Stun>() {
        @Override
        protected Stun newObject() {
            return new Stun();
        }
    };
    static Array<Class<?>> staticClasses=new Array<>();{
        staticClasses.addAll(Stun.class);
    }
    public static boolean isStaticState(State<?> state){
        if(staticClasses.contains(state.getClass(),false))
            return true;
        else return false;
    }

    public static class Stun implements State<AiComponent>, Pool.Poolable {
        float time;
        public Stun init(float time){
            this.time=time;
            return this;
        }
        @Override
        public void enter(AiComponent entity) {

        }

        @Override
        public void update(AiComponent entity) {

        }

        @Override
        public void exit(AiComponent entity) {
            StaticStates.stunPool.free(this);
        }

        @Override
        public boolean onMessage(AiComponent entity, Telegram telegram) {
            return false;
        }

        @Override
        public void reset() {
            time=0;
        }
    }
}
