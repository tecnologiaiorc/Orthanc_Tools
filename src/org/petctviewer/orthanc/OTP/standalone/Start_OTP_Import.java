package org.petctviewer.orthanc.OTP.standalone;

import java.awt.FlowLayout;
import java.util.Timer;
import java.util.prefs.Preferences;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.petctviewer.orthanc.anonymize.QueryOrthancData;
import org.petctviewer.orthanc.anonymize.VueAnon;
import org.petctviewer.orthanc.setup.OrthancRestApis;
import org.petctviewer.orthanc.setup.Run_Orthanc;

@SuppressWarnings("unused")
public class Start_OTP_Import {

	public static void main(String[] args) {

		Run_Orthanc runOrthanc=null;
		
		try {
			runOrthanc=new Run_Orthanc();
			runOrthanc.orthancJsonName="OrthancCTP.json";	
			runOrthanc.copyOrthanc(null);
			runOrthanc.startOrthanc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		OrthancRestApis restApis= new OrthancRestApis("http://localhost:8043");
		
		VueAnon anon=new VueAnon(restApis);
		//System.setProperty("java.net.useSystemProxies", "true");
		//VueAnon.jprefer=Preferences.userNodeForPackage(Start_OTP_Import.class);
		
		anon.setCTPaddress("https://petctviewer.com");
		anon.setRunOrthanc(runOrthanc);
		anon.setLocationRelativeTo(null);
		anon.setVisible(true);
		
		
		anon.tabbedPane.removeAll();
		//Add only OTP and export panel
		JPanel otp= new OTP_Tab(anon);
		anon.tabbedPane.addTab("OTP", otp);
		
		JPanel p2 = new JPanel(new FlowLayout());
		p2.add(anon.mainPanelExport);
		anon.tabbedPane.add("Export Anonymized", p2);
		anon.exportTabForOtp();
		
		/*
		 * SK A REFAIRE TESTER A ALINA
		Setup_OTP_Panel otpPanel=new Setup_OTP_Panel();
		anon.tabbedPane.add("Setup", otpPanel);
		
		anon.tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
			     
		    	 System.setProperty("http.proxyHost", otpPanel.getProxyAdress());
			     System.setProperty("http.proxyPort", otpPanel.getProxyPort());
			     System.setProperty("https.proxyHost",  otpPanel.getProxyAdress());
			     System.setProperty("https.proxyPort", otpPanel.getProxyPort());

				
			}
			
		});
		*/
		
		
		
		
	}

	

}


