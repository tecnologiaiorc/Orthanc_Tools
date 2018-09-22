package org.petctviewer.orthanc.ctpimport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.petctviewer.orthanc.anonymize.VueAnon;
import org.petctviewer.orthanc.importdicom.ImportDCM;

public class CTP_Import_GUI extends VueAnon implements ImportListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImportDCM importFrame;
	CTP_Import_GUI importGUI=this;
	
	public CTP_Import_GUI() {
		super("OrthancCTP.json");
		//Make a simplified version of Orthanc Tools
		tablesPanel.setVisible(false);
		topPanel.setVisible(false);
		anonBtnPanelTop.setVisible(false);
		importCTP.setVisible(true);
		importCTP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				importFrame=new ImportDCM();
				importFrame.setImportListener(importGUI);
				importFrame.pack();
				importFrame.setLocationRelativeTo(importGUI);
				importFrame.setVisible(true);
				
			}
			
		});
		anonBtn.setText("Send");
		openCloseAnonTool(true);
		tabbedPane.remove(2);
		listePeersCTP.setSelectedIndex(1);
		this.revalidate();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//Run the import app
		importFrame=new ImportDCM();
		importFrame.setImportListener(this);
		importFrame.pack();
		importFrame.setLocationRelativeTo(this);
		importFrame.setVisible(true);
		
	}

	public static void main(String[] args) {
		new CTP_Import_GUI();

	}

	@Override
	public void ImportFinished(HashMap<String, HashMap<String, String>> importedStudy) {
		HashMap<String, HashMap<String, String>> importedstudy=importFrame.getImportedStudy();
		Set<String> keys=importedstudy.keySet();
		String[] keysArray=new String[keys.size()];
		keys.toArray(keysArray);
		
		for (int i=0; i<keysArray.length; i++) {
			String patientName=importedstudy.get(keysArray[i]).get("patientName");
			String patientID=importedstudy.get(keysArray[i]).get("patientID");
			String patientDOBString=importedstudy.get(keysArray[i]).get("patientDOB");
			String patientSex=importedstudy.get(keysArray[i]).get("patientSex");
			DateFormat df=new SimpleDateFormat("YYYYMMDD");
			Date patientDOB=null;
			try {
				patientDOB = df.parse(patientDOBString);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			ArrayList<String> studyID=new ArrayList<String>();
			studyID.add(keysArray[i]);

			
			try {
				modeleAnonPatients.clear();
				modeleAnonPatients.addPatient(connexionHttp,patientName, patientID, patientDOB, patientSex, studyID);
				modeleAnonStudies.clear();
				modeleAnonStudies.addStudies(patientName, patientID, studyID);
			} catch (IOException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		importFrame.dispose();
		
	}

}
