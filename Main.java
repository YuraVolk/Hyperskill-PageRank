package pagerank;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.DoubleStream;


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

    private static double[][] fillInitialMatrix(double[][] init) {
        double mathRandom = Math.random();
        for(int i = 0;i<init.length;i++) {
            if (mathRandom == 0) {
                mathRandom++;
            }
            init[i][0] = mathRandom;
            mathRandom = Math.random();
        }
        return init;
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
        matrix = changeMatrixByConstant(matrix, dampingFactor, '*');
        matrix = changeMatrixByConstant(matrix, (1 - dampingFactor) / n, '+');
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
        M = fillInitialMatrix(M);
        M = changeMatrixByConstant(M, getDotScalar(M), '/');
        for (int i = 0; i < 63; i++) {
            M = iterate(M, values);
        }

        M = changeMatrixByConstant(M, 100, '*');
        double[] intitial = {0.033, 0.012, 0.078, 0.049, 0.000, 0.027, 99.801};


        for (double[] x : values) {
            for (double y : x) {
                System.out.print(numFormat.format(y) + " ");
            }
            System.out.println();
        }
        System.out.println();
        for (double x : intitial) {
            System.out.println(numFormat.format(x));
        }
        System.out.println();
        for (double[] x : M) {
            for (double y : x) {
                System.out.println(numFormat.format(y));
            }
        }


    }
}
