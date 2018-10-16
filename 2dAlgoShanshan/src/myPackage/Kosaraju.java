package myPackage;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
// The CNF being handled is: 
// '+' implies 'OR' and '*' implies 'AND' 
// (x1+x2)*(x2¡¯+x3)*(x1¡¯+x2¡¯)*(x3+x4)*(x3¡¯+x5)* 
// (x4¡¯+x5¡¯)*(x3¡¯+x4) 
// our input parameters would be a (1)list of list (2)#of variables (3)#of clauses
//a = [[1,2],[-2,3],[-1,-2],[3,4],[-3,5],[-4,-5],[-3,4]]
public class Kosaraju {
	int variableNum = 0;//number of variables
	int clauseNum = 0;
	ArrayList<ArrayList<Integer>> input = new ArrayList<ArrayList<Integer>>();
	//Boolean marked[] = new Boolean[variableNum];
	//constructor
	Kosaraju(int arg1, int arg2, ArrayList arg3){
		this.variableNum = arg1;
		this.clauseNum = arg2;
		this.input = arg3;
	}
	//argG = the args needed to generate graph;
	public Graph genGraph() {
		int vertices = 2*this.variableNum;
		Graph g = new Graph(vertices);
		for (int i = 0; i<this.clauseNum; i++) {
			int literal1 = this.input.get(i).get(0);
			int literal2 = this.input.get(i).get(1);
			int index1;
			int index2;
			int index1Inv;
			int index2Inv;
			if(literal1<0) {
				index1 = this.variableNum-literal1-1;
				index1Inv = (-1)*literal1-1;
			}else {
				index1 = literal1-1;
				index1Inv = this.variableNum+literal1-1;
			}
			if(literal2<0) {
				index2 = this.variableNum-literal2-1;
				index2Inv = (-1)*literal2-1;
			}else {
				index2 = literal2-1;
				index2Inv = this.variableNum+literal2-1;
			}
			g.addEdge(index1Inv,index2); 
			g.addEdge(index2Inv,index1);
		}
		return g;
	}
	public String genSCC(){
		String output;
		output = this.genGraph().printSCCs();
		return output;
	}
	public void isSatisfiable() {
		String SCC = this.genSCC();
		String[] words = SCC.trim().split(" ");
		for(String w:words){    
			for(int i = 0; i < w.length() ; i++) { 
			    char cFirst = w.charAt(i); 
			    for(int i2 = i; i2 < w.length(); i2++) {
			    	char cSecond = w.charAt(i2);
			    	if (Character.getNumericValue(cFirst)+5==Character.getNumericValue(cSecond)||
			    			Character.getNumericValue(cFirst)-5==Character.getNumericValue(cSecond)) {
			    		System.out.println("not satisfiable");
			    		return;
			    	}
			    }
			}
		}
		System.out.println("satisfiable");
		return;
	}
	public static void main(String[] args) {
		//(x1+x2)*(x2¡¯+x3)*(x1¡¯+x2¡¯)*(x3+x4)*(x3¡¯+x5)* 
	    // (x4¡¯+x5¡¯)*(x3¡¯+x4) 
		int n = 5;
		int m = 7;
		
		ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>();
	    ArrayList<Integer> inner = new ArrayList<Integer>();        

	    inner.add(1);     
	    inner.add(2);
	    outer.add(inner); // add first list
	    inner = new ArrayList<Integer>(); // create a new inner list that has the same content as  
	                                           // the original inner list
	    inner.add(-2);     
	    inner.add(3);
	    outer.add(inner); // add first list
	    inner = new ArrayList<Integer>();
	    
	    inner.add(-1);     
	    inner.add(-2);
	    outer.add(inner); // add first list
	    inner = new ArrayList<Integer>();
	    
	    inner.add(3);     
	    inner.add(4);
	    outer.add(inner); // add first list
	    inner = new ArrayList<Integer>();
	    
	    inner.add(-3);     
	    inner.add(5);
	    outer.add(inner); // add first list
	    inner = new ArrayList<Integer>();
	    
	    inner.add(-4);     
	    inner.add(-5);
	    outer.add(inner); // add first list
	    inner = new ArrayList<Integer>();
	    
	    inner.add(-3);     
	    inner.add(4);
	    outer.add(inner); // add first list
	    
	    System.out.println(outer);
	    Kosaraju k = new Kosaraju(n,m,outer);
	    String SCC = k.genSCC();
		//System.out.println(SCC);
		k.isSatisfiable();
		
		
	}

}
