import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
	//all algorithm is explained in report so code is not involving commands.
	
	//all necessary variables defined here.
	private static int x = 5;
	private static int c = 6;	
	private static int B = 100;
	private static int t = 2;
	private static int p = 2;
	private static int d = 2;
	private static double maxProfitGreedy = 0;
	private static double maxProfitDP = 0;
	private static double minCostGreedy = 0;
	private static double minCostDP = 0;
	private static double[][] investment = new double[x][c];
	private static double[][] investmentSolution = new double[x][c];
	private static double[] monthDemand = new double[x];
	private static double[] garageCost = new double[311];
	private static double[][] costSolution;

	public static void main(String[] args) throws IOException {
		//read file functions calling.
		readInvestmentFile(x,c);
		readDemandFile(x);
		readGarageCostFile();
		
		//copy investment array to make calculations for greedy approach.
		investmentSolution = copy2Darray(investment, investmentSolution);
		maxProfitGreedy = greedyMaximizeProfit();
		
		//copy investment array again to start calculations for dynamic programming approach.
		investmentSolution = copy2Darray(investment, investmentSolution);
		maxProfitDP = dpMaximizeProfit();
		
		//Dynamic programming and greedy approach to minimize cost.
		minCostDP = dpMinimizeCost();
		minCostGreedy = greedyMinimizeCost();
		
		//result printers.
		System.out.println("Greedy Profit: " + maxProfitGreedy);
		System.out.println("DP Profit: " + maxProfitDP);
		System.out.println("Greedy Cost: " + minCostGreedy);
		System.out.println("DP Cost: " + minCostDP);
	}

	public static double dpMinimizeCost() {
		int difference = 0;
		double[] monthDemandTemp = new double[x+1];
		double tempcost = 0;
		double cost = Integer.MAX_VALUE;
		double minimum = Integer.MAX_VALUE;
		int sumOfDemand = 0;
		for (int i = 1; i < monthDemandTemp.length; i++) {
			monthDemandTemp[i] = monthDemand[i-1];
		}
		for (int i = 0; i < monthDemand.length; i++) {
			sumOfDemand = sumOfDemand + (int)monthDemand[i];
		}
		costSolution = new double[monthDemand.length+1][sumOfDemand];
		for (int i = 0; i < monthDemandTemp.length; i++) {
			difference = (int) monthDemandTemp[i] - p;
			for (int j = 0; j < sumOfDemand; j++) {
				cost = Integer.MAX_VALUE;
				if(i == 0) {
					costSolution[i][j] = garageCost[j];
				}
				else {
					if (difference < 0) {
						for (int k = 0; k < sumOfDemand; k++) {
							tempcost = garageCost[j] + costSolution[i-1][k];
							if (tempcost < cost) {
								cost = tempcost;
							}
						}
						costSolution[i][j] = cost;
					}
					else if (difference >= 0) {
						for (int k = 0; k <= difference+j; k++) {
							if (k < sumOfDemand) {
								tempcost = ((difference+j)-k)*d + costSolution[i-1][k] + garageCost[j];
								if (tempcost < cost) {
									cost = tempcost;
								}
							}
						}
						costSolution[i][j] = cost;
					}
				}
			}
		}
		for (int i = 0; i < costSolution[0].length; i++) {
			if (costSolution[costSolution.length-1][i] < minimum) {
				minimum = costSolution[costSolution.length-1][i];
			}
		}
		return minimum;
	}
	
	public static double greedyMinimizeCost() {
		double difference = 0;
		double cost = 0;
		for (int i = 0; i < monthDemand.length; i++) {
			difference = (monthDemand[i] - p);
			if (difference > 0) {
				cost += difference*d;
			}
		}
		return cost;
	}

	public static int dpMaximizeProfit() {
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
		return (int) maximum;
	}

	public static int greedyMaximizeProfit() {
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
		return (int) collectedMoney;
	}

	//readfile method for investment file.
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

	//readfile method for month_demand file.
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

	//readfile method for garage_cost file.
	public static void readGarageCostFile() throws NumberFormatException, IOException {
		garageCost[0] = 0;
		File file = new File("garage_cost.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st = br.readLine(); //to pass first line.
		String[] sts = null; //to split a line.
		int index = 1;
		while ((st = br.readLine()) != null) {
			sts = st.split("\t");
			garageCost[index] = Integer.parseInt(sts[1]);
			index++;
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
	
	//1D array print method.
	public static void printArray(double[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
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