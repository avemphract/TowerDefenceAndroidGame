package com.katafrakt.towerdefence.ai.formations;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class VFormation implements FormationPattern<Vector2> {

    private float memberRadius;

    @Override
    public void setNumberOfSlots(int numberOfSlots) {

    }

    @Override
    public Location<Vector2> calculateSlotLocation(Location<Vector2> outLocation, int slotNumber) {
        return null;
    }

    @Override
    public boolean supportsSlots(int slotCount) {
        return false;
    }
}
