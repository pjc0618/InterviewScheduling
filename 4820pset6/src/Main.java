import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;

class Main
{
	int n; // number of candidates
	int k; // number of recruiters

	// provided data structures (already filled in)
	ArrayList<ArrayList<Integer>> neighbors;
	int[] recruiterCapacities;
	int[] preliminaryAssignment;

	// provided data structures (you need to fill these in)
	boolean existsValidAssignment;
	int[] validAssignment;
	int[] bottleneckRecruiters;

	// reading the input
	void input()
	{
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new InputStreamReader(System.in));

			String text = reader.readLine();
			String[] parts = text.split(" ");

			n = Integer.parseInt(parts[0]);
			k = Integer.parseInt(parts[1]);
			neighbors = new ArrayList<ArrayList<Integer>>(n+k);
			recruiterCapacities = new int[n+k];
			preliminaryAssignment = new int[n];

			for (int j = 0; j < n+k; j++) {
				neighbors.add(new ArrayList<Integer>());
			}
			for (int i = 0; i < n; i++) {
				text = reader.readLine();
				parts = text.split(" ");
				int numRecruiters = Integer.parseInt(parts[0]);
				for (int j = 0; j < numRecruiters; j++) {
					int recruiter = Integer.parseInt(parts[j+1]);
					neighbors.get(i).add(recruiter);
					neighbors.get(recruiter).add(i);
				}
			}

			text = reader.readLine();
			parts = text.split(" ");
			for (int j = 0; j < k; j++) {
				recruiterCapacities[n+j] = Integer.parseInt(parts[j]);
			}

			text = reader.readLine();
			parts = text.split(" ");
			for (int i = 0; i < n-1; i++) {
				preliminaryAssignment[i] = Integer.parseInt(parts[i]);
			}

			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// writing the output
	void output()
	{
		try
		{
			PrintWriter writer = new PrintWriter(System.out);

			if (existsValidAssignment) {
				writer.println("Yes");
				for (int i = 0; i < n-1; i++) {
					writer.print(validAssignment[i] + " ");
				}
				writer.println(validAssignment[n-1]);
			} else {
				writer.println("No");
				for (int j = 0; j < n+k-1; j++) {
					writer.print(bottleneckRecruiters[j] + " ");
				}
				writer.println(bottleneckRecruiters[n+k-1]);
			}

			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Main()
	{
		input();

		// Fill these in as instructed in the problem statement.
		existsValidAssignment = false;
		validAssignment = new int[n];
		bottleneckRecruiters = new int[n+k];
		//YOUR CODE STARTS HERE
		int[] candidates = new int[k];//number of candidates a recruiter is interviewing
		ArrayList<ArrayList<Integer>> edges;
//		edges= new ArrayList<ArrayList<Integer>>(n+k+2);
//		for (int j = 0; j < n+k+2; j++) {
//			edges.add(new ArrayList<Integer>());
//		}
//		for(int i = 0; i < n+k; i++) {
//			if(i<n)	{
//				//edges.get(n+k).add(i);//adds edge in residual from source to each candidate
//				ArrayList<Integer> neigh = neighbors.get(i);
//				int pre = preliminaryAssignment[i];
//				for(int j = 0; j <neigh.size();j++) {//adding edges between candidate and neighbors
//					if(i<n-1 && neigh.get(j)==pre) {
//						edges.get(pre).add(i);//adds backwards edge for all preliminary assignments
////						System.out.println(preliminaryAssignment[i]);
////						System.out.println(k);
////						System.out.println(candidates.length);
//						candidates[pre-n]++;
//					}else edges.get(i).add(neigh.get(j));
//				}
//			}else {//add edges between sink and recruiters
//				 if(recruiterCapacities[i]>candidates[i-n]) {
//					 edges.get(i).add(n+k+1);//if there is still capacity there is an edge
//				 }
//			}
//		}
		int[] path = new int [n+k+2];
		boolean [] visited = new boolean[n+k+2];
		for(int i = 0; i<n+k+2; i++) {
			visited[i] = false;
		}
		for(int i = 0; i<n+k; i++) {
			ArrayList<Integer> neigh = neighbors.get(i);
			if (i < n-1) {
				int pre = preliminaryAssignment[i];
				neighbors.get(i).remove(neighbors.get(i).indexOf(pre));
				candidates[pre-n]++;
			}if(i>n-1) {
				for(int pots = 0; pots<neigh.size(); pots++) {
					if(preliminaryAssignment[neigh.get(pots)]!=i) {
						neighbors.get(i).remove(pots); pots--;
					}
				}
				if(candidates[i-n]<recruiterCapacities[i]) {
					neighbors.get(i).add(n+k+1);
				}
			}
		}
		PriorityQueue<Integer> q = new PriorityQueue<Integer>();
		q.add(n-1); visited[n-1] = true; path[n-1]=-1;
		while(q.size()>0&&!visited[n+k+1]) {
			int v = q.poll();
			//System.out.println(v);
			//System.out.println(edges.get(v));
			//System.out.println(neighbors.get(v));
			ArrayList<Integer> neigh = neighbors.get(v);
			for (int x = 0; x<neigh.size();x++) {
				int x1 = neigh.get(x);
				if(!(visited[x1])) {
					q.add(x1);visited[x1]=true;
					path[x1] = v;
				}
			}
		}
		if(visited[n+k+1]) {
			existsValidAssignment = true;
			validAssignment = preliminaryAssignment;
			int c = n+k+1;
			while(c>-1) {
				int temp=path[c];
				if(c>=n && temp <n) {
					validAssignment[temp]=c;
				}
				c = temp;
			}
		}else {
			for(int y = 0; y<n+k; y++) {
				if(visited[y]&&y>=n) {
					bottleneckRecruiters[y]=1;
				}
			}
		}
		//YOUR CODE ENDS HERE
		
		output();
	}

    // Strings in Args are the name of the input file followed by
    // the name of the output file.
	public static void main(String [] Args) 
	{
		new Main();
	}
	
}
