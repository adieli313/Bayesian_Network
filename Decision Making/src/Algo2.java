import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.*;


public class Algo2 extends Functions {

	read r ;
	Functions f;


	ArrayList<String> Hiddens =new ArrayList<String>();
	ArrayList <String> HdnPrnt = new ArrayList<String>();

	ArrayList<String> row =new ArrayList<String>();
	ArrayList<ArrayList<String>> tbl =new ArrayList<ArrayList<String>>();

	//ArrayList<Hdn>
	public Algo2(){
	}

	public Algo2(read v,Functions f){
		this.f = f;
		this.r = v;
		reset();
	}

	public void RemoveIrrelevants(){
		for(String i: f.Hiddens){
			for(String v: r.Variables){
				if(r.Tree.get(v).parents.contains(i)&&NotqueryVars.contains(i)){
					r.Tree.remove(i);
					f.Hiddens.remove(i);
				}
			}
		}
	}
	public void SetEvidenceVars(){
		f.evidenceVars = new ArrayList<>();
		String sp[] = f.queryParts.get(1).split(",");
		for(int i = 0 ; i < sp.length-1  ; i++){
			f.evidenceVars.add(sp[i]);
		}
	}
	public ArrayList<String> AtachedArrays(ArrayList<String> prnt, ArrayList<String> Row){
		ArrayList<String> temp = new ArrayList<String>();
		for(String p: prnt){
			temp.add(p);
		}
		for(String r: Row){
			temp.add(r);
		}
		//System.out.println(temp);
		return temp;

	}
	// Get Cpt line and crate missing lines
	public ArrayList<ArrayList<String>> EditCptLine(String cpt , String var){
		row = new ArrayList<>();
		ArrayList<String> prntarr = new ArrayList<>();
		ArrayList<String> temp = new ArrayList<>();
		ArrayList<ArrayList<String>> Cptrow = new ArrayList<ArrayList<String>>();

		int prntsSize = r.Tree.get(var).parents.size();
		if(r.Tree.get(var).parents.contains("none"))prntsSize = 0;
		String sp[];
		double sum = 1 , hold = 0;

		int  count = 0;
		sp = cpt.split(",");

		for (int i = 0; i < prntsSize; i++) {

			prntarr.add(sp[i]);

		}

		//Cptrow.add(AtachedArrays(prntarr, row));
		row = new ArrayList<>();
		for(int j = prntsSize, k= 2 ; j < sp.length ; j++ , k++){
			row.add(sp[j]);
			if(k % 2 == 1 ){
				hold  = hold + Double.valueOf(sp[j]);
				//System.out.println("hold" + hold);
				Cptrow.add(AtachedArrays(prntarr, row));
				row = new ArrayList<>();
				count++;
			}
		}

		String tmp = "=" + r.Tree.get(var).values.get(count);
		row.add(tmp);
		row.add(String.valueOf(1 - hold));

		Cptrow.add(AtachedArrays(prntarr, row));

		return Cptrow;
	}

	public void EditVarTable(String var){

		for(String cpt : r.Tree.get(var).CPT){
			r.Tree.get(var).VarTable = EditCptLine(cpt , var);
		}
		//System.out.println(r.Tree.get(var).VarTable);
	}

	// Get Cpt line and crate missing lines
	public ArrayList<ArrayList<String>> EditCptPrntLine(String cpt , String var){

		//SetEvidenceVars();
		String val  = "@";

		for(String s: f.evidenceVars){
			if(s.contains(var)){
				val = s.substring(1,s.length());
			}
		}
		row = new ArrayList<>();
		ArrayList<String> prntarr = new ArrayList<>();
		ArrayList<ArrayList<String>> Cptrow = new ArrayList<ArrayList<String>>();


		int prntsSize = r.Tree.get(var).parents.size();
		if(r.Tree.get(var).parents.contains("none"))prntsSize = 0;
		String sp[];
		double sum = 1 , hold = 0;
		//System.out.println(val);
		int  count = 0;
		sp = cpt.split(",");
		if(cpt.contains(val)){
			//System.out.println("h");
			for (int i = 0; i < sp.length; i++) {
				row.add(sp[i]);
			}
			
			Cptrow.add(row);
			row = new ArrayList<>();
		}
		else{
			for (int i = 0; i < prntsSize; i++) {
				prntarr.add(sp[i]);
			}
			for(int j = prntsSize, k= 2 ; j < sp.length ; j++ , k++){
				row.add(sp[j]);
				if(k % 2 == 1 ){
					hold  = hold + Double.valueOf(sp[j]);
					//System.out.println("hold" + hold);
					Cptrow.add(AtachedArrays(prntarr, row));
					row = new ArrayList<>();
					count++;
				}
			}
			DecimalFormat df=new DecimalFormat("#.#####");
			df.setRoundingMode(RoundingMode.HALF_EVEN);
			String tmp = "=" + r.Tree.get(var).values.get(count);
			row.add(tmp);
			row.add(df.format(1 - hold));

			Cptrow.add(AtachedArrays(prntarr, row));

		}
		
		return Cptrow;
	}
	public void EditVarPrntTable(String var,String Parent){
		for(String cpt: r.Tree.get(var).CPT){
			//			System.out.println("var: " + var + "   Parent: " + Parent);
			r.Tree.get(var).VarPrntTables.add(EditCptPrntLine(cpt, var));
		}
		System.out.println(var+" : "+r.Tree.get(var).VarPrntTables);
	}


	public void CreateTables(){
		//	EditVarTable("B");
		SetEvidenceVars();
		for(String s: r.Variables){
			EditVarTable(s);
		}
		for(String h: f.Hiddens){
			for(String v: r.Variables){

				if(r.Tree.get(v).parents.contains(h)){
					//System.out.println(v);
					EditVarPrntTable(v,h);

				}
				else{

				}
			}
		}
	}
	public void JoinFactors(){

		f.EditQuery("P(B=true|J=true,M=true),1");
		Collections.sort(f.Hiddens);
		//System.out.println(r.Tree.get("J").CPT);
		RemoveIrrelevants();
		for(String h: f.Hiddens){
			for(String v: r.Variables){
				if(r.Tree.get(v).parents.contains(h)){
					if(r.Tree.get(v).parents.size() > 1){

					}
					else{

					}
				}
				else{

				}
			}
		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		read r = new read();

		r.ReadFile(r.FileToString());
		Functions F = new Functions(r);
		Algo2 A = new Algo2(r, F);
		//A.EditQuery(r.Queries.get(0));
		A.JoinFactors();
		A.CreateTables();
		//		for(String v: r.Variables){
		//			System.out.println(v+" "+r.Tree.get(v).VarTable);
		//			System.out.println();
		//		}
		//A.EditCPT(r.Tree.get("J").CPT , "J");
		System.out.println();
	}

}
