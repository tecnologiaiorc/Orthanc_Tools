/**
Copyright (C) 2017 VONGSALAT Anousone

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public v.3 License as published by
the Free Software Foundation;

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */


package org.petctviewer.orthanc.query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.petctviewer.orthanc.setup.OrthancRestApis;

public class Rest {

	private OrthancRestApis connexion;
	private JSONObject contentJson=new JSONObject();
	private JSONParser parser = new JSONParser();

	public Rest(OrthancRestApis connexion){
		this.connexion=connexion;
	}


	/*
	 *  This method is usually called within other methods to get an Orthanc query ID
	 */
	private String getQueryID(String level, String name, String id, String studyDate, String modality, String studyDescription, String accessionNumber, String aet) {
		// We re-define the new query
		String query =setQuery(level, name, id, studyDate, modality, studyDescription, accessionNumber);
		String ID = null;
		
		JSONObject answer;
		try {
			answer = (JSONObject) parser.parse(connexion.makePostConnectionAndStringBuilder("/modalities/" + aet + "/query/", query).toString());
			ID=(String) answer.get("ID");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ID;
	}


	/*
	 * This method gets the answer's indexes to an Orthanc query, as an Object[].
	 * An Object[] should be instantiated to store the values inside it.
	 */
	public String[] getQueryAnswerIndexes(String level, String name, String id, String studyDate, String modality, String studyDescription, String accessionNumber, String aet) {
		// We call getQueryID to generate a query ID
		String idQuery =  this.getQueryID(level, name, id, studyDate, modality, studyDescription, accessionNumber, aet);
		
		JSONArray contentArray = null;
		try {
			contentArray = (JSONArray) parser.parse(connexion.makeGetConnectionAndStringBuilder("/queries/" + idQuery + "/answers/").toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String[] resultatQueryIDSize = new String[2];
		resultatQueryIDSize[0]=idQuery;
		resultatQueryIDSize[1]=String.valueOf(contentArray.size());
		return resultatQueryIDSize;
	}

	/*
	 * This method returns the content of a specified index.
	 * The index is obtained by using the getQueryAnswerIndexes.
	 */
	public String getIndexContent(String queryId, int index) {
		String content = null;
		content = connexion.makeGetConnectionAndStringBuilder( "/queries/" + queryId + "/answers/" + index + "/content/").toString();
		return content;
	}

	/*
	 * This method returns the desired value, it requires a getIndexContent String, and the desired value.
	 * Some values can only be obtained if the contents correspond with the right query level 
	 * (StudyDescription is obtained via the Series level).
	 * These values may be PatientID, PatientName, StudyDate or StudyDescription.
	 */
	public Object getValue(String contents, String desiredValue){
		if(contents == null){
			return null;
		}
		try {
			contentJson= (JSONObject) parser.parse(contents);
		} catch (ParseException e) {e.printStackTrace();}
		String s1 = null;
		// We build a substring s1 to get the part from the contents to the end
		// Value are read in JSON objects
		switch (desiredValue) {
		case "StudyDescription":
			try {
				if (contentJson.containsKey("0008,1030")) {
					JSONObject studyDescriptionJson=(JSONObject) parser.parse(contentJson.get("0008,1030").toString());
					s1=(String) studyDescriptionJson.get("Value");
				}
				else s1="";
				
			} catch (ParseException e) {e.printStackTrace();}
			break;
			
		case "PatientName":
			try {
				if (contentJson.containsKey("0010,0010")) {
					JSONObject patientNameJson=(JSONObject) parser.parse(contentJson.get("0010,0010").toString()); 
					s1=(String) patientNameJson.get("Value");
				}
				else s1="";
			} catch (ParseException e) {e.printStackTrace();}
			break;
			
		case "Modality":
			try {
				if (contentJson.containsKey("0008,0061")) {
					JSONObject modalityJson=(JSONObject) parser.parse(contentJson.get("0008,0061").toString());
					s1=(String) modalityJson.get("Value");
				}
				else s1="";
			} catch (ParseException e) {e.printStackTrace();}
			break;
			
		case "StudyInstanceUID":
			try {
				if (contentJson.containsKey("0020,000d")) {
					JSONObject studyUidJson=(JSONObject) parser.parse(contentJson.get("0020,000d").toString());
					s1=(String) studyUidJson.get("Value");
				}
				else s1="";
			} catch (ParseException e) {e.printStackTrace();}
			break;
		case "AccessionNumber":
			try {
				if (contentJson.containsKey("0008,0050")) {
					JSONObject accessionJson=(JSONObject) parser.parse(contentJson.get("0008,0050").toString());
					s1=(String) accessionJson.get("Value");
				}
				else s1="";
			} catch (ParseException e) {e.printStackTrace();}
			break;
			
		case "ModalitiesInStudy":
			try {
				if (contentJson.containsKey("0008,0061")) {
					JSONObject modalitiesInStudyJson=(JSONObject) parser.parse(contentJson.get("0008,0061").toString());
					s1=(String) modalitiesInStudyJson.get("Value");
				}
				else s1="";
			} catch (ParseException e) {e.printStackTrace();}
			break;
			
		case "PatientID":
			try {
				if (contentJson.containsKey("0010,0020")) {
					JSONObject patientIdJson=(JSONObject) parser.parse(contentJson.get("0010,0020").toString());
					s1=(String) patientIdJson.get("Value");
				}
				else s1="";
			} catch (ParseException e) {e.printStackTrace();}
			break;
			
		case "StudyDate":
			try {
				if (contentJson.containsKey("0008,0020")) {
					JSONObject studyDateJson=(JSONObject) parser.parse(contentJson.get("0008,0020").toString());
					s1=(String) studyDateJson.get("Value");
				}
				else s1="";
			} catch (ParseException e) {e.printStackTrace();}
			break;
		default:
		}

		// We return a substring of s1 (s1 is cut so that we retain only the part before the ")
		return s1;
	}

	/*
	 * This method returns the series's descriptions's ID.
	 * It is treated separately because we only need the sole series's descriptions here. 
	 */
	public String getSeriesDescriptionID(String studyInstanceUID, String aet) {
		// getting the query ID
		String query = "{ \"Level\" : \"" + "Series" + "\", \"Query\" : "
				+ "{\"Modality\" : \"" + "*" + "\","
				+ "\"ProtocolName\" : \"" + "*" + "\","
				+ "\"SeriesDescription\" : \"" + "*" + "\","
				+ "\"SeriesInstanceUID\" : \"" + "*" + "\","
				+ "\"StudyInstanceUID\" : \"" + studyInstanceUID + "\"}"
				+ "}";
		
		JSONObject answer;
		String idURL =null;
		try {
			answer = (JSONObject) parser.parse(connexion.makePostConnectionAndStringBuilder("/modalities/" + aet + "/query/", query).toString());
			idURL=(String) answer.get("ID");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return idURL;
	}

	/*
	 * This method returns the series's description's (index 0) and modalities (index 1) values in an array
	 */
	public String[] [] getSeriesDescriptionValues(String idURL) {
		String[] [] values=null;
		try {
				JSONArray serverResponseArray=(JSONArray) parser.parse(connexion.makeGetConnectionAndStringBuilder("/queries/" + idURL + "/answers/").toString());
			
			if(serverResponseArray.size() == 0){
				throw new Exception("No Answer for this Query");
			}
			
			values = new String[3][serverResponseArray.size()];
			for(int i = 0; i < serverResponseArray.size(); i++){
				contentJson= (JSONObject) parser.parse(connexion.makeGetConnectionAndStringBuilder("/queries/" + idURL + "/answers/" + i + "/content").toString());
				
				if (contentJson.containsKey("0008,103e")) {
					JSONObject studyDescriptionJson=(JSONObject) parser.parse(contentJson.get("0008,103e").toString());
					values[0][i]=(String) studyDescriptionJson.get("Value");	
				} else {
					values[0][i]="";	
				}
				
				if (contentJson.containsKey("0008,0060")) {
					JSONObject modalityJson=(JSONObject) parser.parse(contentJson.get("0008,0060").toString());
					values[1][i]=(String) modalityJson.get("Value");
				}else {
					values[1][i]="";
				}
				
				if (contentJson.containsKey("0020,0011")) {
					JSONObject serieNumberJson;
				
						serieNumberJson = (JSONObject) parser.parse(contentJson.get("0020,0011").toString());
					
					values[2][i]=(String) serieNumberJson.get("Value");
				}else {
					values[2][i]="";
				}
				
				
			}
			
		} catch (Exception e) {
					e.printStackTrace();
				}
		return values;
	}

	/*
	 * This method retrieves an instance, depending on its query ID 
	 */
	public void retrieve(String queryID, int answer, String retrieveAET) {
		connexion.makePostConnectionAndStringBuilder("/queries/" + queryID + "/answers/" + answer + "/retrieve/", retrieveAET);
	}
	

	private String setQuery(String level, String name, String id, String studyDate, String modality, String studyDescription, String accessionNumber) {
		String query = "{ \"Level\" : \"" + level + "\", \"Query\" : "
				+ "{\"PatientName\" : \"" + name + "\","
				+ "\"PatientID\" : \"" + id + "\","
				+ "\"StudyDate\" : \"" + studyDate + "\","
				+ "\"ModalitiesInStudy\" : \"" + modality + "\","
				+ "\"StudyDescription\" : \"" + studyDescription + "\","
				+ "\"AccessionNumber\" : \"" + accessionNumber + "\"}"
				+ "}";
		System.out.println(query);
		return query;
	}
	
	public String[] getAets() {
		return connexion.getAET();
	}
	
	public String getLocalAet() {
		return connexion.getLocalAET();
	}

}
