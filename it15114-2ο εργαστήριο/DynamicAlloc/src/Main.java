import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	private static int totalTasks;
	private static int nThreads; 
	private static int tasksAssigned = -1;

	public static void main(String[] args) {
		totalTasks = 2000; 
		nThreads = 10; 
		
		//���������� ������ �� �������� ����� ��� �� ������ �� ����������� ��� 
		//�� �������� ��� �� ������������� ���� ��� �������� ��� ������� ��� �� ��������� 
		//�� ���� �� ��� ����� ��� ������� putTask()
		ArrayList<Double> a = new ArrayList<>();
		for(int i = 0; i < totalTasks; i++) {
			a.add((double) (i + 3));
			System.out.println(a.get(i));
		}
		
		//���������� ��� ����� min ��� max ��� ��� ������, ��� ��� ������� �����������
		//��� ����������� ��� ����� ��� ���� ���������
		System.out.println("Please give min.");
		Scanner in = new Scanner(System.in);
		int min = in.nextInt();
		
		System.out.println("Please give max.");
		int max = in.nextInt();
		
		//���������� ��� ������� ��� ������� ��� ��������� ����
		Thread[] threads = new Thread[nThreads];
		for (int i = 0; i < threads.length; ++i)
			threads[i] = new Thread(new Worker(a,i, min, max));	

		//�������� ��� �������
		for (int i = 0; i < threads.length; ++i)
			threads[i].start();
		
		//������������ 
		long startTime = System.currentTimeMillis();

		//������� ��� ��� ���������� ��� �������
		for (int i = 0; i < threads.length; ++i) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {
				System.err.println("this should not happen");
			}
		}
		
		//������������
		long elapsedTimeMillis = System.currentTimeMillis()-startTime;
		
		System.out.println("");
		System.out.println("Results:");
		
		//�������� ��� ������������� ���� �����
		for(int i = 0; i < totalTasks; i++)
			System.out.println(a.get(i));
		
		//������ ��� ��� ��������� ��� ��������
		System.out.println("Time elapsed: " + elapsedTimeMillis + " ms.");
	}

	//������� ��� ��� ������� �������� ����� Workers
	private static synchronized int getTask() {
		if (++tasksAssigned < totalTasks) 
			return tasksAssigned;
		else
			return -1;
	}
	
	//������� ��� ������� ��� ������ ��� totalTasks, ���������� ��� ���� ����� ���� �����
	//��� ��������� ��� �������� ���� ����������� �� ����� �� ��� ����� ��� add()
	private static synchronized void putTask(ArrayList list, double result) {
		totalTasks++;
		list.add(result);
	}

	private static class Worker implements Runnable {

		private int myID;
		private ArrayList list;
		private static int min;
		private static int max;
		//�������� �������� ��� �� �������� ���� ������ 10 ����������� ��� �� ����������� 
		//�������� ���� ���� ���� ��� ����������� ��� ���� ���� �� ��������� ��������
		private int counter = 0;

		// 
		public Worker(ArrayList aList, int myID, int min, int max)
		{
			this.myID = myID;
			this.list = aList;
			this.min = min;
			this.max = max;
		}

		public void run() {
			
			int element;
			
			//��� �������� �������� ��� �����������
			while ((element = getTask()) >= 0) {
				//�� ������� ���� ������ 10 �������� ��� ���� �������� � worker
				//������� ���� �������� �������� ��� putTask()
				if(counter < 10) {
					double temp = 0;
					int rand = (int)(Math.random()*((max-min)+1))+min;
					for(int i = 0; i < rand; i++) {
						temp = Math.sqrt((double) list.get(element));
					}
					list.set(element, temp);
					//����� putTask()
					putTask(list, temp);
				//�� ������� ���� ��� 10 ������ ��������, ���� �������������� �� ��������	
				}else {
					double temp = 0;
					int rand = (int)(Math.random()*((max-min)+1))+min;
					for(int i = 0; i < rand; i++) {
						temp = Math.sqrt((double) list.get(element));
					}
					list.set(element, temp);
				}
				//������ ��� �������
				counter++;
				System.out.println("worker " + myID + " received " + element);  
			}
			System.out.println("worker " + myID + " done");
		}
	}
}