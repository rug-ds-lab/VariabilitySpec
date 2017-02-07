package nl.rug.ds.bpm.variability.specification.tool;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.rug.ds.bpm.variability.specification.VariabilitySpecification;

public class VariabilitySpecGen {
	
	public static void main(String[] args) {
		VariabilitySpecGen gen = new VariabilitySpecGen();
		gen.getSpecification();
		
		System.exit(0);
	}
	
	private void getSpecification() {
		FileDialog loadDialog = new FileDialog(new Frame(), "Select Petri Net files to include in the specification" , FileDialog.LOAD);
		loadDialog.setMultipleMode(true);
		loadDialog.setFile("*.pnml");
	    loadDialog.setVisible(true);
	    
	    List<String> filenames = getFileList(loadDialog.getFiles());
	    loadDialog.dispose();
	    
		FileDialog outputDialog = new FileDialog(new Frame(), "Select outputfile" , FileDialog.LOAD);
		outputDialog.setFile("*.xml");
		outputDialog.setMultipleMode(false);
		outputDialog.setVisible(true);
		
		String outputfile = "speciication.xml";
		if(outputDialog.getFile() != null)
			outputfile = outputDialog.getDirectory() + outputDialog.getFile();
		
		outputDialog.dispose();
		
		VariabilitySpecification vs = new VariabilitySpecification(filenames, "silent");
		
		write2File(outputfile, XMLSpecifications(vs));
	}
	
	private String getOverview(VariabilitySpecification vs) {
		String overview = "";
		overview += bar();
		overview += "Viresp\n";
		overview += bar();
		overview += toList(vs.getViresp());
		
		overview += bar();
		overview += "Viprec\n";
		overview += bar();
		overview += toList(vs.getViprec());
		
		overview += bar();
		overview += "Veiresp\n";
		overview += bar();
		overview += toList(vs.getVeiresp());
		
		overview += bar();
		overview += "Veresp reduced\n";
		overview += bar();
		overview += toList(vs.getVerespReduced(false));
		
		overview += bar();
		overview += "Vconf\n";
		overview += bar();
		overview += toList(vs.getVconf());
		
		overview += bar();
		overview += "Vpar\n";
		overview += bar();
		overview += toList(vs.getVpar());
		
		return overview;
	}
	public String XMLSpecifications(VariabilitySpecification vs) {
		StringBuilder sb = new StringBuilder();
		sb.append("<variabilitySpecification>\n");
		
		sb.append(XMLSet(vs.getViresp(), "always immediate response"));
		sb.append(XMLSet(vs.getViprec(), "always immediate precedence"));
		sb.append(XMLSet(vs.getVeiresp(), "exists immediate response"));
		sb.append(XMLSet(vs.getVerespReduced(false), "exists eventual response (reduced)"));
		sb.append(XMLSet(vs.getVconf(), "conflict"));
		sb.append(XMLSet(vs.getVpar(), "parallel"));
			
		sb.append("</variabilitySpecification>");
		
		return sb.toString();
	}
	
	private String XMLSet(Collection<String> set, String type) {
		StringBuilder sb = new StringBuilder();
		
		for(String spec: set)
			sb.append("\t<specification language=\"CTL\" type=\"" + type+ "\">" + spec + "</specification>\n");
		
		return sb.toString();
	}

	private List<String> getFileList(File[] files) {
		List<String> fList = new ArrayList<String>();
		
		for (File f: files) {
			fList.add(f.getAbsolutePath()); // + "/" + f.getName());
		}
		
		return fList;
	}
	
	private String toList(Collection<String> list) {
		String str = "";
		for (String s: list) {
			str += s + "\n";
		}
		return str;
	}

	private String bar() {
		return "=====================================================================================\n";
	}
	
	private static void write2File(String filename, String filecontent) {
		try {
			File newfile = new File(filename);
				
	        FileWriter fileWriter = new FileWriter(newfile);
	        fileWriter.write(filecontent);
	    	fileWriter.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
