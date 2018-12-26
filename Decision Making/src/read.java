import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;



public class read {
	public HashMap<String, Var> Tree;
	public ArrayList<String> Variables;
	public ArrayList<String> Queries;
	public String file;

	public read()
	{
		Tree = new HashMap<String, Var>();
		Variables=new ArrayList<String>();
		Queries=new ArrayList<String>();
		file="";
	}
	public static void main(String[] args) throws IOException {
//		read r=new read();
//		r.ReadFile(r.FileToString());
//		Functions f = new Functions(r);
//		f.QueryParts("P(B=true|J=true,M=true)");
	}

	//-------Read-------//
	public String FileToString() throws IOException
	{
		String FileText="";
		
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\adiel\\workspace\\Decision Making\\src\\input.txt"));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();



		while (line != null) {

			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		FileText = sb.toString();
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		//System.out.println(FileText);
		return FileText ;
	}

	public void ReadFile(String FileText)
	{
		Scanner scanner = new Scanner(FileText);
		Var Current=new Var();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(line.contains("Variables"))
			{
				Variables(line);
			}
			if(line.contains("Var")&& !line.contains("Variables"))
			{
				Current=new Var();

				Tree.put(var(line), Current);
				//System.out.println(Tree.size());
			}
			if(line.contains("Values"))
			{
				for(String f:values(line))
				{
					Current.values.add(f);
				}
			}

			if(line.contains("Parents"))
			{
				for(String f:parents(line))
				{
					Current.parents.add(f);
				}
			}

			if(line.contains("=")&&!line.contains("P("))
			{
				cpt(line, Current);
			}
			if(line.contains("P("))
			{
				Queries.add(line);
			}
		}
		scanner.close();
	}

	//--------Cases--------//
	public  String var(String str)
	{
		String[]split=str.split(" ");
		return split[1];
	}
	public String[] values(String str)
	{   

		str=str.replaceAll("Values: ", "");
		str =str.replaceAll(",", "");
		String[]split=str.split(" ");
		return split;
	}
	public String[] parents(String s)
	{   
		s=s.replaceAll("Parents: ", "");
		String[]split=s.split(",");
		return split;
	}
	public void cpt(String s,Var v)
	{   
		int counter=0;
		String []split=s.split(",");
		String result=s.substring(s.indexOf("="), s.length());
		//System.out.println(result);
		String main="";
		//System.out.println(Tree.size());
		//System.out.println(v.parents.size());
		if(!v.parents.contains("none"))
		{
			for(String g:v.parents)
			{

				main=main+g+"="+split[counter++]+",";
			}
		}
		
		main=main+result;
		v.CPT.add(main);
		//System.out.println(v.CPT);
	}
	public  void Variables(String s)
	{
		s=s.replaceAll("Variables: ", "");
		String []split =s.split(",");
		for(String b:split)
		{
			Variables.add(b);
			
		}
	} 
}


