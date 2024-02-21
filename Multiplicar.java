import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class Multiplicar {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar dimensiones y valores para la primera matriz
        System.out.println("Ingrese el ancho y la altura de la primera matriz:");
        int ancho = scanner.nextInt();
        int altura = scanner.nextInt();

        // Leer los valores de la primera matriz
        System.out.println("Ingrese los valores de la primera matriz:");
        int[][] matriz1 = readMatrix(ancho, altura);

        // Solicitar dimensiones y valores para la segunda matriz
        System.out.println("Ingrese el ancho y la altura de la segunda matriz:");
        int ancho1 = scanner.nextInt();
        int altura1 = scanner.nextInt();

        // Leer los valores de la segunda matriz
        System.out.println("Ingrese los valores de la segunda matriz:");
        int[][] matriz2 = readMatrix(ancho1, altura1);

        // Verificar si las matrices pueden multiplicarse
        if (ancho != altura1) {
            System.out.println("Error: El número de columnas de la primera matriz debe ser igual al número de filas de la segunda matriz.");
            return;
        }

        // Imprimir las matrices ingresadas por el usuario
        System.out.println("Matriz 1:");
        imprimir(matriz1);

        System.out.println("Matriz 2:");
        imprimir(matriz2);

        // Inicializar matriz resultado
        int[][] result = new int[altura][ancho1];

        // Realizar la multiplicación de matrices en paralelo
        multiplyMatricesParallel(matriz1, matriz2, result);

        // Imprimir la matriz resultante
        System.out.println("Matriz resultante:");
        imprimir(result);

        // Preguntar al usuario si desea guardar la matriz resultante en un archivo
        System.out.println("¿Desea guardar la matriz resultante en un archivo? (S/N)");
        String eleccion = scanner.next().toUpperCase();

        if (eleccion.equals("S")) {
        	guardarmatriz(result);
        }

        // Preguntar al usuario si desea realizar otra multiplicación de matrices
        System.out.println("¿Desea realizar otra multiplicación de matrices? (S/N)");
        eleccion = scanner.next().toUpperCase();

        if (eleccion.equals("S")) {
            main(args);
        }
    }

    // Método para leer una matriz desde la entrada estándar
    private static int[][] readMatrix(int ancho, int altura) {
        Scanner scanner = new Scanner(System.in);
        int[][] matrix = new int[altura][ancho];

        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < ancho; j++) {
                System.out.print("Ingrese el valor en la posición (" + (i + 1) + "," + (j + 1) + "): ");
                matrix[i][j] = scanner.nextInt();
            }
        }

        return matrix;
    }

    // Método para imprimir una matriz
    private static void imprimir(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Método para multiplicar matrices en paralelo
    private static void multiplyMatricesParallel(int[][] matriz, int[][] matriz2, int[][] res) {
        int numThreads = Math.min(matriz.length, Runtime.getRuntime().availableProcessors());
        Matrices[] threads = new Matrices[numThreads];
        int rowsPerThread = matriz.length / numThreads;
        int startRow = 0;

        for (int i = 0; i < numThreads; i++) {
            int endRow = (i == numThreads - 1) ? matriz.length : startRow + rowsPerThread;
            threads[i] = new Matrices(matriz, matriz2, res, startRow, endRow);
            startRow = endRow;
        }

        for (Matrices thread : threads) {
            thread.start();
        }

        try {
            for (Matrices thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para guardar una matriz en un archivo
    private static void guardarmatriz(int[][] matrix) {
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
