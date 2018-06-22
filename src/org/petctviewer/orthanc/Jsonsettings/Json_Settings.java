/**
Copyright (C) 2017 KANOUN Salim
This
 program is free software; you can redistribute it and/or modify
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

package org.petctviewer.orthanc.Jsonsettings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.packtag.implementation.JSMin;


public class Json_Settings {
	
	protected JSONObject index = new JSONObject();
	
	// Hasmap des AET
	protected HashMap<String,JSONArray> dicomNode=new HashMap<String,JSONArray>();
	// Hasmap des orthancPeer
	protected HashMap<String,JSONArray> orthancPeer=new HashMap<String,JSONArray>();
	// Hasmap des contentType
	protected HashMap<String,JSONArray> contentType=new HashMap<String,JSONArray>();
	// Hasmap des dictionary
	protected HashMap<String,JSONArray> dictionary=new HashMap<String,JSONArray>();
	
	// Array des Lua folder 
	protected JSONArray luaFolder=new JSONArray();
	
	//Array des plugin folder
	protected JSONArray pluginsFolder=new JSONArray();
	
	// Object pour les users
	protected JSONObject users =new JSONObject();
	
	//Object pour les metadata
	protected JSONObject userMetadata=new JSONObject();
	
	//OrthancPeer JsonObject
	protected JSONObject orthancPeers =new JSONObject();
	
	//DicomNode JSON object
	private JSONObject dicom =new JSONObject();
	
	//Object pour les content Type
	private JSONObject contentTypes=new JSONObject();
	
	//Object pour les dictionnaire
	private JSONObject dictionaries=new JSONObject();
	
	// A modifier via des setteurs
	protected String orthancName;
	protected String storageDirectory;
	protected String indexDirectory;
	protected boolean StorageCompression;
	protected int MaximumStorageSize;
	protected int MaximumPatientCount;
	protected boolean HttpServerEnabled;
	protected int HttpPort;
	protected boolean HttpDescribeErrors;
	protected boolean HttpCompressionEnabled;
	protected boolean DicomServerEnabled;
	protected String DicomAet;
	protected boolean DicomCheckCalledAet;
	protected int DicomPort;
	protected String DefaultEncoding;
	protected boolean DeflatedTransferSyntaxAccepted;
	protected boolean JpegTransferSyntaxAccepted;
	protected boolean Jpeg2000TransferSyntaxAccepted;
	protected boolean JpegLosslessTransferSyntaxAccepted;
	protected boolean JpipTransferSyntaxAccepted;
	protected boolean Mpeg2TransferSyntaxAccepted;
	protected boolean RleTransferSyntaxAccepted;
	protected boolean UnknownSopClassAccepted;
	protected int DicomScpTimeout;
	protected boolean RemoteAccessAllowed;
	protected boolean SslEnabled;
	protected String SslCertificate;
	protected boolean AuthenticationEnabled;
	protected int DicomScuTimeout;
	protected String HttpProxy;
	protected int HttpTimeout;
	protected boolean HttpsVerifyPeers;
	protected String HttpsCACertificates;
	protected int StableAge;
	protected boolean StrictAetComparison;
	protected boolean StoreMD5ForAttachments;
	protected int LimitFindResults;
	protected int LimitFindInstances;
	protected int LimitJobs;
	protected boolean LogExportedResources;
	protected boolean KeepAlive;
	protected boolean StoreDicom;
	protected int DicomAssociationCloseDelay;
	protected int QueryRetrieveSize;
	protected boolean CaseSensitivePN;
	protected boolean AllowFindSopClassesInStudy;
	protected boolean LoadPrivateDictionary;
	protected boolean DicomAlwaysStore;
	protected boolean CheckModalityHost;
	
	public Json_Settings() {
		initialiserIndex();
	}
	
	protected void initialiserIndex() {
		// on Set des valeurs par defaut
				orthancName="myOrthanc";
				storageDirectory="C:\\Orthanc\\OrthancStorage-v6";
				indexDirectory="C:\\Orthanc\\OrthancStorage-v6";
				StorageCompression=false;
				MaximumStorageSize=0;
				MaximumPatientCount=0;
				HttpServerEnabled=true;
				HttpPort=8042;
				HttpDescribeErrors=true;
				HttpCompressionEnabled=true;
				DicomServerEnabled=true;
				DicomAet="Orthanc";
				DicomCheckCalledAet=false;
				DicomPort=4242;
				DefaultEncoding="Latin1";
				DeflatedTransferSyntaxAccepted=true;
				JpegTransferSyntaxAccepted=true;
				Jpeg2000TransferSyntaxAccepted=true;
				JpegLosslessTransferSyntaxAccepted=true;
				JpipTransferSyntaxAccepted=true;
				Mpeg2TransferSyntaxAccepted=true;
				RleTransferSyntaxAccepted=true;
				UnknownSopClassAccepted=false;
				DicomScpTimeout=30;
				RemoteAccessAllowed=false;
				SslEnabled=false;
				SslCertificate="certificate.pem";
				AuthenticationEnabled=false;
				DicomScuTimeout=10;
				HttpsVerifyPeers=true;
				HttpProxy="";
				HttpsCACertificates="";
				StableAge=60;
				StrictAetComparison=false;
				StoreMD5ForAttachments=true;
				LimitFindResults=0;
				LimitFindInstances=0;
				LimitJobs=10;
				LogExportedResources=true;
				KeepAlive=false;
				StoreDicom=true;
				DicomAssociationCloseDelay=5;
				QueryRetrieveSize=10;
				CaseSensitivePN=false;
				AllowFindSopClassesInStudy=false;
				LoadPrivateDictionary=true;
				DicomAlwaysStore=true;
				CheckModalityHost=false;
	}
	
	// permet de creer le JSON avant de l'ecrire
	@SuppressWarnings("unchecked")
	public void construireIndex() {
		
		//On construit les objets dont on aura besoin
		buildOrthancPeer(orthancPeer);
		buildDicom(dicomNode);
		buildContentType(contentType);
		buildDictionary(dictionary);
		
		//On rentre les valeurs contenue dans les variables
		index.put("Name", orthancName);
		index.put("StorageDirectory", storageDirectory);
		index.put("IndexDirectory", indexDirectory);
		index.put("StorageCompression", StorageCompression);
		index.put("MaximumStorageSize", MaximumStorageSize);
		index.put("MaximumPatientCount", MaximumPatientCount);
		index.put("LuaScripts", luaFolder);
		index.put("Plugins", pluginsFolder);
		index.put("HttpServerEnabled", HttpServerEnabled);
		index.put("HttpPort", HttpPort);
		index.put("HttpDescribeErrors", HttpDescribeErrors);
		index.put("HttpCompressionEnabled", HttpCompressionEnabled);
		index.put("DicomServerEnabled", DicomServerEnabled);
		index.put("DicomAet", DicomAet);
		index.put("DicomCheckCalledAet", DicomCheckCalledAet);
		index.put("DicomPort", DicomPort);
		index.put("DefaultEncoding", DefaultEncoding);
		index.put("DeflatedTransferSyntaxAccepted", DeflatedTransferSyntaxAccepted);
		index.put("JpegTransferSyntaxAccepted", JpegTransferSyntaxAccepted);
		index.put("Jpeg2000TransferSyntaxAccepted", Jpeg2000TransferSyntaxAccepted);
		index.put("JpegLosslessTransferSyntaxAccepted", JpegLosslessTransferSyntaxAccepted);
		index.put("JpipTransferSyntaxAccepted", JpipTransferSyntaxAccepted);
		index.put("Mpeg2TransferSyntaxAccepted", Mpeg2TransferSyntaxAccepted);
		index.put("RleTransferSyntaxAccepted", RleTransferSyntaxAccepted);
		index.put("UnknownSopClassAccepted", UnknownSopClassAccepted);
		index.put("DicomScpTimeout", DicomScpTimeout);
		index.put("RemoteAccessAllowed", RemoteAccessAllowed);
		index.put("SslEnabled", SslEnabled);
		index.put("SslCertificate", SslCertificate);
		index.put("AuthenticationEnabled", AuthenticationEnabled);
		index.put("RegisteredUsers", users);
		index.put("DicomModalities", dicom);
		index.put("DicomScuTimeout", DicomScuTimeout);
		index.put("OrthancPeers", orthancPeers);
		index.put("HttpProxy", HttpProxy);
		index.put("HttpTimeout", HttpTimeout);
		index.put("HttpsVerifyPeers", HttpsVerifyPeers);
		index.put("HttpsCACertificates", HttpsCACertificates);
		index.put("UserMetadata", userMetadata);
		index.put("UserContentType", contentTypes);
		index.put("StableAge", StableAge);
		index.put("StrictAetComparison", StrictAetComparison);
		index.put("StoreMD5ForAttachments", StoreMD5ForAttachments);
		index.put("LimitFindResults", LimitFindResults);
		index.put("LimitFindInstances", LimitFindInstances);
		index.put("LimitJobs", LimitJobs);
		index.put("LogExportedResources", LogExportedResources);
		index.put("KeepAlive", KeepAlive);
		index.put("StoreDicom", StoreDicom);
		index.put("DicomAssociationCloseDelay", DicomAssociationCloseDelay);
		index.put("QueryRetrieveSize", QueryRetrieveSize);
		index.put("CaseSensitivePN", CaseSensitivePN);
		index.put("AllowFindSopClassesInStudy", AllowFindSopClassesInStudy);
		index.put("LoadPrivateDictionary", LoadPrivateDictionary);
		index.put("Dictionary", dictionaries);
		index.put("DicomAlwaysAllowStore", DicomAlwaysStore);
		index.put("DicomCheckModalityHost", CheckModalityHost);
	}

	/**
	 * Ajoute des utilisateurs pour Orthanc
	 * @param user
	 * @param password
	 */
	@SuppressWarnings("unchecked")
	protected void addusers(String user, String password) {
		//on ajoute l'utilisateur a l'array de users
		users.put(user, password);
	}
	
	/**
	 * Ajoute des metadata
	 * @param user
	 * @param number
	 */
	
	@SuppressWarnings("unchecked")
	protected void addUserMetadata(String user, int number) {
		//on ajoute l'utilisateur a l'array des metadata
		userMetadata.put(user, number);
	}
	
	
	/**
	 * Ajoute un repertoire lua a la liste des repertoire lua
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	protected void addLua(String path) {
		luaFolder.add(path);
	}
	
	/**
	 * Ajoute un repertoire plugin  a la liste des repertoire plugin
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	protected void addplugins(String path) {
		pluginsFolder.add(path);
	}
	
	/**
	 * Cree le JSON object des noeuds DICOM � partir de la Hashmap qui contient le nom du noeud et son array
	 * @param dicomNode :Hashmap des noeuds declares
	 * @return : le JSON object a inject
	 */
	@SuppressWarnings("unchecked")
	public void buildDicom(HashMap<String,JSONArray> dicomNode) {
		String[] noms=new String[dicomNode.size()];
		dicomNode.keySet().toArray(noms);
		for (int i=0 ; i<dicomNode.size() ; i++) {
			dicom.put(noms[i], dicomNode.get(noms[i]));
		}
	}
	
	/**
	 * Ajoute un AET dans la declaration
	 * @param nom
	 * @param name
	 * @param ip
	 * @param port
	 * @param wildcard
	 */
	@SuppressWarnings("unchecked")
	protected void addDicomNode(String nom, String name, String ip, int port, String wildcard) {
		JSONArray dicomNode=new JSONArray();
		dicomNode.add(name);
		dicomNode.add(ip);
		dicomNode.add(port);
		dicomNode.add(wildcard);
		this.dicomNode.put(nom,dicomNode); 
	}
	/**
	 * Cree un peer Ortanc et l'ajoute dans la hashmap Orthanc Peer
	 * @param nom
	 * @param URL
	 * @param login
	 * @param password
	 */
	@SuppressWarnings("unchecked")
	protected void addorthancPeer(String nom, String URL, String login, String password) {
		JSONArray orthancPeer=new JSONArray();
		orthancPeer.add(URL);
		orthancPeer.add(login);
		orthancPeer.add(password);
		this.orthancPeer.put(nom,orthancPeer); 
	}
	
	/**
	 * Ajoute les content type dans la hashmap
	 * @param name
	 * @param number
	 * @param mime
	 */
	@SuppressWarnings("unchecked")
	protected void addContentType(String name, int number, String mime) {
		JSONArray contentType=new JSONArray();
		contentType.add(number);
		contentType.add(mime);
		this.contentType.put(name,contentType); 
	}
	
	/**
	 * Ajoute des valeurs dans le dictionnaire
	 * @param name
	 * @param vr
	 * @param tag
	 * @param minimum
	 * @param maximum
	 * @param privateCreator
	 */
	@SuppressWarnings("unchecked")
	protected void addDictionary(String name, String vr, String tag, int minimum, int maximum, String privateCreator) {
		JSONArray dictionary=new JSONArray();
		dictionary.add(vr);
		dictionary.add(tag);
		dictionary.add(minimum);
		dictionary.add(maximum);
		dictionary.add(privateCreator);
		this.dictionary.put(name,dictionary); 
	}
	
	// les build permet de generer les Objet JSON qui seront mis dans le JSON global
	
	@SuppressWarnings("unchecked")
	public void buildOrthancPeer(HashMap<String,JSONArray> peers) {
		String[] noms=new String[peers.size()];
		peers.keySet().toArray(noms);
		for (int i=0 ; i<peers.size() ; i++) {
			orthancPeers.put(noms[i], peers.get(noms[i]));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buildContentType(HashMap<String,JSONArray> contentType) {
		String[] noms=new String[contentType.size()];
		contentType.keySet().toArray(noms);
		for (int i=0 ; i<contentType.size() ; i++) {
			this.contentTypes.put(noms[i], contentType.get(noms[i]));
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void buildDictionary(HashMap<String,JSONArray> dictionary) {
		String[] noms=new String[dictionary.size()];
		dictionary.keySet().toArray(noms);
		for (int i=0 ; i<dictionary.size() ; i++) {
			this.dictionaries.put(noms[i], dictionary.get(noms[i]));
		}
	}
	
	
	/**
	 * Permet de lire un fichier et enlever les commentaires avec JSmin
	 * @throws Exception
	 */
	public void setExistingJsonConfig(File fichierInput) throws Exception {
		 try {
			 FileReader reader= new FileReader(fichierInput);
			 
			 //On passe dans JSMin pour enlever les commentaire avant le parsing
			 StringWriter out = new StringWriter();
			 JSMin js= new JSMin(reader, out);
			 
			 js.jsmin();
			 
			
			 JSONParser parser = new JSONParser();
			 JSONObject orthancJson= (JSONObject) parser.parse(out.toString());
		
			 parserOrthancJson(orthancJson);
		 }
	 
		 catch (FileNotFoundException e1) {	e1.printStackTrace();}
			
		
	}
	
	
	//Evnoie le resultat du parsing dans les variables de l'index qui sert a produire le nouveau Json
	@SuppressWarnings("unchecked")
	private void parserOrthancJson(JSONObject orthancJson) throws ParseException {
		//Boucle try pour eviter de bloquer en cas d'element manquant lors du parsing (version anterieure de JSON par exemple)
		try {
			orthancName=(String) orthancJson.get("Name");
			storageDirectory=(String) orthancJson.get("StorageDirectory");
			indexDirectory=(String) orthancJson.get("IndexDirectory");
			StorageCompression=(boolean) orthancJson.get("StorageCompression");
			MaximumStorageSize=Integer.valueOf(orthancJson.get("MaximumStorageSize").toString());
			MaximumPatientCount=Integer.valueOf(orthancJson.get("MaximumPatientCount").toString());
			HttpServerEnabled=(boolean) orthancJson.get("HttpServerEnabled");
			HttpPort=Integer.valueOf(orthancJson.get("HttpPort").toString());
			HttpDescribeErrors=(boolean) orthancJson.get("HttpDescribeErrors");
			HttpCompressionEnabled=(boolean) orthancJson.get("HttpCompressionEnabled");
			DicomServerEnabled=(boolean) orthancJson.get("DicomServerEnabled");
			DicomAet=(String) orthancJson.get("DicomAet");
			DicomCheckCalledAet=(boolean) orthancJson.get("DicomCheckCalledAet");
			DicomPort=Integer.valueOf(orthancJson.get("DicomPort").toString());
			DefaultEncoding=(String) orthancJson.get("DefaultEncoding");
			DeflatedTransferSyntaxAccepted=(boolean) orthancJson.get("DeflatedTransferSyntaxAccepted");
			JpegTransferSyntaxAccepted=(boolean) orthancJson.get("JpegTransferSyntaxAccepted");
			Jpeg2000TransferSyntaxAccepted=(boolean) orthancJson.get("Jpeg2000TransferSyntaxAccepted");
			JpegLosslessTransferSyntaxAccepted=(boolean) orthancJson.get("JpegLosslessTransferSyntaxAccepted");
			JpipTransferSyntaxAccepted=(boolean) orthancJson.get("JpipTransferSyntaxAccepted");
			Mpeg2TransferSyntaxAccepted=(boolean) orthancJson.get("Mpeg2TransferSyntaxAccepted");
			RleTransferSyntaxAccepted=(boolean) orthancJson.get("RleTransferSyntaxAccepted");
			UnknownSopClassAccepted=(boolean) orthancJson.get("UnknownSopClassAccepted");
			DicomScpTimeout=Integer.valueOf(orthancJson.get("DicomScpTimeout").toString());
			RemoteAccessAllowed=(boolean) orthancJson.get("RemoteAccessAllowed");
			SslEnabled=(boolean) orthancJson.get("SslEnabled");
			SslCertificate=(String) orthancJson.get("SslCertificate");
			AuthenticationEnabled=(boolean) orthancJson.get("AuthenticationEnabled");
			DicomScuTimeout=Integer.valueOf(orthancJson.get("DicomScuTimeout").toString());
			HttpProxy=(String) orthancJson.get("HttpProxy");
			HttpTimeout=Integer.valueOf(orthancJson.get("HttpTimeout").toString());
			HttpsVerifyPeers=(boolean) orthancJson.get("HttpsVerifyPeers");
			HttpsCACertificates=(String) orthancJson.get("HttpsCACertificates");
			StableAge=Integer.valueOf(orthancJson.get("StableAge").toString());
			StrictAetComparison=(boolean) orthancJson.get("StrictAetComparison");
			StoreMD5ForAttachments=(boolean) orthancJson.get("StoreMD5ForAttachments");
			LimitFindResults=Integer.valueOf(orthancJson.get("LimitFindResults").toString());
			LimitFindInstances=Integer.valueOf(orthancJson.get("LimitFindInstances").toString());
			LimitJobs=Integer.valueOf(orthancJson.get("LimitJobs").toString());
			LogExportedResources=(boolean) orthancJson.get("LogExportedResources");
			KeepAlive=(boolean) orthancJson.get("KeepAlive");
			StoreDicom=(boolean) orthancJson.get("StoreDicom");
			DicomAssociationCloseDelay=Integer.valueOf(orthancJson.get("DicomAssociationCloseDelay").toString());
			QueryRetrieveSize=Integer.valueOf(orthancJson.get("QueryRetrieveSize").toString());
			CaseSensitivePN=(boolean) orthancJson.get("CaseSensitivePN");
			AllowFindSopClassesInStudy=(boolean) orthancJson.get("AllowFindSopClassesInStudy");
			LoadPrivateDictionary=(boolean) orthancJson.get("LoadPrivateDictionary");
			CheckModalityHost=(boolean)orthancJson.get("DicomCheckModalityHost");
			DicomAlwaysStore=(boolean)orthancJson.get("DicomAlwaysAllowStore");
			
			//On recupere les autres objet JSON dans le JSON principal
			//on recupere les AET declares par un nouveau parser
			JSONParser parser = new JSONParser();
			dicomNode= (JSONObject) parser.parse(orthancJson.get("DicomModalities").toString());
			
			//On recupere les users
			users= (JSONObject) parser.parse(orthancJson.get("RegisteredUsers").toString());
			
			// On recupere les Lua scripts
			luaFolder= (JSONArray) parser.parse(orthancJson.get("LuaScripts").toString());
			
			// On recupere les plugins
			pluginsFolder= (JSONArray) parser.parse(orthancJson.get("Plugins").toString());
			
			//On recupere les metadata
			userMetadata= (JSONObject) parser.parse(orthancJson.get("UserMetadata").toString());
			
			// On recupere les dictionnary
			dictionary= (JSONObject) parser.parse(orthancJson.get("Dictionary").toString());
			
			// On recupere les Content
			contentType= (JSONObject) parser.parse(orthancJson.get("UserContentType").toString());
			
			// On recupere les Peer
			orthancPeer=(JSONObject) parser.parse(orthancJson.get("OrthancPeers").toString());
			
		}
		catch (NullPointerException e) {}
	}
	
	/**
	 * Permet d'ecrire le JSON final dans un fichier
	 * @param json
	 * @param fichier
	 */
	public void writeJson(JSONObject json, File fichier) {
		//Pour un export plus lisible on utilise Gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonString = gson.toJson(json);
		//Pour ecrire le Json
		BufferedWriter writer=null;
		try {
			writer = new BufferedWriter(new java.io.FileWriter(fichier));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			writer.write(jsonString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
		
}