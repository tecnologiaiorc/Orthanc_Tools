package org.petctviewer.orthanc.anonymize.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.petctviewer.orthanc.anonymize.VueAnon;

public class Controller_Anonymize_Btn implements ActionListener {
	
	private VueAnon vue;
	
	public Controller_Anonymize_Btn(VueAnon vue) {
		this.vue=vue;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}


//public void actionPerformed(ActionEvent arg0) {
	/*
	anonCount = 0;
	
	SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>(){
		int dialogResult=JOptionPane.YES_OPTION;
		@Override
		protected Void doInBackground() {

			//Disable the anons button during anonymization
			enableAnonButton(false);

			anonBtn.setText("Anonymizing");
			
			Choice bodyCharChoice=Choice.CLEAR,
					datesChoice=Choice.CLEAR,
					bdChoice=Choice.CLEAR,
					ptChoice=Choice.CLEAR,
					scChoice=Choice.CLEAR,
					descChoice =Choice.CLEAR;
			
			// Change Choice to Keep if position 0 is selected
			if(settingsBodyCharButtons[0].isSelected()) bodyCharChoice = Choice.KEEP;
			if(settingDatesButtons[0].isSelected()) datesChoice = Choice.KEEP;
			if(settingsBirthDateButtons[0].isSelected()) bdChoice = Choice.KEEP;
			if(settingsPrivateTagButtons[0].isSelected()) ptChoice = Choice.KEEP;
			if(settingsSecondaryCaptureButtons[0].isSelected()) scChoice = Choice.KEEP;
			if(settingsStudySerieDescriptionButtons[0].isSelected()) descChoice = Choice.KEEP;			
		

			int i = 0;
			int j = 0;
			try {
				
				if(anonProfiles.getSelectedItem().equals("Full clearing")){
					if(modeleAnonStudies.getModalities().contains("NM") || 
							modeleAnonStudies.getModalities().contains("PT")){
						dialogResult = JOptionPane.showConfirmDialog (gui, 
								"Full clearing is not recommended for NM or PT modalities."
										+ "Are you sure you want to anonymize ?",
										"Warning anonymizing PT/NM",
										JOptionPane.WARNING_MESSAGE,
										JOptionPane.YES_NO_OPTION);
					}
				}
				if(modeleAnonStudies.getModalities().contains("US")){
					JOptionPane.showMessageDialog (gui, 
							"DICOM files with the US modality may have hard printed informations, "
									+ "you may want to check your files.",
									"Warning anonymizing US",
									JOptionPane.WARNING_MESSAGE);
				}
				
				// Checking if several anonymized patients have the same ID or not
				boolean similarIDs = false;
				ArrayList<String> newIDs = new ArrayList<String>();
				for(int n = 0; n < anonPatientTable.getRowCount(); n++){
					String newID = modeleAnonPatients.getPatient(anonPatientTable.convertRowIndexToModel(n)).getNewID();
					if(newID != "" && !newIDs.contains(newID)){
						newIDs.add(newID);
					}else if(newIDs.contains(newID)){
						similarIDs = true;
					}
				}
				if(similarIDs){
					dialogResult = JOptionPane.showConfirmDialog (gui, 
							"You have defined 2 or more identical IDs for anonymized patients, which is not recommended."
									+ " Are you sure you want to anonymize ?",
									"Warning similar IDs",
									JOptionPane.WARNING_MESSAGE,
									JOptionPane.YES_NO_OPTION);
				}
				
				if(dialogResult == JOptionPane.YES_OPTION){

					String substituteName = "A-" + jprefer.get("centerCode", "12345");

					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
					String substituteID = "A-" + df.format(new Date());

					for(String patientID : modeleAnonStudies.getPatientIDs()){
						String newName = modeleAnonPatients.getPatient(anonPatientTable.convertRowIndexToModel(j)).getNewName();
						String newID = modeleAnonPatients.getPatient(anonPatientTable.convertRowIndexToModel(j)).getNewID();
						String newStudyID = "";
						if((newName == null || newName.equals("")) || (newID == null || newID.equals(""))){
							anonCount++;
						}
						if(newName == null || newName.equals("")){
							newName = substituteName + "^" + anonCount;
							modeleAnonPatients.setValueAt(newName, anonPatientTable.convertRowIndexToModel(j), 3);
						}

						if(newID == null || newID.equals("")){
							newID = substituteID + "^" + anonCount;
							modeleAnonPatients.setValueAt(newID, anonPatientTable.convertRowIndexToModel(j), 4);
						}

						for(String uid : modeleAnonStudies.getOldOrthancUIDsWithID(patientID)){
							String newDesc = modeleAnonStudies.getNewDesc(uid);
							AnonRequest quAnon;
							quAnon = new AnonRequest(connexionHttp, bodyCharChoice, datesChoice, bdChoice, ptChoice, scChoice, descChoice, newName, newID, newDesc);
							state.setText("<html>Anonymization state - " + (i+1) + "/" + modeleAnonStudies.getStudies().size() + 
									" <font color='red'> <br>(Do not use the toolbox while the current operation is not done)</font></html>");
							quAnon.sendQuery(uid);
							modeleAnonStudies.addNewUid(quAnon.getNewUID());
							i++;
							newStudyID = quAnon.getNewUID();
							//Add anonymized study in export list
							modeleExportStudies.addStudy(newStudyID);
						}

						j++;
					}
					
					if(settingsSecondaryCaptureButtons[1].isSelected()){
						modeleAnonStudies.removeScAndSr();
					}
					
					//Empty list
					modeleAnonStudies.empty();
					modeleAnonPatients.clear();
					
					
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void done(){
			
			//Re-enable anon button
			enableAnonButton(true);
			anonBtn.setText("Anonymize");
			if(dialogResult == JOptionPane.YES_OPTION){
				state.setText("<html><font color='green'>The data has successfully been anonymized.</font></html>");
				openCloseAnonTool(false);
				pack();
				tabbedPane.setSelectedIndex(1);
				modeleAnonPatients.clear();
				modeleAnonStudies.empty();
				
			}
			if(tableauExportStudies.getRowCount() > 0){
				tableauExportStudies.setRowSelectionInterval(tableauExportStudies.getRowCount() - 1, tableauExportStudies.getRowCount() - 1);
			}
			modeleExportSeries.clear();
			try {
				if(modeleExportStudies.getRowCount() > 0){
					String studyID = (String)tableauExportStudies.getValueAt(tableauExportStudies.getSelectedRow(), 5);
					modeleExportSeries.addSerie(studyID);
					tableauExportSeries.setRowSelectionInterval(0,0);
				}
			} catch (Exception e1) {
				// IGNORE
			}
			//Si fonction a ete fait avec le CTP on fait l'envoi auto A l'issue de l'anon
			if(autoSendCTP) {
				exportCTP.doClick();
				autoSendCTP=false;
			}
			if(anonymizeListener!=null) {
				anonymizeListener.AnonymizationDone();
			}
		}
	};
	if(!modeleAnonStudies.getOldOrthancUIDs().isEmpty()){
			worker.execute();
	}
	*/
//}
