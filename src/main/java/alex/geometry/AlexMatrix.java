package alex.geometry;

import Jama.Matrix;

public class AlexMatrix {
	private Matrix matrix;
	private int rows, cols;
	
	
	public AlexMatrix(double[][] array){
		this.matrix = new Matrix(array);
		this.rows = array.length;
		this.cols = array[0].length;
	}
	
	public AlexMatrix(Matrix matrix, int rows, int cols) {
		super();
		this.matrix = matrix;
		this.rows = rows;
		this.cols = cols;
	}
	
	public double get(int row, int col){
		return this.matrix.get(row, col);
	}

	public AlexMatrix inverse(){
		//dimensions stay the same
		return new AlexMatrix(this.matrix.inverse(), this.rows, this.cols);
	}
	
	public AlexMatrix mult(AlexMatrix matrix2){
		return new AlexMatrix(this.matrix.times(matrix2.matrix), this.rows ,matrix2.cols);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < rows; i++){
			sb.append("[");
			for(int j=0; j < this.cols; j++){
				sb.append(this.matrix.get(i, j));
				if(j != this.cols-1){
					sb.append(", ");
				}
			}
			sb.append("]");
			if(i != rows-1){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}