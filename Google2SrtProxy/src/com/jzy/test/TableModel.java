/*
    This file is part of Google2SRT.

    Google2SRT is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    Google2SRT is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Google2SRT.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author kom
 * @version "0.5, 06/06/09"
 */
package com.jzy.test;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
        private final String[] columnNames = {"Convert?", "Language code", "Language name", "Track name"};
        private java.util.ResourceBundle bundle;


        private Object dades[][];
        List<NetSubtitle> llista;

        public TableModel(java.util.ResourceBundle bundle) {
            this.llista = null;
            this.dades = null;
            this.bundle = bundle;
        }

        public Object[][] getData() {
            return this.dades;
        }

        public void setData(Object[][] dades) {
            this.dades = dades;
        }

        public void setBundle(java.util.ResourceBundle bundle) {
            this.bundle = bundle;
        }

        public void init(List<NetSubtitle> llista) {
            int i;
            NetSubtitle ns;

            this.llista = llista;
            this.dades = new Object[llista.size()][columnNames.length];

            if (this.llista.size() == 1) {
                ns = this.llista.get(0);
                dades[0][0] = new Boolean(true);               // checkbox
                dades[0][1] = new String(ns.getLang());         // lang code
                dades[0][2] = new String(ns.getLangOriginal()); // lang name
                dades[0][3] = new String(ns.getName());         // track name (optional)
            } else
                for (i = 0; i < this.llista.size(); i++) {
                    ns = this.llista.get(i);

                    dades[i][0] = new Boolean(false);               // checkbox
                    dades[i][1] = new String(ns.getLang());         // lang code
                    dades[i][2] = new String(ns.getLangOriginal()); // lang name
                    dades[i][3] = new String(ns.getName());         // track name (optional)
                }

        }

        public void init(List<NetSubtitle> llista, Object[][] dades) {
            this.llista = llista;
            this.dades = dades;
        }

      
        @Override
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0) return java.lang.Boolean.class;
            else return java.lang.String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return true;
            else return false;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            dades[row][col] = value;
            fireTableCellUpdated(row, col);
        }

        @Override
         public String getColumnName(int col) {
            String s;
            try { s = bundle.getString("GUI.jtLlistaSubtitols.columnName." + col); }
            catch (Exception e) { s = ""; }
            return s;
         }


        public int getRowCount() {
            if (dades == null) return 0;
            return dades.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return dades[rowIndex][columnIndex];
        }
    }