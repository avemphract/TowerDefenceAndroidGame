package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.pfa.Node;

import java.awt.image.ColorConvertOp;

public abstract class DebugShapes {
    protected Color color;
    public ShapeRenderer.ShapeType shapeType;

    public DebugShapes(Color color, ShapeRenderer.ShapeType shapeType) {
        this.color = color;
        this.shapeType = shapeType;
    }

    public ShapeRenderer.ShapeType getShapeType() {
        return shapeType;
    }


    public void render(ShapeRenderer shapeRenderer, TransformComponent transformComponent) {
        render(shapeRenderer, transformComponent, transformComponent.orientation);
    }

    public void render(ShapeRenderer shapeRenderer, Vector2 vector2, float r) {
        render(shapeRenderer, vector2.x, vector2.y, r);
    }


    public abstract void render(ShapeRenderer shapeRenderer, float x, float y, float r);


    public static class FilledHexagon extends DebugShapes {
        private final float[] vertices;

        public FilledHexagon(Color color, float length) {
            super(color, ShapeRenderer.ShapeType.Filled);

            vertices = new float[]{
                    0, (length),
                    (length * Node.ROOT3DIV2), (length * 0.5f),
                    (length * Node.ROOT3DIV2), (-length * 0.5f),
                    0, (-length),
                    (-length * Node.ROOT3DIV2), (-length * 0.5f),
                    (-length * Node.ROOT3DIV2), (+length * 0.5f),
            };
        }

        @Override
        public void render(ShapeRenderer shapeRenderer, float x, float y, float r) {
            shapeRenderer.setColor(color);
            for (int i = 0; i < 6; i++) {
                shapeRenderer.triangle(
                        vertices[(2 * i) % 12] + x,
                        vertices[(2 * i + 1) % 12] + y,
                        vertices[(2 * i + 2) % 12] + x,
                        vertices[(2 * i + 3) % 12] + y
                        , x, y);
            }
        }
    }

    public static class LineHexagon extends DebugShapes {
        private final float[] vertices;

        public LineHexagon(Color color, float length) {
            super(color, ShapeRenderer.ShapeType.Line);
            vertices = new float[]{
                    0, (length),
                    (length * Node.ROOT3DIV2), (+length * 0.5f),
                    (length * Node.ROOT3DIV2), (-length * 0.5f),
                    0, (-length),
                    (-length * Node.ROOT3DIV2), (-length * 0.5f),
                    (-length * Node.ROOT3DIV2), (+length * 0.5f),
            };
        }


        @Override
        public void render(ShapeRenderer shapeRenderer, float x, float y, float r) {

            shapeRenderer.setColor(color);
            for (int i = 0; i < 6; i++) {
                shapeRenderer.line(
                        vertices[2 * i] + x,
                        vertices[2 * i + 1] + y,
                        vertices[(2 * i + 2) % 12] + x,
                        vertices[(2 * i + 3) % 12] + y);
            }
            //shapeRenderer.polygon(vertices);
        }
    }

    public static class FilledRect extends DebugShapes {
        Polygon polygon;
        float width;
        float height;

        public FilledRect(Color color, float width, float height) {
            super(color, ShapeRenderer.ShapeType.Filled);
            this.width=width;
            this.height=height;

            polygon = new Polygon(new float[]{0, 0, width, 0, width, height, 0, height});
            polygon.setOrigin(width / 2, height / 2);
        }

        @Override
        public void render(ShapeRenderer shapeRenderer, float x, float y, float r) {
            shapeRenderer.setColor(color);
            polygon.setRotation(r * MathUtils.radiansToDegrees);
            polygon.setPosition(x-width/2,y-height/2);
            float[] vertices= polygon.getTransformedVertices();
            float centerX=(vertices[0] + vertices[4])/2;
            float centerY=(vertices[1] + vertices[5])/2;
            shapeRenderer.triangle(vertices[0],vertices[1],vertices[2],vertices[3],centerX,centerY);
            shapeRenderer.triangle(vertices[2],vertices[3],vertices[4],vertices[5],centerX,centerY);
            shapeRenderer.triangle(vertices[4],vertices[5],vertices[6],vertices[7],centerX,centerY);
            shapeRenderer.triangle(vertices[6],vertices[7],vertices[0],vertices[1],centerX,centerY);
            //shapeRenderer.polygon(polygon.getTransformedVertices());
        }
    }

    public static class LineRect extends DebugShapes {
        Polygon polygon;

        public LineRect(Color color, float width, float height) {
            super(color, ShapeRenderer.ShapeType.Line);
            polygon = new Polygon(new float[]{0, 0, width, 0, width, height, 0, height});
            polygon.setOrigin(width / 2, height / 2);
        }

        @Override
        public void render(ShapeRenderer shapeRenderer, float x, float y, float r) {
            shapeRenderer.setColor(color);
            polygon.setPosition(x,y);
            polygon.setRotation(r);
            shapeRenderer.polygon(polygon.getTransformedVertices());
            //shapeRenderer.rect(x - width / 2, y - height / 2, width, height);
        }
    }

    public static class FilledCircle extends DebugShapes {
        private final float radius;

        public FilledCircle(Color color, float radius) {
            super(color, ShapeRenderer.ShapeType.Filled);
            this.radius = radius;
        }

        @Override
        public void render(ShapeRenderer shapeRenderer, float x, float y, float r) {
            shapeRenderer.setColor(color);
            shapeRenderer.circle(x, y, radius);
        }
    }

    public static class LineCircle extends DebugShapes {
        private final float radius;

        public LineCircle(Color color, float radius) {
            super(color, ShapeRenderer.ShapeType.Line);
            this.radius = radius;
        }

        @Override
        public void render(ShapeRenderer shapeRenderer, float x, float y, float r) {
            shapeRenderer.setColor(color);
            shapeRenderer.circle(x, y, radius);
        }
    }

    public static class Line extends DebugShapes {
        private final float length;
        private final float angle;

        public Line(Color color, float length, float angle) {
            super(color, ShapeRenderer.ShapeType.Line);
            this.length = length;
            this.angle = angle;
        }

        @Override
        public void render(ShapeRenderer shapeRenderer, float x, float y, float r) {

            shapeRenderer.setColor(color);
            float outX = MathUtils.cos(angle+r) * length;
            float outY = MathUtils.sin(angle+r) * length;
            shapeRenderer.line(x, y, x + outX, y + outY);
        }
    }
}
