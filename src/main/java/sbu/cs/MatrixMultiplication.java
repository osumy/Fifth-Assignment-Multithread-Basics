package sbu.cs;

import java.util.ArrayList;
import java.util.List;

public class MatrixMultiplication {

    public static class BlockMultiplier implements Runnable
    {
        List<List<Integer>> tempMatrixProduct;
        List<List<Integer>> A;
        List<List<Integer>> B;
        int p, q, r;

        public BlockMultiplier(List<List<Integer>> A, List<List<Integer>> B) {
            this.A = A;
            this.B = B;
            tempMatrixProduct = new ArrayList<>();
        }

        @Override
        public void run() {
            p = A.size();
            q = A.getFirst().size();
            r = B.getFirst().size();

            for (int i = 0; i < p; i++) {
                List<Integer> row = new ArrayList<>();
                for (int j = 0; j < r; j++) {
                    int sum = 0;
                    for (int k = 0; k < q; k++) {
                        sum += A.get(i).get(k)*B.get(k).get(j);
                    }
                    row.add(sum);
                }
                tempMatrixProduct.add(row);
            }
        }
    }

    /*
    Matrix A is of the form p x q
    Matrix B is of the form q x r
    both p and r are even numbers
    */
    public static List<List<Integer>> ParallelizeMatMul(List<List<Integer>> matrix_A, List<List<Integer>> matrix_B) throws InterruptedException {
        List<List<Integer>> matrix_C = new ArrayList<>();

        int p = matrix_A.size();
        int q = matrix_A.getFirst().size();
        int r = matrix_B.getFirst().size();

        List<List<Integer>> mat_A1 = new ArrayList<>();
        List<List<Integer>> mat_A2 = new ArrayList<>();
        List<List<Integer>> mat_B1 = new ArrayList<>();
        List<List<Integer>> mat_B2 = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                row.add(0);
            }
            matrix_C.add(i, row);
        }

        for (int i = 0; i < p/2; i++){
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < q; j++){
                row.add(matrix_A.get(i).get(j));
            }
            mat_A1.add(row);
        }
        for (int i = p/2; i < p; i++){
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < q; j++){
                row.add(matrix_A.get(i).get(j));
            }
            mat_A2.add(row);
        }
        for (int i = 0; i < q; i++){
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < r/2; j++){
                row.add(matrix_B.get(i).get(j));
            }
            mat_B1.add(row);
        }
        for (int i = 0; i < q; i++){
            List<Integer> row = new ArrayList<>();
            for (int j = r/2; j < r; j++){
                row.add(matrix_B.get(i).get(j));
            }
            mat_B2.add(row);
        }

        BlockMultiplier block_1 = new BlockMultiplier(mat_A1, mat_B1);
        BlockMultiplier block_2 = new BlockMultiplier(mat_A1, mat_B2);
        BlockMultiplier block_3 = new BlockMultiplier(mat_A2, mat_B1);
        BlockMultiplier block_4 = new BlockMultiplier(mat_A2, mat_B2);

        Thread thread1 = new Thread(block_1);
        Thread thread2 = new Thread(block_2);
        Thread thread3 = new Thread(block_3);
        Thread thread4 = new Thread(block_4);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread4.join();

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < r; j++) {
                if (i < p/2 && j < r/2){
                    matrix_C.get(i).set(j, block_1.tempMatrixProduct.get(i).get(j));
                }
                else if (i < p/2 && j >= r/2){
                    matrix_C.get(i).set(j, block_2.tempMatrixProduct.get(i).get(j-r/2));
                }
                else if (i >= p/2 && j < r/2){
                    matrix_C.get(i).set(j, block_3.tempMatrixProduct.get(i-p/2).get(j));
                }
                else{
                    matrix_C.get(i).set(j, block_4.tempMatrixProduct.get(i-p/2).get(j-r/2));
                }
            }
        }

        return matrix_C;
    }

    public static void main(String[] args) {
    }
}
