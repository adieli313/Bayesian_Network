import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.Random;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.sound.sampled.Line;

import org.omg.Messaging.SyncScopeHelper;

public class Functions extends Var{

	read r;

	ArrayList<String> queryParts = new ArrayList<String>();
	ArrayList<String> Hiddens =new ArrayList<String>();


	ArrayList<String> queryVars = new ArrayList<String>();
	ArrayList<String> NotqueryVars = new ArrayList<String>();
	ArrayList<String> evidenceVars = new ArrayList<String>();
	ArrayList<String> PrintSel = new ArrayList<String>();
	ArrayList<String> queryIn = new ArrayList<String>();

	ArrayList<ArrayList<String>> Linecalc = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> Options = new ArrayList<ArrayList<String>>();
	public Functions(){}
	public Functions(read v){
		this.r = v;
	}
	public int numOfLines(){
		int numLines = 1;
		for(String s:NotqueryVars){
			numLines *= r.Tree.get(s).values.size();
		}
		return numLines;
	}

	public void tmp(ArrayList<String> arl){
		createoptions();
		ConvertStr(queryIn);
	}

	public  void EditQuery(String query){
		ArrayList<String> tmp = new ArrayList<String>();
		if(query.contains("|"))
		{
			query=query.replace("P", "").replace("(", "").replace(")", "").replaceAll("\\|", "@");
			//////System.out.println(query);

			String []sp=query.split("@");
			String firstpart=sp[0];
			String secondpart=sp[1];
			queryParts.add(firstpart);
			queryParts.add(secondpart);
			String letter=sp[0].substring(0,1);
			String []ps = query.split(",");
			//NotqueryVars.add(letter);


			for(int i = 0; i<secondpart.length() ; i++){
				if(secondpart.charAt(i) >= 65 && secondpart.charAt(i) <=90){
					String temp =""+secondpart.charAt(i);
					//////////System.out.println(temp);
					queryVars.add(temp);
				}
			}
			for(String s: r.Variables){
				if(!queryVars.contains(s)){
					if(!s.contains(letter)){
						Hiddens.add(s);
					}
					NotqueryVars.add(s);
					for(int i = 0 ; i < r.Tree.get(s).values.size() ; i++){
						tmp.add(r.Tree.get(s).values.get(i));
					}
					Linecalc.add(tmp);
					tmp = new ArrayList<String>();
					//////////System.out.println("Nqr.size = " + NotqueryVars.size() +" , " + "Linecalc.size =" + Linecalc.size());
					//					////////System.out.println(NotqueryVars.size());
					//					////////System.out.println(NotqueryVars.get(count++));
				}
				else{
					//System.out.println(tmp);
					tmp.add(query.substring(query.indexOf(s) , query.indexOf(",", query.indexOf(s))));
					Linecalc.add(tmp);
					tmp = new ArrayList<String>();
					////////System.out.println(Linecalc.toString());
				}
			}

		}


	}
	public void line(ArrayList<String> arr){
		String b ="";
		int i =0;
		//////System.out.println(arr.get(0));
		for(String z: r.Variables){
			if(r.Tree.get(z).parents.get(0).contains("none"))
			{
				b=b+"*["+ arr.get(i) +"]";
			}
			else 
			{
				String e="["+arr.get(i)+"|";
				for(String w:r.Tree.get(z).parents)
				{
					e=e+arr.get(r.Variables.indexOf(w))+",";
				}
				e=e+"]";
				b=b+"*"+e;

			}
			i++;
		}
		b=b.substring(1,b.length()).replace(",]", "]");
		//System.out.println(b);
		PrintSel.add(b);

	}

	public void createoptions()
	{

		if(NotqueryVars.size()==0) {return;}	
		Options=new ArrayList<ArrayList<String>>();
		int line=numOfLines();
		for(int i =0 ; i<line ;)
		{
			ArrayList<String> option= new ArrayList<String>();
			option = RandomLine();
			if(!Options.contains(option))
			{
				Options.add(option);
				line(option);
				i++;
				//if(option.get(i).)
			}
		}
		//    	////System.out.println("----------------------------------------------------");
		//    	////System.out.println();
		//    	////System.out.println();
		//////System.out.println(Options);
	}

	public ArrayList<String> RandomLine(){
		ArrayList<String> Rndres = new ArrayList<String>();
		String result="";
		int i =0;
		for(String v:r.Variables)
		{
			int rnd=Linecalc.get(i).size();
			String op=r.Tree.get(v).values.get(new Random().nextInt(rnd));

			result=v+"="+op;
			//////System.out.println(result);
			Rndres.add(result);
			i++;
		}
		//////System.out.println();
		//////System.out.println(result);
		//result=result.substring(1,result.length());
		return Rndres;
	}

	public void ConvertStr(ArrayList<String> sel){
		//System.out.println(sel);
		int line = numOfLines();
		DecimalFormat df=new DecimalFormat("#.#####");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		double LineSumQ = 1;
		double LineSumNQ =1;
		double SUM =0;
		double SUMQ =0;
		double SUMNQ =0;
		sel = PrintSel;
		String sp[] = sel.get(0).split("\\]\\*\\[");
		int idx=0;
		double qs =0;
		//////System.out.println(sel.get(0));
		for (int i = 0; i < sel.size(); i++) {

			//System.out.println(sel.get(i));
			if(sel.get(i).contains(queryParts.get(0))){

				sp =sel.get(i).split("\\]\\*\\[");
				sp[0] = sp[0].substring(1, sp[0].length());
				sp[sp.length-1] = sp[sp.length-1].substring(0, sp[sp.length-1].length()-1); 
				for (int j = 0; j < sp.length; j++) {
					////System.out.println(LineSum);


					LineSumQ = LineSumQ * PartCalc(sp[j]);
					//System.out.println(" = :" + PartCalc(sp[j]));
					//System.out.println(queryIn);

				}
				SUMQ = SUMQ + LineSumQ;
			}
			else{
				sp =sel.get(i).split("\\]\\*\\[");
				sp[0] = sp[0].substring(1, sp[0].length());
				sp[sp.length-1] = sp[sp.length-1].substring(0, sp[sp.length-1].length()-1); 
				for (int j = 0; j < sp.length; j++) {
					////System.out.println(LineSum);


					LineSumNQ = LineSumNQ * PartCalc(sp[j]);
					//System.out.println(" = :" + PartCalc(sp[j]));
					//System.out.println(queryIn);

				}
				SUMNQ = SUMNQ + LineSumNQ;
			}

			////System.out.println();
			////System.out.println("------LineSum =  "+df.format(LineSum));

			////System.out.println();
			////System.out.println();
			LineSumQ =1.00;
			LineSumNQ =1.00;
		}
		SUM =SUMQ + SUMNQ;
		System.out.println("Resault:  " + df.format(SUMQ/SUM) + "," + (line-1) + "," + (line*(r.Variables.size()-1)));
	}

	public Double PartCalc(String s){
		//System.out.println(s);
		String cptval ="";
		Var lt = r.Tree.get(s.substring(0,1));
		double sum =1;
		double hold = 0;
		if(s.contains("|")){
			//////System.out.println(" ******* Has PArents ******");
			int remCpt = -1;
			int remVal = 0;
			String sp[];
			String crnt = s.substring(1, s.indexOf("|"));
			String prnts = s.substring(s.indexOf("|")+1 , s.length());
			//////System.out.println("crnt : " + crnt + " ,  prnts : " + prnts);
			for (int i = 0; i < lt.CPT.size() ; i++) {
				if(lt.CPT.get(i).contains(prnts)){
					//					////System.out.println(lt.CPT.get(i));
					remCpt = i;
					break;
				}
			}
			if(remCpt < 0){
				System.out.println("There is no such CPT - FAILED!!!");
				System.exit(0);
			}
			//////System.out.println("remCpt :" +remCpt);
			String cptline = lt.CPT.get(remCpt);
			sp = cptline.split(",");
			//////System.out.println("cptline : "+ cptline);
			for (int i = 0; i < sp.length; i++) {
				if(sp[i].compareTo(crnt) == 0){
					remVal = i;
					break;
				}
			}
			if(remVal > 0){
				sum = sum* Double.valueOf(sp[remVal+1]);
			}

			else {
				for (int i = lt.parents.size()+1; i < sp.length; i+=2) {
					//					////System.out.println("sp["+i+"] :   "+ sp[i]);
					hold = hold+ Double.valueOf(sp[i]);
					//////System.out.println("hold = " +hold);
				}
				sum = sum* (1 - hold);
			}
		}
		else{
			//////System.out.println(" ******* No PArents ******");

			String sp[] = lt.CPT.get(0).split(",");
			cptval = lt.CPT.get(0);
			//////System.out.println(cptval);
			int rem = 1;
			for (int i = 0; i < sp.length; i++) {
				//////System.out.println("sp["+i+"]  = "+sp[i]);
				if(sp[i].contains(s.substring(1,s.length()))){
					rem = i;
					break;
				}
			}
			//////System.out.println(rem);
			if(cptval.contains(s.substring(1,s.length()))){
				sum = sum* Double.parseDouble(sp[rem+1]);
			}
			else{
				for (int i = 2; i < sp.length; i+=2) {
					hold = hold+ Double.parseDouble(sp[i]);
					//////System.out.println("hold = " +hold);
				}
				sum = sum* (1 - hold);
			}

		}
		////System.out.println("sum =" +sum);
		return sum;
	}

	public void reset(){
		queryParts = new ArrayList<String>();
		queryVars = new ArrayList<String>();
		NotqueryVars = new ArrayList<String>();
		PrintSel = new ArrayList<String>();
		queryIn = new ArrayList<String>();
		Linecalc = new ArrayList<ArrayList<String>>();
		Options = new ArrayList<ArrayList<String>>();
		Hiddens =new ArrayList<String>();

	}

	public static void main(String[]args) throws IOException{
		read r = new read();
		r.ReadFile(r.FileToString());

		Functions f = new Functions(r);
		////System.out.println(f.r.Queries.get(0));
		f.EditQuery(f.r.Queries.get(0));
		//		ArrayList<String> arr = new ArrayList<>();
		//		arr.add("true");arr.add("true");arr.add("false");arr.add("true");arr.add("false");
		f.createoptions();
		////System.out.println();
		////System.out.println();
		////System.out.println("                   ***********");
		////System.out.println();

		f.ConvertStr(f.PrintSel);
		//////System.out.println(f.PartCalc(""));
		//f.RandomLine();
		f.EditQuery("");
	}



}
