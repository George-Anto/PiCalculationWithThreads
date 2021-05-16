
public class Main {
	
	static long numSteps = 1000000000;
	//Ενδεικτικά δημιουργούμε 4 νήματα
	static int numThreads = 4;
	//Η μοιραζόμενη μεταβλητή που θα αποθηκευτεί το τελικό π
	static double Pi = 0;

	public static void main(String[] args) {
        
        double step = 1.0 / (double)numSteps;
        
        Threads threads[] = new Threads[numThreads];
        for(int i = 0; i < numThreads; i++) {
        	threads[i] = new Threads(i, step);
        	threads[i].start();
        }
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
           		} catch (InterruptedException e) {}
		}
		
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
			//Κλείδωμα της κρίσιμης περιοχής με χρήση του synchronized και όρισμα 
			//την κλάση των νημάτων για να εισέρχεται σε αυτήν κάθε φορά μόνο το 
			//νήμα που έχει το κλειδί του αντικειμένου Threads
			synchronized(getClass()) {
				Pi += sum * step;
			}
		}
	}
}