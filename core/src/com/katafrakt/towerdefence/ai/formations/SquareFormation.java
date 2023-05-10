package com.katafrakt.towerdefence.ai.formations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.screens.GameManager;

public class SquareFormation implements FormationPattern<Vector2> {
    private static final String TAG = SquareFormation.class.getSimpleName();
    private float colMax;
    private float rowMax;
    private float numberOfSlot;
    private float memberRadius;

    public SquareFormation(float memberRadius) {
        this.memberRadius = memberRadius;
    }

    @Override
    public void setNumberOfSlots(int numberOfSlots) {
        this.rowMax = MathUtils.floor((float) Math.pow(numberOfSlots, 0.5f));
        this.colMax = MathUtils.ceil(numberOfSlots / rowMax);
        this.numberOfSlot = numberOfSlots;
        //Gdx.app.log(TAG, "MAX col: " + colMax + " row: " + rowMax);
    }

    @Override
    public Location<Vector2> calculateSlotLocation(Location<Vector2> outLocation, int slotNumber) {
        float col = slotNumber % colMax;
        float row = MathUtils.floor(slotNumber / colMax);
        //Gdx.app.log(TAG, slotNumber + ": col: " + col + " row: " + row);
        if (rowMax == row + 1)
            outLocation.getPosition().set((rowMax - row - 1) * memberRadius, (col - (numberOfSlot - row * colMax - 1) * 0.5f) * memberRadius).add(memberRadius, 0);
        else
            outLocation.getPosition().set((rowMax - row - 1) * memberRadius, (col - (colMax - 1) * 0.5f) * memberRadius).add(memberRadius, 0);
        return outLocation;
    }

    @Override
    public boolean supportsSlots(int slotCount) {
        return true;
    }
}
