package pagerank;

import Jama.Matrix;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.DoubleStream;


public class Main {
    private final static DecimalFormat numFormat = new DecimalFormat("#0.000");
    public static void main(String[] args) {
        double[][] values = {
                {0    , 1./2., 1./3., 0 , 0     , 0},
                {1./3., 0    , 0    , 0 , 1./2. , 0},
                {1./3., 1./2., 0    , 1., 0     , 1./2.},
                {1./3., 0    , 1./3., 0 , 1./2. , 1./2.},
                {0    , 0    , 0    , 0 , 0     , 0},
                {0    , 0    , 1./3., 0 , 0     , 0}
        };
        double[] r = DoubleStream.generate(() -> 1.).limit(6).toArray();
        r = DoubleStream.of(r).map(p -> 100 * p / 6).toArray();


        Matrix m = new Matrix(values);

        Matrix r0;
        Matrix r1;

        r0 = new Matrix(r, 1).transpose();
        r0 = m.times(r0);

        double[] firstRes = r0.getColumnPackedCopy();

        for (int i = 1; i < 50; i++) {
            r1 = new Matrix(r0.getColumnPackedCopy(), 1).transpose();
            r1 = m.times(r1);

            r0 = new Matrix(r1.getColumnPackedCopy(), 1).transpose();
            r0 = m.times(r0);
        }

        double[] secondRes = r0.getColumnPackedCopy();

        r0 = new Matrix(r, 1).transpose();
        r0 = m.times(r0);
        for (int i = 0; i < 12; i++) {
            r1 = new Matrix(r0.getColumnPackedCopy(), 1).transpose();
            r1 = m.times(r1);
            if(r0.minus(r1).normInf() <= 0.01) {
                break;
            }
            r0 = new Matrix(r1.getColumnPackedCopy(), 1).transpose();
            r0 = m.times(r0);
        }

        double[] thirdRes = r0.getColumnPackedCopy();

        for (double x : firstRes) {
            System.out.println(numFormat.format(x));
        }
        System.out.println();
        for (double x : secondRes) {
            System.out.println(numFormat.format(x));
        }
        System.out.println();
        for (int i = 0; i < thirdRes.length; i++) {
            if (i == thirdRes.length - 1) {
                System.out.print(numFormat.format(thirdRes[i]));
            } else {
                System.out.println(numFormat.format(thirdRes[i]));
            }
        }
    }
}
