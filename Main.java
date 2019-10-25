package pagerank;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final static DecimalFormat numFormat = new DecimalFormat("#0.000");
    private static final double dampingFactor = 0.5;
    private static int n;

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

        double f = 0;
        for (int i = 0; i < c.length; i++) {
            double s = 0;
            for (int j = 0; j < c[0].length; j++) {
                s += Math.abs(c[i][j]);
            }
            f = Math.max(f,s);
        }

        return f <= 0.01;
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

    private static double[][] calculatePagerank(double[][] values) {
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

        return M;
    }

    private static Map<String, Double> sortMap(Map<String, Double> queries) {
        return queries.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e2, e1) -> e1, LinkedHashMap::new));
    }

    private static double[][] iterate(double[][] matrix, double[][] values) {
        matrix = multiplyMatrices(values, matrix);
        changeMatrixByConstant(matrix, dampingFactor, '*');
        changeMatrixByConstant(matrix, (1 - dampingFactor) / n, '+');
        return matrix;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();

        String[] sites = new String[n];
        for(int i = 0; i < n; i++) {
            sites[i] = scanner.next();
        }

        double[][] values = new double[n][n];
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                values[i][j] = scanner.nextDouble();
            }
        }

        String query = scanner.next();

        scanner.close();

        Map<String, Double> queries = new HashMap<>();
        Map<String, Double> priorities = new HashMap<>();

        double[][] M = calculatePagerank(values);
        for (int i = 0; i < n; i++) {
            if (sites[i].contains(query)) {
                priorities.put(sites[i], M[i][0]);
                continue;
            }
            queries.put(sites[i], M[i][0]);
        }

        List<String> sortedSites = new ArrayList<>(sortMap(queries).keySet());
        List<String> sortedPriorities = new ArrayList<>(sortMap(priorities).keySet());
        Collections.reverse(sortedPriorities);
        Collections.reverse(sortedSites);

        int count = 0;
        for (int i = 0; i < 5; i++) {
            count = i;
            if (!(i >= sortedPriorities.size())) {
                System.out.println(sortedPriorities.get(i));
            } else {
                break;
            }
        }
        if (count < 5) {
            for (int i = 0; i < 5; i++) {
                count++;
                if (count > 5) {
                    break;
                }
                if (!(i >= sortedSites.size())) {
                    System.out.println(sortedSites.get(i));
                } else {
                    break;
                }
            }

        }
    }
}
