import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class MatrixMultiplierApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar dimensiones y valores para la primera matriz
        System.out.println("Ingrese el ancho y la altura de la primera matriz:");
        int width1 = scanner.nextInt();
        int height1 = scanner.nextInt();

        // Leer los valores de la primera matriz
        System.out.println("Ingrese los valores de la primera matriz:");
        int[][] matrix1 = readMatrix(width1, height1);

        // Solicitar dimensiones y valores para la segunda matriz
        System.out.println("Ingrese el ancho y la altura de la segunda matriz:");
        int width2 = scanner.nextInt();
        int height2 = scanner.nextInt();

        // Leer los valores de la segunda matriz
        System.out.println("Ingrese los valores de la segunda matriz:");
        int[][] matrix2 = readMatrix(width2, height2);

        // Verificar si las matrices pueden multiplicarse
        if (width1 != height2) {
            System.out.println("Error: El número de columnas de la primera matriz debe ser igual al número de filas de la segunda matriz.");
            return;
        }

        // Imprimir las matrices ingresadas por el usuario
        System.out.println("Matriz 1:");
        printMatrix(matrix1);

        System.out.println("Matriz 2:");
        printMatrix(matrix2);

        // Inicializar matriz resultado
        int[][] result = new int[height1][width2];

        // Realizar la multiplicación de matrices en paralelo
        multiplyMatricesParallel(matrix1, matrix2, result);

        // Imprimir la matriz resultante
        System.out.println("Matriz resultante:");
        printMatrix(result);

        // Preguntar al usuario si desea guardar la matriz resultante en un archivo
        System.out.println("¿Desea guardar la matriz resultante en un archivo? (S/N)");
        String choice = scanner.next().toUpperCase();

        if (choice.equals("S")) {
            saveMatrixToFile(result);
        }

        // Preguntar al usuario si desea realizar otra multiplicación de matrices
        System.out.println("¿Desea realizar otra multiplicación de matrices? (S/N)");
        choice = scanner.next().toUpperCase();

        if (choice.equals("S")) {
            main(args);
        }
    }

    // Método para leer una matriz desde la entrada estándar
    private static int[][] readMatrix(int width, int height) {
        Scanner scanner = new Scanner(System.in);
        int[][] matrix = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print("Ingrese el valor en la posición (" + (i + 1) + "," + (j + 1) + "): ");
                matrix[i][j] = scanner.nextInt();
            }
        }

        return matrix;
    }

    // Método para imprimir una matriz
    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Método para multiplicar matrices en paralelo
    private static void multiplyMatricesParallel(int[][] matrix1, int[][] matrix2, int[][] result) {
        int numThreads = Math.min(matrix1.length, Runtime.getRuntime().availableProcessors());
        MatrixMultiplier[] threads = new MatrixMultiplier[numThreads];
        int rowsPerThread = matrix1.length / numThreads;
        int startRow = 0;

        for (int i = 0; i < numThreads; i++) {
            int endRow = (i == numThreads - 1) ? matrix1.length : startRow + rowsPerThread;
            threads[i] = new MatrixMultiplier(matrix1, matrix2, result, startRow, endRow);
            startRow = endRow;
        }

        for (MatrixMultiplier thread : threads) {
            thread.start();
        }

        try {
            for (MatrixMultiplier thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para guardar una matriz en un archivo
    private static void saveMatrixToFile(int[][] matrix) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("result_matrix.txt"))) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    writer.print(matrix[i][j] + " ");
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Matriz guardada en el archivo result_matrix.txt");
    }
}
