package com.din.comp.compare;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * Author RAD9KOR_24_oct_2016
 * required lib
 * jsoup-1.10.1.jar
 * */
public class BOMCompareWithShipment  {
/*	public static void main(String[] args) throws IOException {
		*//**
		 * update 3 paths
		 * 2 input path and 
		 * 1 output path
		 *//*
		//comment it in testing mode Remember 
		Scanner scan = new Scanner(System.in);
		System.out.println("Drag and drop old BOM compliance html report:");
		String firstinput = scan.next();
		
		System.out.println("Drag and drop new BOM compliance html report:");
		String secondInput = scan.next();
		System.out.println("Drag and drop Output Folder:");
		String outputPath = scan.next();
		
		
		String firstinput = FileCellRenderer.files[0];
		String secondInput = FileCellRenderer.files[1];
		String outputPath = FileCellRenderer.files[2];;
		
		

		// Uncomment only for testing locally
		String firstinput = "D:/PT/Projects/current/1/NET/delta1/old_doc/RBEI_OSS-Compliance_Report_ST-CO_IRIS-Net_Delta.html";
		String secondInput = "D:/PT/Projects/current/1/NET/delta1/doc/RBEI_OSS-Compliance_Report_ST-CO_IRIS-Net_Delta1.html";
		String outputPath = "D:/PT/Projects/current/1/NET/delta1/doc/";

		perform(firstinput, secondInput, outputPath);
		//scan.close();
	}*/

	public static void perform(String firstinput, String secondInput) throws FileNotFoundException, IOException {
		
		
		System.out.println(firstinput);
		System.out.println(secondInput);
		//System.out.println(outputPath);
		
		String oldBOMFilePath = firstinput ;
		String newBOMFilePath = secondInput;
		String outputFilePath = pathComponent(secondInput)+"\\Output.txt";
		
		
		if(!oldBOMFilePath.endsWith(".html") || !newBOMFilePath.endsWith(".html") || (!oldBOMFilePath.endsWith(".html") && !newBOMFilePath.endsWith(".html")) ){
			JOptionPane.showMessageDialog (null, "Drag and drop 2 bom reports to be compared", "Info", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		System.out.println("Check the Output.txt for result");
		System.setOut(new PrintStream(new FileOutputStream(outputFilePath)));
		File oldBOMFile = new File(oldBOMFilePath);
		File newBOMFile = new File(newBOMFilePath);
		


		Collection<String> listOne = new ArrayList<String>();
		Collection<String> listOnev = new ArrayList<String>();
		Collection<String> listOnes = new ArrayList<String>();
		Collection<String> listTwo = new ArrayList<String>();
		Collection<String> listTwov = new ArrayList<String>();
		Collection<String> listTwos = new ArrayList<String>();

		ArrayList<String> finallistold = new ArrayList<String>();
		ArrayList<String> finallistnew = new ArrayList<String>();


		org.jsoup.nodes.Document document =  Jsoup.parse(oldBOMFile, "UTF-8", "");
		//check external id
		Elements elements;
		String text = null;
		elements = document.select(".bomTable th:eq(7)");
		if (elements.size() == 0) {
			text = "NA";
		} else {
			//				 bomversiontext = elements.get(1).text();
			
			text = elements.get(0).text();
			//System.out.println("7th position element"+text);
		}
		//check external id


		Elements nextTurns = document.select(".bomTable td:eq(2)");   
		/*String Titleold = document.select(".labeledTable td:eq(0)").first().text();
	System.out.println(Titleold);*/
		//System.out.println("================BOM1======================");
		System.out.println("===========================Total================================");
		for (Element nextTurn : nextTurns) {
			//	System.out.println(nextTurn.text());
			listOne.add(nextTurn.text());                           //list1
		}
		System.out.printf("Total components in old BOM: %s (Including 1 title)%n",listOne.size());

		ArrayList<String> newlistOne = new ArrayList<String>(listOne);

		String[] old = newlistOne.toArray(new String[newlistOne.size()]);

		//=============
		Elements nextTurnsv = document.select(".bomTable td:eq(3)");
		//System.out.println("================Version1======================");
		for (Element nextTurn : nextTurnsv) {
			//	System.out.println(nextTurn.text());
			listOnev.add(nextTurn.text());                           //list1v
		}
		//System.out.println("TOTAL OLD BOM versions:"+listOnev.size());
		ArrayList<String> newlistOnev = new ArrayList<String>(listOnev);
		String[] oldv = newlistOnev.toArray(new String[newlistOnev.size()]);
		Elements nextshipstatus ;

		//play
		if (text.equalsIgnoreCase("External IDs")) {
			nextshipstatus = document.select(".bomTable td:eq(9)");
		}else{
			System.out.println("no external id present");
			nextshipstatus = document.select(".bomTable td:eq(8)");
		}

		//System.out.println("================Ship status old======================");
		for (Element nextTurn : nextshipstatus) {
			//	System.out.println(nextTurn.text());
			listOnes.add(nextTurn.text());                           //list1v
		}
		//System.out.println("TOTAL OLD BOM shipstatus:"+listOnes.size());
		ArrayList<String> newlistOnes = new ArrayList<String>(listOnes);
		String[] olds = newlistOnes.toArray(new String[newlistOnes.size()]);
		//play


		if ( old.length ==  oldv.length) {
			for (int i = 0; i < old.length; i++) {
				old[i] = old[i]+" ="+oldv[i]+" ="+olds[i];//<---------- computation
			}
			List<String> ewlistOne = (List<String>) Arrays.asList(old) ;
			/*Titleold = Titleold+"_Unspecified";
		ewlistOne.remove(Titleold);*/
			finallistold.addAll(ewlistOne);                           //list 1 final
		} else {
			System.out.println("size mismatch");
		}

		//=========================================================file2 ==================================================
		org.jsoup.nodes.Document document1 =  Jsoup.parse(newBOMFile, "UTF-8", "");

		//check external id
		Elements elements2;
		String text1 = null;
		elements2 = document1.select(".bomTable th:eq(7)");
		if (elements2.size() == 0) {
			text1 = "NA";
		} else {
			//				 bomversiontext = elements.get(1).text();
			text1 = elements2.get(0).text();
		}
		//check external id



		Elements nextTurns1 = document1.select(".bomTable td:eq(2)");
		String Titlenew = document1.select(".labeledTable td:eq(0)").text();
		//System.out.println("================BOM2======================");
		for (Element nextTurn1 : nextTurns1) {
			//System.out.println(nextTurn1.text());
			listTwo.add(nextTurn1.text());                            //list2
		}
		
		
		System.out.printf("Total components in new BOM: %s (Including 1 title)%n",listTwo.size());
		System.out.println("NOTE: For correct output always drag & drop old html first");
		System.out.println("");
		//System.out.println("================================================================");
		//System.out.printf("");
		ArrayList<String> newlistTwo = new ArrayList<String>(listTwo);
		String[] old1 =  newlistTwo.toArray(new String[newlistTwo.size()]);


		Elements nextTurnsv1 = document1.select(".bomTable td:eq(3)");
		//System.out.println("================VERSION2======================");
		for (Element nextTurn : nextTurnsv1) {
			//	System.out.println(nextTurn.text());
			listTwov.add(nextTurn.text());                           //list2v
		}
		//System.out.println("TOTAL New BOM versions:"+listTwov.size());
		ArrayList<String> newlistTwov = new ArrayList<String>(listTwov);
		String[] old1v = (String[]) newlistTwov.toArray(new String[newlistTwov.size()]);

		Elements newshipstatus ;
		//play
		if (text1.equalsIgnoreCase("External IDs")) {
			 newshipstatus = document1.select(".bomTable td:eq(9)");
		}else{
			 newshipstatus = document1.select(".bomTable td:eq(8)");
		}
		
		//System.out.println("================Ship status new======================");
		for (Element nextTurn : newshipstatus) {
			//	System.out.println(nextTurn.text());
			listTwos.add(nextTurn.text());                           //list1v
		}
		//System.out.println("TOTAL OLD BOM shipstatus:"+listOnes.size());
		ArrayList<String> newlistTwos = new ArrayList<String>(listTwos);
		String[] olds1 = newlistTwos.toArray(new String[newlistTwos.size()]);
		//play


		if ( old1.length ==  old1v.length) {
			for (int i = 0; i < old1.length; i++) {
				old1[i] = old1[i]+" ="+old1v[i]+" ="+olds1[i];//<---------- computation
			}
			List<String> ewlistTwo = (List<String>) Arrays.asList(old1);
			/*Titlenew = Titlenew+"_Unspecified";
		ewlistTwo.remove(Titlenew);*/
			finallistnew.addAll(ewlistTwo);                           //list 2 final
		} else {
			System.out.println("size mismatch 2nd");
		}
		/**Author RAD9KOR_24_oct_2016*/

		//========================================================temp======================================================


		Collection<String> tempOLD = new ArrayList<String>(finallistold);
		Collection<String> tempNEW = new ArrayList<String>(finallistnew);

		//Collection<String> tempOLD1 = new ArrayList<String>(finallistold);
		Collection<String> tempcommoninnew = new ArrayList<String>(finallistnew);

		tempOLD.removeAll(finallistnew);   //removed
		tempNEW.removeAll(finallistold);   //newly added
		tempcommoninnew.retainAll(finallistold);   //retains
		//System.out.println(Arrays.toString(tempNEW.toArray()));
		//System.out.println(Arrays.toString(tempOLD.toArray()));
		
		int othersize =0;
		for (String string : tempcommoninnew) {
			if(string.contains("Used at development time")){
				othersize = othersize +1;
			}
			
		}
		for (String string : tempNEW) {
			if(string.contains("Used at development time")){
				othersize = othersize +1;
			}
			
		}
		//System.out.println("Total Other developments:"+othersize);
		
		
		
		
		//6/23/2017===================================same component but different version
		//System.out.println("========================testoldbom=================================");
		System.out.println("");
		String data[] = new String[3];
		HashMap<String, String> cv = new HashMap<String, String>();
		HashMap<String, String> cvs = new HashMap<String, String>();


		for (String string : finallistold) {
			string = string.replace(" ", "");
			data = string.split("="); 
			cv.put(data[0], data[1]);
			cvs.put(data[0]+data[1],data[2]);
			//System.out.printf("%s%n",string);
		}

		//System.out.println("========================testoldbom=================================");


		//System.out.println("========================testnewbom=================================");
		String data1[] = new String[3];
		HashMap<String, String> cv1= new HashMap<String, String>();
		HashMap<String, String> cvs1 = new HashMap<String, String>();


		for (String string : finallistnew) {
			string = string.replace(" ", "");
			data1 = string.split("="); 
			cv1.put(data1[0], data1[1]);
			cvs1.put(data1[0]+data1[1],data1[2]);
			//System.out.printf("%s%n",string);
		}

		
		System.out.println("===========Same components having different versions============");
		System.out.println("Common components having different versions in both BOMs");
		System.out.println("along with their shipment status");
		System.out.println("================================================================");

		//==========computation
		int t = 0;
		

		for (HashMap.Entry<String, String> entryold : cv.entrySet())
		{
			for (HashMap.Entry<String, String> entrnew : cv1.entrySet())
			{
				if(entryold.getKey().equals(entrnew.getKey()) && !(entryold.getValue().equals(entrnew.getValue()))){
					t=t+1;
					System.out.println(t+":");
					System.out.println("oldbom-> "+entryold.getKey()+" ,"+entryold.getValue()+" ,"+cvs.get(entryold.getKey()+entryold.getValue()));
					System.out.println("newbom-> "+entrnew.getKey()+" ,"+entrnew.getValue()+" ,"+cvs1.get(entrnew.getKey()+entrnew.getValue()));
				}
				if((!(entryold.getKey().equals(entrnew.getKey())) && (entryold.getKey().equalsIgnoreCase(entrnew.getKey()))) && !(entryold.getValue().equals(entrnew.getValue()))){
					t=t+1;
					System.out.println(t+":");
					System.out.println("oldbom-> "+entryold.getKey()+" ,"+entryold.getValue()+" ,"+cvs.get(entryold.getKey()+entryold.getValue()));
					System.out.println("newbom-> "+entrnew.getKey()+" ,"+entrnew.getValue()+" ,"+cvs1.get(entrnew.getKey()+entrnew.getValue()));

				}
			}
		}

		System.out.printf("%n%n%n");
		//==========computation


		//6/23/2017===================================same component but different version
		
		
		
		
		
		System.out.println("========================Newly Added=================================");
		System.out.printf("Newly Added: %s (Excluding title)  %nNewly Added: %s (including title name)%n",tempNEW.size()-1 , tempNEW.size());
		System.out.println("NOTE: Title is included");
		System.out.println("====================================================================");
		System.out.printf("(s.no, component_name ,component_version ,shipping_status):%n");
		int x = 0, y = 0, Z=0;
		for (String string : tempNEW) {
			System.out.printf("%s 		%s%n",++x, string.replaceAll("=",","));
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("=========================Removed================================");
		System.out.printf("Removed components: %s (Excluding title)%nRemoved components: %s (including title name)%n", tempOLD.size()-1, tempOLD.size());
		System.out.println("NOTE: Title is included");
		System.out.println("================================================================");
		System.out.printf("(s.no, component_name ,component_version ,shipping_status):%n");
		for (String string : tempOLD) {
			System.out.printf("%s 		%s%n",++y, string.replaceAll("=",","));
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("==========================Common================================");
		System.out.println("Common components having same versions in both BOMs");
		System.out.println("with same shipment status");
		//System.out.println("");
		System.out.printf("Total such components: %s %n",  tempcommoninnew.size());
		System.out.println("================================================================");
		System.out.printf("(s.no, component_name ,component_version ,shipping_status):%n");
		for (String string : tempcommoninnew) {
			System.out.printf("%s 		%s%n",++Z, string.replaceAll("=",","));
		}
		
		int total = tempcommoninnew.size() + (tempNEW.size()-1) - othersize;
		System.out.println();
		
		System.out.println("================================================================");
		System.out.println("Total Other developments(not shipped)    : "+othersize);
		System.out.println("Total bom in obligations report should be: "+total);
		System.out.println("================================================================");
		
		
		
		JOptionPane.showMessageDialog (null, "Check output in "+ outputFilePath, "Info", JOptionPane.INFORMATION_MESSAGE);
		
	}
	  public static String pathComponent(String filename) {
	      int i = filename.lastIndexOf(File.separator);
	      return (i > -1) ? filename.substring(0, i) : filename;
	  }
}
/**
 * Author RAD9KOR_24_oct_2016
 * 
 * */