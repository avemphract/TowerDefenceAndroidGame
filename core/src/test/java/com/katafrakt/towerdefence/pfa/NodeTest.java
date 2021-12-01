package com.katafrakt.towerdefence.pfa;


import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void isInside() {
        Node node = new Node(0, 0, 0);
        assertTrue(node.isInside(0, 0));
        for (float i = (int) (-Node.HEIGHT / 2); i < Node.HEIGHT / 2; i++) {
            float finalI = i;
            assertTrue(node.isInside(0, i), new NodeMessage(node, 0, i, false));
        }
        for (int i = 0; i < node.vertices.length; i += 2) {
            assertTrue(node.isInside(node.vertices[i], node.vertices[i + 1]), new NodeMessage(node, node.vertices[i], node.vertices[i + 1], false));
        }

        assertFalse(node.isInside(0,10),new NodeMessage(node,0,10,true));
        assertFalse(node.isInside(Node.WIDTH / 2,Node.HEIGHT / 2),new NodeMessage(node,Node.WIDTH / 2,Node.HEIGHT / 2,true));
        for (int y = (int) (-Node.HEIGHT / 2 - 1); y < Node.HEIGHT / 2 + 2; y++) {
            for (int x = (int) (-Node.WIDTH / 2 - 1); x < Node.WIDTH / 2 + 1; x++) {
                if (node.isInside(x, y))
                    //System.out.print("x:"+x+" y:"+y+" T :");
                    System.out.print("1   ");
                else
                    //System.out.print("x:"+x+" y:"+y+" F :");
                    System.out.print("0   ");

            }
            System.out.print("\n");
        }

    }

    @Test
    void isInsideDeep(){
        Node node=new Node(0,0,0);
        assertFalse(node.isInside(6.9282f,8));
        assertFalse(node.isInside(6.9282f,7));
        assertFalse(node.isInside(6.9282f,6));
        assertFalse(node.isInside(6.9282f,5));
        assertFalse(node.isInside(6.9282f,0));
    }

    @Test
    void nodeDistance() {
        Node node1 = new Node(0,0,1);
        Node node2 = new Node(-1,1,1);

        System.out.println(node1.nodeDistance(node2));
    }

    private class NodeMessage implements Supplier<String> {
        Node node;
        float x, y;
        boolean isInside;

        public NodeMessage(Node node, float x, float y, boolean isInside) {
            this.node = node;
            this.x = x;
            this.y = y;
            this.isInside = isInside;
        }

        @Override
        public String get() {
            if (isInside)
                return node.toString() + " is inside (" + x + "," + y + ")";
            else
                return node.toString() + " is not inside (" + x + "," + y + ")";

        }
    }

}