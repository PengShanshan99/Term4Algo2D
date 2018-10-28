package myPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
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
	Kosaraju(int arg1, int arg2, ArrayList<ArrayList<Integer>> arg3){
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
		for(int i = 0; i < words.length; i++){
			String[] literals = words[i].split(",");
			Integer[] literalsnum = new Integer[literals.length];
			HashMap<Integer, Integer> myHash = new HashMap<Integer, Integer>();
			for (int i1 = 0; i1 < literals.length; i1++) {
				myHash.put(Integer.parseInt(literals[i1]), 0);
				literalsnum[i1] = Integer.parseInt(literals[i1]);
			}
			for (int i2 = 0; i2 < literalsnum.length; i2++) {
				if (myHash.containsKey(literalsnum[i2]+5)||myHash.containsKey(literalsnum[i2]-5)) {
					System.out.println("FORMULA UNSATISFIABLE");
					return;
				}
			}
		}
		System.out.println("FORMULA SATISFIABLE");
		return;
	}
	public static void main(String[] args) throws IOException{
		
		int n = 0;//number of variables
		int m = 0;//number of clauses
		Boolean startParsing = Boolean.FALSE;
		ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>();//list of clauses
	    ArrayList<Integer> inner = new ArrayList<Integer>(2); //clauses
	    
	    //reading .cnf file
	    String userDirectory = System.getProperty("user.dir");
    	File file = new File(userDirectory+"\\sampleCNF\\debug.cnf");
        Scanner input = new Scanner(file);
        ArrayList<String> list = new ArrayList<String>();//each line is stored as an element of the list
        while (input.hasNextLine()) {
            list.add(input.nextLine());
        }
        input.close();
        int numberOfLines = list.size();
        
        //store input in a list of list
        for(int i=0; i<numberOfLines; i++){
            if(list.get(i).length()>0){ //skip empty lines
                char a = list.get(i).charAt(0); 
                String preamble = Character.toString(a);
                if(preamble.equals("p")){  //problem line
                    String[] splitP = list.get(i).split("\\s+");
                    //reading variable number info from problem line
                    n = Integer.parseInt(splitP[2]);
                    m = Integer.parseInt(splitP[3]);
                    startParsing = Boolean.TRUE;
                } else if (startParsing) {   //start parsing after problem line
                    String tempString = list.get(i);
                    tempString = tempString.trim();
                    String[] temp = tempString.split("\\s+");
                    for(String x:temp){
                    	int currentLiteral = Integer.parseInt(x);
                        if(currentLiteral != 0) {
                            inner.add(currentLiteral);
                        } else{
                        	outer.add(inner);
                        	inner = new ArrayList<Integer>(2); 
                        }
                    }
                }
            }
        }

	    Kosaraju k = new Kosaraju(n,m,outer);
	    String SCC = k.genSCC();
		System.out.println(SCC);
		k.isSatisfiable();
		
		
	}

}
