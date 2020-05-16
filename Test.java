import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
	//all necessary variables defined here.
	private static int x = 25;
	private static int c = 5;
	private static int B = 100;
	private static int t = 2;
	private static int p = 5;
	private static int d = 5;
	private static double maxProfitGreedy = 0;
	private static double maxProfitDP = 0;
	private static double[][] investment = new double[x][c];
	private static double[][] investmentSolution = new double[x][c];
	private static double[] monthDemand = new double[x];

	public static void main(String[] args) throws IOException {
		readInvestmentFile(x,c);
		readDemandFile(x);
		investmentSolution = copy2Darray(investment, investmentSolution);
		maxProfitGreedy = greedyMaximizeProfit();
		investmentSolution = copy2Darray(investment, investmentSolution);
		System.out.println("Greedy Profit: " + maxProfitGreedy);
		maxProfitDP = dpMaximizeProfit();
		System.out.println("DP Profit: " + maxProfitDP);
	}
	
	public static double dpMaximizeProfit() {
		double maximum = 0; 
		double firstHalfMoney = 0;
		double secondHalfMoney = 0;
		double tempMoney = 0;
		double[] temp = new double[investment[0].length];
		
		for (int i = 0; i < investment.length; i++) {
			firstHalfMoney = monthDemand[i]*B/2;
			
			if (i+1 < x) {secondHalfMoney = firstHalfMoney + monthDemand[i+1]*B/2;}
			else {secondHalfMoney = firstHalfMoney;}

			for (int j = 0; j < investment[i].length; j++) {
				maximum = 0;
				if(i == 0) {
					investmentSolution[i][j] = firstHalfMoney + firstHalfMoney*investment[i][j]/100;
				}
				else {
					for (int k = 0; k < investment[i].length; k++) {
						tempMoney = investmentSolution[i-1][k];
						if(k == j) {
							temp[k] = tempMoney + tempMoney*investment[i][k]/100;
						}
						else {
							tempMoney = tempMoney - tempMoney*t/100;
							temp[k] = tempMoney + tempMoney*investment[i][k]/100;
						}
					}
					for(int k = 0; k < investment[i].length; k++) {
						if (temp[k] > maximum) {
							maximum = temp[k];
						}
					}
					investmentSolution[i][j] = maximum;
				}
				investmentSolution[i][j] += secondHalfMoney;
			}
			
			if (i == investment.length-1) {
				for (int j = 0; j < investmentSolution[i].length; j++) {
					if (investmentSolution[i][j] > maximum) {
						maximum = investmentSolution[i][j];
					}
				}
			}
		}
		return maximum;
	}

	public static double greedyMaximizeProfit() {
		double maximum = 0; 
		double jtemp = Integer.MAX_VALUE; 
		double firstHalfMoney = 0; 
		double secondHalfMoney = 0; 
		double collectedMoney =  monthDemand[0]*B/2;
		double tempcollected = 0;

		for (int i = 0; i < investment.length; i++) {
			firstHalfMoney = monthDemand[i]*B/2;

			if (i+1 < x) {secondHalfMoney = firstHalfMoney + monthDemand[i+1]*B/2;}
			else {secondHalfMoney = firstHalfMoney;}

			for (int j = 0; j < investment[i].length; j++) {

				if (jtemp == j || jtemp == Integer.MAX_VALUE) {
					investmentSolution[i][j] = collectedMoney + collectedMoney*investment[i][j]/100;
				}
				else {
					tempcollected = tempcollected - tempcollected*t/100;
					investmentSolution[i][j] = tempcollected + tempcollected*investment[i][j]/100;
					tempcollected = collectedMoney;
				}

				investmentSolution[i][j] = investmentSolution[i][j] + secondHalfMoney;
			}
			
			for (int j = 0; j < investmentSolution[i].length; j++) {
				if (investmentSolution[i][j] > maximum) {
					maximum = investmentSolution[i][j];
					jtemp = j;
				}
			}
			
			collectedMoney = maximum;
			tempcollected = collectedMoney;
		}
		
		return collectedMoney;
	}

	//readfile method for investment part.
	public static void readInvestmentFile(int x, int c) throws IOException {
		File file = new File("investment.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st = br.readLine(); //to pass first line.
		String[] sts = null; //to split a line.
		int xtemp = 0;
		while ((st = br.readLine()) != null && xtemp < x) {
			sts = st.split("\t");
			for (int i = 1; i < c+1; i++) {
				investment[xtemp][i-1] = Integer.parseInt(sts[i]);
			}
			xtemp++;
		}
		br.close();
	} 

	//readfile method for month_demand.
	public static void readDemandFile(double x) throws IOException {
		File file = new File("month_demand.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st = br.readLine(); //to pass first line.
		String[] sts = null; //to split a line.
		int xtemp = 0;
		while ((st = br.readLine()) != null && xtemp < x) {
			sts = st.split("\t");
			monthDemand[xtemp] = Integer.parseInt(sts[1]);
			xtemp++;
		}
		br.close();
	}

	//2d array print method.
	public static void print2Darray(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}

	//2d array copy method to reuse arrays.
	public static double[][] copy2Darray(double[][] toCopyArray, double[][] copiedArray) {
		for (int i = 0; i < toCopyArray.length; i++) {
			for (int j = 0; j < toCopyArray[i].length; j++) {
				copiedArray[i][j] = toCopyArray[i][j];
			}
		}
		return copiedArray;
	}
}