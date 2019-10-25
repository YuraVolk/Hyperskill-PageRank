package pagerank;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {
    private final static DecimalFormat numFormat = new DecimalFormat("#0.000");
    private final static double dampingFactor = 0.5;
    private final static int n = 7;

    private static double getDotScalar(double[][] rank) {
        double product = 0;
        for (int i = 0; i < n; i++) {
            product = product + rank[i][0] * rank[i][0];
        }
        return Math.sqrt(product);
    }

    private static void fillInitialMatrix(double[][] init) {
        double mathRandom = Math.random();
        for(int i = 0;i<init.length;i++) {
            if (mathRandom == 0) {
                mathRandom++;
            }
            init[i][0] = mathRandom;
            mathRandom = Math.random();
        }
    }

    private static boolean substractMatrices(double[][] matrix1, double[][] matrix2) {
        double c[][] = new double[matrix1.length][1];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                c[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }

        double sum = 0;
        for (int i = 0; i < c.length; i++) {
            sum += c[i][0];
        }
        return sum <= 0.01;
    }

    private static double[][] changeMatrixByConstant(double[][] matrix, double constant, char operation) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                switch (operation) {
                    case '*':
                        matrix[row][col] *= constant;
                        break;
                    case '+':
                        matrix[row][col] += constant;
                        break;
                    case '/':
                    default:
                        matrix[row][col] /= constant;
                        break;
                }
            }
        }
        return matrix;
    }

    private static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        double[][] result = new double[matrix1.length][1];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    private static double[][] iterate(double[][] matrix, double[][] values) {
        matrix = multiplyMatrices(values, matrix);
        changeMatrixByConstant(matrix, dampingFactor, '*');
        changeMatrixByConstant(matrix, (1 - dampingFactor) / n, '+');
        return matrix;
    }

    public static void main(String[] args) {
        double[][] values = {
                {0    , 1./2., 1./3., 0 , 0     , 0,     0},
                {1./3., 0    , 0    , 0 , 1./2. , 0,     0},
                {1./3., 1./2., 0    , 1., 0     , 1./3., 0},
                {1./3., 0    , 1./3., 0 , 1./2. , 1./3., 0},
                {0    , 0    , 0    , 0 , 0     , 0,     0},
                {0    , 0    , 1./3., 0 , 0     , 0,     0},
                {0    , 0    , 0    , 0 , 0     , 1./3., 1}
        };

        double[][] M = new double[n][1];
        fillInitialMatrix(M);
        changeMatrixByConstant(M, getDotScalar(M), '/');

        int count = 0;
        double[][] tempM;
        for (int i = 0; i < 32; i++) {
            M = iterate(M, values);
            count++;
            tempM = M;
            M = iterate(M, values);
            count++;
            if (substractMatrices(tempM, M)) {
                break;
            }

        }
        for (int i = 0; i < count; i++) {
            M = iterate(M, values);
        }


        changeMatrixByConstant(M, 100, '*');


        for (double[] x : M) {
            for (double y : x) {
                System.out.println(numFormat.format(y));
            }
        }


    }
}
