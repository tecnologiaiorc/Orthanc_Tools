/**
Copyright (C) 2017 VONGSALAT Anousone & KANOUN Salim

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

package org.petctviewer.orthanc.anonymize.listeners;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.petctviewer.orthanc.anonymize.TableSeriesModel;
import org.petctviewer.orthanc.anonymize.TableStudiesModel;
import org.petctviewer.orthanc.anonymize.datastorage.Study2;


public class TableStudiesMouseListener implements ListSelectionListener {
	
	private JFrame frame;
	private JTable tableau;
	private TableStudiesModel modele;
	private TableSeriesModel modeleSeries;

	public TableStudiesMouseListener(JFrame frame, JTable tableau, TableStudiesModel modele,
			TableSeriesModel modeleSeries, ListSelectionModel listSelection) {
		this.frame = frame;
		this.tableau = tableau;
		this.modele = modele;
		this.modeleSeries = modeleSeries;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {
			// We clear the details
			this.modeleSeries.clear();
			try {
				if(this.modele.getRowCount() != 0){
					Study2 study = (Study2)this.tableau.getValueAt(this.tableau.getSelectedRow(), 4);
					this.modeleSeries.addSerie(study);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			frame.pack();
			
		}
		
		
	}
	
	
}
