package org.petctviewer.orthanc.monitoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.petctviewer.orthanc.ParametreConnexionHttp;
import org.petctviewer.orthanc.query.Rest;

public class Auto_Fetch {
	
	private ParametreConnexionHttp connexion;
	private String level, studyDate, modality, studyDescription, queryAet , retrieveAet;
	private JSONParser parser=new JSONParser();
	private Rest restApi;
	private JLabel status;
	
	//sert a etre arrete via methode
	private Timer timer;
	
	public Auto_Fetch(ParametreConnexionHttp connexion, String level, String studyDate, String modality, String studyDescription, String queryAet, JLabel status) {
		this.connexion=connexion;
		restApi=new Rest(connexion);
		this.level=level;
		this.studyDate=studyDate;
		this.studyDescription=studyDescription;
		this.queryAet=queryAet;
		this.modality=modality;
		try {
			Object[] localAET = restApi.getLocalAET();
			retrieveAet=localAET[0].toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.status=status;
		
	}
	
	public void startAutoFetch() {
		Orthanc_Monitoring monitoring=new Orthanc_Monitoring(connexion);
		monitoring.autoSetChangeLastLine();
		
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				System.out.println("starting Auto-Fetch");
				monitoring.makeMonitor();
				List<String> idToProcess=new ArrayList<String>();
				//Si on monitore le level Study on parcours les study arrivees pour stocker les ID patients a retrieve
				if (level.equals("study")) {
					for (int i=0; i<monitoring.newStudyID.size(); i++) {
						StringBuilder sb = connexion.makeGetConnectionAndStringBuilder("/studies/"+monitoring.newStudyID.get(i));
						try {
							JSONObject study = (JSONObject) parser.parse(sb.toString());
							JSONObject patientMainTag= (JSONObject) study.get("PatientMainDicomTags");
							idToProcess.add((String) patientMainTag.get("PatientID"));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
					}
				}
				//Si on monitore le level Patients on parcours les patients arrivees pour stocker les ID patients a retrieve
				if (level.equals("patient")) {
					for (int i=0; i<monitoring.newPatientID.size(); i++) {
						StringBuilder sb = connexion.makeGetConnectionAndStringBuilder("/patients/"+monitoring.newPatientID.get(i));
						try {
							JSONObject patient = (JSONObject) parser.parse(sb.toString());
							JSONObject patientMainTag= (JSONObject) patient.get("MainDicomTags");
							idToProcess.add((String) patientMainTag.get("PatientID"));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
					}
					
				}
				//Pour chaque ID patient on lance le retrieve
				for (int i=0 ; i<idToProcess.size(); i++) {
					makeRetrieve(idToProcess.get(i));
				}
				
				//On vide pour les messages suivant
				idToProcess.clear();
				monitoring.clearAllList();
				
			}
			
		};
		
        //running timer task as daemon thread
        timer = new Timer(true);
        //Toutes les 90 seconds
        timer.scheduleAtFixedRate(timerTask, 0, (90*1000));
	}
	
	/**
	 * Retrieve en local toutes les reponse d'un ID patient avec le filtre (date, modality, description)
	 * @param patientID
	 * @throws IOException
	 */
	private void makeRetrieve(String patientID) {
		
		String[] results=restApi.getQueryAnswerIndexes("Study", "*", patientID, studyDate, modality, studyDescription, "*", queryAet);
		int numberofAnswers=Integer.parseInt(results[1]);
		for (int i=0 ; i<numberofAnswers ; i++) {
			restApi.retrieve(results[0], i, retrieveAet);
			status.setText("Retriving "+(i+1)+ "/"+ (numberofAnswers) );
		}
		status.setText("Done, waiting");
		
		
	}
	
	
	public void stopAutoFecth() {
		timer.cancel();
	}

}