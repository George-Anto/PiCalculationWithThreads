
public class Main {
	
	static long numSteps = 1000000000;
	//Ενδεικτικά δημιουργούμε 10 νήματα
	static int numThreads = 10;
	//Πίνακας με τόσες θέσεις όσα και τα νήματα για να αποθηκεύει τα μερικά αθροίσματα
	static double[] partialPi = new double[numThreads];

	public static void main(String[] args) {
        
        double step = 1.0 / (double)numSteps;
        
        for(int i = 0; i < numThreads; i++)
        	partialPi[i] = 0.0;
        
        Threads threads[] = new Threads[numThreads];
        for(int i = 0; i < numThreads; i++) {
        	threads[i] = new Threads(i, step);
        	threads[i].start();
        }
        
        //Χρονομέτρηση
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
           		} catch (InterruptedException e) {}
		}
        
        //’θροιση των μερικών αθροισμάτων στο τελικό αποτέλεσμα 
        double Pi = 0.0;
		for (int i = 0; i < numThreads; i++) 
			Pi +=partialPi[i];
		
		//Χρονομέτρηση 
		long elapsedTimeMillis = System.currentTimeMillis()-startTime;
		double elapsedTime = (double)elapsedTimeMillis / (double)1000;
	
		
		System.out.println("sequential program results with " + numSteps + " steps.");
		System.out.println("Computed Pi = " +Pi + ".");
		System.out.println("Difference between estimated Pi and Math.PI = " + Math.abs(Pi - Math.PI) + ".");
		System.out.println("Time to compute: " + elapsedTime + " seconds.");
	}
	
	public static class Threads extends Thread {
		
		private int id;
		private double step;
		private long threadPortion;
		private long start;
		private long end;
		
		public Threads(int id, double step) {
			this.id = id;
			this.step = step;
			//Υπολογισμός το φόρτου εργασίας κάθε νήματος και των σημείων που θα ξεκιναάει 
			//και θα τελειώνει τους υπολογισμούς του
			this.threadPortion = numSteps / numThreads;
			this.start = id * threadPortion;
			this.end = (id + 1) * threadPortion;
			if(id == (numThreads - 1))
				this.end = numSteps;
		}
		
		public void run() {
			
			double sum = 0;
			
			for (long i = start; i < end; i++) {
				double x = ((double)i+0.5)*step;
				sum += 4.0/(1.0+x*x);
		     	}
			
			//Ενημέρωση του στοιχείου του πίνακα που θα φιλοξενήσει το μερικό άθροισμα
			partialPi[id] = sum * step;
		}
	}
}
