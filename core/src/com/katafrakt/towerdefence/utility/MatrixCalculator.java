package com.katafrakt.towerdefence.utility;

import static com.badlogic.gdx.math.MathUtils.*;

public class MatrixCalculator {

    public static float[] rotate(float[] vertices, float radian) {
        return product(new float[]{cos(radian), -sin(radian), sin(radian), cos(radian)}, 2, vertices, vertices.length / 2);
    }

    public static float[] product(float[] matrix1, int height1, float[] matrix2, int width2) {
        float[] result = new float[height1 * width2];
        int common = matrix1.length / height1;
        for (int i = 0; i < height1; i++) {
            for (int j = 0; j < width2; j++) {
                for (int k = 0; k < common; k++) {
                    result[i + j * common] += matrix1[i + k * common] * matrix2[j * common + k];
                }
            }
        }
        return result;
    }
}
