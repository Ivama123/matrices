class Matrices extends Thread {
    private int[][] matrix1;
    private int[][] matrix2;
    private int[][] result;
    private int startRow;
    private int endRow;

    // Constructor que recibe las matrices de entrada, la matriz de resultado y el rango de filas a procesar
    public Matrices(int[][] matrix1, int[][] matrix2, int[][] result, int startRow, int endRow) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    // Método run que se ejecutará cuando se inicie el hilo
    @Override
    public void run() {
    	multiplicar(); // Llama al método para multiplicar las matrices
    }

    // Método privado que realiza la multiplicación de matrices para el rango de filas asignado al hilo
    private void multiplicar() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }
}
