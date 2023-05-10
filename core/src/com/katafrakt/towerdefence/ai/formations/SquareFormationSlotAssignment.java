package com.katafrakt.towerdefence.ai.formations;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.SlotAssignment;
import com.badlogic.gdx.ai.fma.SlotAssignmentStrategy;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;

public class SquareFormationSlotAssignment implements SlotAssignmentStrategy<Vector2> {
    private Entity leader;

    public SquareFormationSlotAssignment(Entity leader) {
        this.leader=leader;
    }

    @Override
    public void updateSlotAssignments(Array<SlotAssignment<Vector2>> assignments) {
        float possiblOri = PlayerAiComponent.MAPPER.get(leader).walkTarget.getPosition().angleRad(TransformComponent.MAPPER.get(leader));
        float currentOri = TransformComponent.MAPPER.get(leader).orientation;

        for (int i = 0; i < assignments.size; i++)
            assignments.get(i).slotNumber = i;
    }

    @Override
    public int calculateNumberOfSlots(Array<SlotAssignment<Vector2>> slotAssignments) {
        return slotAssignments.size;
    }

    @Override
    public void removeSlotAssignment(Array<SlotAssignment<Vector2>> slotAssignments, int index) {
        slotAssignments.removeIndex(index);
    }
}
