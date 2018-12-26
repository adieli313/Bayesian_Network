import java.io.*;
import java.util.ArrayList; 
public class Var {

	String Name;
	ArrayList<String> values;
	ArrayList<String> parents;
	ArrayList<String> CPT;
	ArrayList<ArrayList<String>> VarTable;
	ArrayList<ArrayList<ArrayList<String>>> VarPrntTables;
	
public Var(){	
	Name = "";
	values = new ArrayList<>();
	parents = new ArrayList<>();
	CPT = new ArrayList<>();
	VarTable =new ArrayList<ArrayList<String>>();
	VarPrntTables = new ArrayList<ArrayList<ArrayList<String>>>();
}

//------Getters------//

public String getVarName(){
	return this.Name;
}

public ArrayList<String> getValues(){
	return this.values;
}

public ArrayList<String> getParents(){
	return this.parents;
}

public ArrayList<String> getCPT(){
	return this.CPT;
}

public ArrayList<ArrayList<String>> getVarTable(){
	return this.VarTable;
}

public ArrayList<ArrayList<ArrayList<String>>> getVarPrntTables(){
	return this.VarPrntTables;
}
//------Setters------//

public void setVarName(String v){
	this.Name = v;
}

public void setValues(ArrayList<String> v){
	this.values = v;
}

public void setParents(ArrayList<String> v){
	this.parents = v;
}

public void setCPT(ArrayList<String> v){
	this.CPT = v;
}
public void setVarTables(ArrayList<ArrayList<String>> v){
	this.VarTable = v;
}
public void setVarPrntTables(ArrayList<ArrayList<ArrayList<String>>> v){
	this.VarPrntTables = v;
}
}