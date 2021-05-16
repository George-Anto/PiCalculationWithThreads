import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	private static int totalTasks;
	private static int nThreads; 
	private static int tasksAssigned = -1;

	public static void main(String[] args) {
		totalTasks = 2000; 
		nThreads = 10; 
		
		//Δημιουργία λίστας με δυναμικό τρόπο για να μπορεί να αποθηκεύσει και 
		//τα στοιχεία που θα δημιουργηθούν κατά την εκτέλεση των νημάτων και θα περαστούν 
		//σε αυτή με την κλήση της μεθόδου putTask()
		ArrayList<Double> a = new ArrayList<>();
		for(int i = 0; i < totalTasks; i++) {
			a.add((double) (i + 3));
			System.out.println(a.get(i));
		}
		
		//Καθορισμός των ορίων min και max από τον χρήστη, για τις τυχαίες επαναλήψεις
		//του υπολογισμού της ρίζας του κάθε στοιχείου
		System.out.println("Please give min.");
		Scanner in = new Scanner(System.in);
		int min = in.nextInt();
		
		System.out.println("Please give max.");
		int max = in.nextInt();
		
		//Δημιουργία των νημάτων και πέρασμα των ορισμάτων τους
		Thread[] threads = new Thread[nThreads];
		for (int i = 0; i < threads.length; ++i)
			threads[i] = new Thread(new Worker(a,i, min, max));	

		//Ξεκίνημα των νημάτων
		for (int i = 0; i < threads.length; ++i)
			threads[i].start();
		
		//Χρονομέτρηση 
		long startTime = System.currentTimeMillis();

		//Αναμονή για τον τερματισμό των νημάτων
		for (int i = 0; i < threads.length; ++i) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {
				System.err.println("this should not happen");
			}
		}
		
		//Χρονομέτρηση
		long elapsedTimeMillis = System.currentTimeMillis()-startTime;
		
		System.out.println("");
		System.out.println("Results:");
		
		//Εμφάνιση των αποτελεσμάτων στην οθόνη
		for(int i = 0; i < totalTasks; i++)
			System.out.println(a.get(i));
		
		//Χρόνος για την υλοποίηση των εργασιών
		System.out.println("Time elapsed: " + elapsedTimeMillis + " ms.");
	}

	//Μέθοδος για την ανάθεση εργασιών στους Workers
	private static synchronized int getTask() {
		if (++tasksAssigned < totalTasks) 
			return tasksAssigned;
		else
			return -1;
	}
	
	//Μέθοδος που αυξάνει των αριθμό των totalTasks, δημιουργεί μια θέση ακόμα στην λίστα
	//και προσθέτει ένα στοιχείο προς επεξεργασία σε αυτήν με την κλήση της add()
	private static synchronized void putTask(ArrayList list, double result) {
		totalTasks++;
		list.add(result);
	}

	private static class Worker implements Runnable {

		private int myID;
		private ArrayList list;
		private static int min;
		private static int max;
		//Ιδιότητα μετρητής για να μπορούμε στις πρώτες 10 επαναλήψεις και να προσθέτουμε 
		//στοιχεία πίσω στην ουρά για επεξεργασία ενώ μετά μόνο να παίρνουμε στοιχεία
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
			
			//Όσο υπάρχουν στοιχεία για επεξεργασία
			while ((element = getTask()) >= 0) {
				//Αν είμαστε στις πρώτες 10 εργασίες που έχει αναλάβει ο worker
				//βάζουμε πίσω εργασίες καλώντας την putTask()
				if(counter < 10) {
					double temp = 0;
					int rand = (int)(Math.random()*((max-min)+1))+min;
					for(int i = 0; i < rand; i++) {
						temp = Math.sqrt((double) list.get(element));
					}
					list.set(element, temp);
					//Κλήση putTask()
					putTask(list, temp);
				//Αν είμαστε μετά τις 10 πρώτες εργασίες, μόνο επεξεργαζόματε τα δεδομένα	
				}else {
					double temp = 0;
					int rand = (int)(Math.random()*((max-min)+1))+min;
					for(int i = 0; i < rand; i++) {
						temp = Math.sqrt((double) list.get(element));
					}
					list.set(element, temp);
				}
				//Αύξηση του μετρητή
				counter++;
				System.out.println("worker " + myID + " received " + element);  
			}
			System.out.println("worker " + myID + " done");
		}
	}
}