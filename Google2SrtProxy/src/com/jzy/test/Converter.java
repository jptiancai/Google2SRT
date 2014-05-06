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
 * @version "0.5.6, 01/20/13"
 */
package com.jzy.test;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Converter {
    private String sortida;
    private InputStreamReader entrada;
    private double inc;
    private GUI gui;
    
    public Converter(GUI gui, InputStreamReader entrada, String sortida, double inc) {
        this.gui = gui;
        this.entrada = entrada;
        this.sortida = sortida;
        this.inc = inc;
    }
    
    private String tsrt(String dur) { // retorna duracio en format SRT
        String resultat = "";
        Integer idur, iaux;
        Double dd;
        String saux;
        
        dd = Double.valueOf(dur);
        dd = Double.valueOf(dd.doubleValue() + this.inc);
        
        idur = Integer.valueOf(dd.intValue());
        
        iaux = Integer.valueOf(idur.intValue() / 3600);
        if (iaux < 10)
            resultat += "0";
        resultat += iaux.toString(); // hores
        
        resultat += ":";
        
        iaux = Integer.valueOf((idur.intValue()%3600) / 60);
        if (iaux < 10)
            resultat += "0";
        resultat += iaux.toString(); // mins
        
        resultat += ":";
        
        iaux = Integer.valueOf(idur.intValue()%60);
        if (iaux < 10)
            resultat += "0";
        resultat += iaux.toString(); // segons
        
        resultat += ",";
        
        dd = Double.valueOf(dd.doubleValue() - dd.intValue());
        
        saux = Double.toString(dd);
        
        switch (saux.length()) {
            case 3: // 0.x
                resultat += saux.substring(2, 3) + "00";
                break;
            case 4: // 0.xx
                resultat += saux.substring(2, 4) + "0";
                break;
            default: // 0.xxx.........
                if (saux.length() >= 5) // cas <3 --> ERROR!!
                    resultat += saux.substring(2, 5);
        }
        
        return resultat;
    }
    
    public void run() {
        this.iniciar();
    }
    
    private boolean iniciar() {
        int i, tam;
        SAXBuilder parser = new SAXBuilder();
        Document gSub = null;
        try {
            gSub = parser.build(entrada);
        } catch (JDOMException ex) {
            gui.msgFormatEntradaNoValid();
            return false;
        } catch (IOException ex) {
            gui.msgIOException();
            return false;
        }


        Element gSubRoot = gSub.getRootElement();
        List<Element> gSubRootFills = gSubRoot.getChildren();
        Element fill, post;
        String iTemps = null, fTemps = null, duracio = null; // inici i fi de subtítol
        double dFi;
        String text;

        OutputStreamWriter f;

        if (gSubRootFills.isEmpty()) {
            gui.msgFitxerNoSubtitols();
            return false;
        }
        try {
            f = new OutputStreamWriter(new FileOutputStream(sortida), "UTF-8");
        } catch (IOException ex) {
            gui.msgIOException();
            return false;
        }
        
        try {
            f.write("\ufeff"); // UTF-8 BOM: EF BB BF
        } catch(IOException ex) {
                gui.msgIOException();
                return false;
        }
        
        tam = gSubRootFills.size();
        for (i=0; i<tam; i++) {
            fill = gSubRootFills.get(i);
            iTemps = fill.getAttributeValue("start"); // temps inici

            duracio = fill.getAttributeValue("dur");
            if (duracio != null) { // no indica duració
                dFi = Double.valueOf(iTemps).doubleValue();
                dFi += Double.valueOf(duracio).doubleValue();
                fTemps = (Double.valueOf(dFi)).toString(); // temps fi
            } else {
                if (i+1 < tam) { // tots menys es darrer
                    post = gSubRootFills.get(i+1);
                    fTemps = post.getAttributeValue("start"); // temps inici
                } else { // darrer element --> +10 segons
                    dFi = Double.valueOf(iTemps).doubleValue();
                    dFi += Double.valueOf(10.0).doubleValue();
                    fTemps = (Double.valueOf(dFi)).toString(); // temps fi
                }
            }



            //text = fill.getTextNormalize(); // text
            text = fill.getText(); // text
            text = text.replaceAll("&quot;", "\"");
            text = text.replaceAll("&amp;", "&");
            text = text.replaceAll("&#39;", "'");
            text = text.replaceAll("&lt;", "<");
            text = text.replaceAll("&gt;", ">");

            try {
                f.write(String.valueOf(i+1)); f.write("\r\n");
                f.write(tsrt(iTemps) + " --> " + tsrt(fTemps));
                f.write("\r\n");
                f.write(text); f.write("\r\n");
                f.write("\r\n");
            } catch(IOException ex) {
                gui.msgIOException();
                return false;
            }
        }
        try {
            f.close();
        } catch(IOException ex) {
            gui.msgIOException();
            return false;
        }
        return true; // ok!
    }

}
