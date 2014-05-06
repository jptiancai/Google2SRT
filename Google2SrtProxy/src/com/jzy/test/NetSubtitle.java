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
 * @version "0.5.2, 07/11/09"
 */
package com.jzy.test;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NetSubtitle {
    public static enum tipus {Google, YouTube};
    
    private tipus tipus;
    private String name = null,     // nom pista (track)
            lang = null,            // codi idioma
            langOriginal = null,    // nom de l'idioma, en el propi idioma
            id = null;              // "docid" (GV) o "v" (YouTube)
    private int idXML = -1;         // atribut "id" dins llista XML de subs
    private boolean isDefault;      // atribut "default" dins llista XML de subs
    
    public NetSubtitle(tipus x) {
        this.tipus = x;
        this.isDefault = false; // tots (excepte un) no són "per defecte"
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setDefault() {
        this.isDefault = true;
    }
    
    public void setIdXML(int id) {
        this.idXML = id;
    }
    
    public void setLangOriginal(String lang) {
        this.langOriginal = lang;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getLang() {
        return this.lang;
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean isDefault() {
        return this.isDefault;
    }
    
    public String getLangOriginal() {
        return this.langOriginal;
    }

    public int getIdXML() {
        return this.idXML;
    }
    
    public String getTrackURL() throws UnsupportedEncodingException {
        String s;
        switch (this.tipus) {
            case Google:
                s = "http://video.google.com/videotranscript?frame=c&type=track&" +
                        "name=" + URLEncoder.encode(this.name, "UTF-8") +
                        "&lang=" + URLEncoder.encode(this.lang, "UTF-8") +
                        "&docid=" + URLEncoder.encode(this.id, "UTF-8");
                System.out.println("(DEBUG) Track URL: " + s);
                return s;
            case YouTube:
                s = "http://video.google.com/timedtext?type=track&" +
                        "name=" + URLEncoder.encode(this.name, "UTF-8") +
                        "&lang=" + URLEncoder.encode(this.lang, "UTF-8") +
                        "&v=" + URLEncoder.encode(this.id, "UTF-8");
                System.out.println("(DEBUG) Track URL: " + s);
                return s;
        }
        return null;
    }
    
    public static String getListURL(String id, tipus t) throws UnsupportedEncodingException {
        String s;
        switch (t) {
            case Google:
                s = "http://video.google.com/videotranscript?frame=c&type=list&docid=" +
                        URLEncoder.encode(id, "UTF-8");
                System.out.println("(DEBUG) List URL: " + s);
                return s;
            case YouTube:
                s = "http://video.google.com/timedtext?type=list&v=" +
                        URLEncoder.encode(id, "UTF-8");
                System.out.println("(DEBUG) List URL: " + s);
                return s;
        }
        return null;
    }
    
    public String getListURL() throws UnsupportedEncodingException {
        return NetSubtitle.getListURL(this.id, this.tipus);
    }
    
}
