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
 * @version "0.5.5, 10/09/12"
 */
package com.jzy.test;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Vector;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Network {
    public static class HostNoGV extends Exception {};
    public static class NoDocId extends Exception {};
    public static class NoQuery extends Exception {};
    public static class DocIdInvalid extends Exception {};
    public static class NoSubs extends Exception {};
    public static class NoYouTubeParamV extends Exception {};
    
    public static final String PROXY_HOST="proxy-sg-singapore.gemalto.com";
    public static final String PROXY_PORT="8080";
    
    
    public static List<NetSubtitle> getSubtitles(String URL) throws MalformedURLException, HostNoGV, NoQuery, NoDocId, DocIdInvalid, UnsupportedEncodingException, JDOMException, IOException, NoSubs, NoYouTubeParamV {
        String param = "", urlLlista = "";
        URL url = null;
        Document docXML;
        List<NetSubtitle> lSubs;
        url = new URL(URL);
            
        if (url.getHost() == null) {
            throw new HostNoGV();
        } else if (url.getHost().indexOf("video.google.com") != -1) {
            param = docidFromURL(url);
            urlLlista = NetSubtitle.getListURL(param, NetSubtitle.tipus.Google);
            docXML = readListURL(urlLlista);
            lSubs = getListSubs(docXML, NetSubtitle.tipus.Google, param);
        } else if (url.getHost().indexOf("youtube.com") != -1
                || url.getHost().indexOf("youtu.be") != -1) {
            
            if (url.getHost().indexOf("youtu.be") != -1) {
                // http://youtu.be/c8RGPpcenZY => http://www.youtube.com/?v=c8RGPpcenZY
                
                String s;
                try { s = url.getFile(); }
                catch (Exception e) { s = " "; }
                
                url = new URL("http://www.youtube.com/?v=" + s.substring(1, s.length()));
            }
            param = paramVFromURL(url);
            urlLlista = NetSubtitle.getListURL(param, NetSubtitle.tipus.YouTube);
            docXML = readListURL(urlLlista);
            lSubs = getListSubs(docXML, NetSubtitle.tipus.YouTube, param);
        } else {
            throw new HostNoGV();
        }

        return lSubs;
    }

	private static void setProxy() {
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", PROXY_HOST);  
        System.setProperty("http.proxyPort", PROXY_PORT);
	}
    
    private static String docidFromURL(URL url) throws HostNoGV, NoQuery, NoDocId, DocIdInvalid {
        /** Google Video */
        String queryURL;
        int startDocId, endDocId;
        if ((url.getHost() == null) ||
           (url.getHost().indexOf("video.google.com") == -1)) {
            throw new HostNoGV();
        }
        
        if ((queryURL = url.getQuery()) == null) {
            throw new NoQuery();
        }

        if ((startDocId = queryURL.indexOf("docid=")) == -1) {
            throw new NoDocId();
        }

        startDocId += "docid=".length();
        endDocId = startDocId;

        if ((endDocId == queryURL.length()) ||
            !((Character.isDigit(queryURL.charAt(endDocId))) || (queryURL.charAt(endDocId) == '-'))) {
            throw new DocIdInvalid();
        }
        endDocId++;

        while ((endDocId < queryURL.length()) &&
                (Character.isDigit(queryURL.charAt(endDocId))))
            endDocId++;

        return queryURL.substring(startDocId, endDocId);
    }
    
    private static String paramVFromURL(URL url) throws HostNoGV, NoQuery, NoYouTubeParamV, DocIdInvalid {
        /** YouTube */
        String queryURL;
        int startDocId, endDocId;
        if ((url.getHost() == null) ||
           (url.getHost().indexOf("youtube.com") == -1)) {
            throw new HostNoGV();
        }

        if ((queryURL = url.getQuery()) == null) {
            throw new NoQuery();
        }

        if ((startDocId = queryURL.indexOf("v=")) == -1) {
            throw new NoYouTubeParamV();
        }

        startDocId += "v=".length();
        endDocId = startDocId;

        if ((endDocId == queryURL.length()) ||
            (queryURL.charAt(endDocId) == '&')) { // no buit
            throw new DocIdInvalid();
        }
        endDocId++;

        while ((endDocId < queryURL.length()) &&
                (queryURL.charAt(endDocId) != '&'))
            endDocId++;
        
        return queryURL.substring(startDocId, endDocId);
}
     
    private static Document readListURL(String sURL) throws MalformedURLException, JDOMException, IOException {
        URL url = null;
        SAXBuilder parser = new SAXBuilder();
        InputStreamReader isr;
        String appName, appVersion;
        appName = java.util.ResourceBundle.getBundle("Bundle").getString("app.name");
        appVersion = java.util.ResourceBundle.getBundle("Bundle").getString("app.version");
        setProxy();
        url = new URL(sURL);
        URLConnection urlconn = url.openConnection();
        urlconn.setRequestProperty("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlconn.setRequestProperty("Accept-Charset",
            "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        //urlconn.setRequestProperty("User-Agent",
        //        "Mozilla/5.0 (Windows; U; Windows NT 5.1; ca; rv:1.9.1) Gecko/20090624 Firefox/3.5 (.NET CLR 3.5.30729)");
        urlconn.setRequestProperty("User-Agent",
        "Mozilla/5.0 (compatible; " + appName + "/" + appVersion + ")");
        urlconn.connect();

        isr = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
        //isr = new InputStreamReader(url.openStream(), "UTF-8");

        return parser.build(isr);
        
    }
    
    private static List<NetSubtitle> getListSubs(Document xml, NetSubtitle.tipus t, String id)
            throws NoSubs, UnsupportedEncodingException {
        Element arrel, track;
        List<Element> tracks;
        int tam, i, tmpInt;
        Attribute tmpAtt;
        String tmpS, sName = "", sLang = "", sLangOrig = "";
        List<NetSubtitle> llista = new Vector<NetSubtitle>();
        NetSubtitle tNS;
        
        if (xml == null)
            throw new NoSubs();
        
        arrel = xml.getRootElement();
        tmpAtt = arrel.getAttribute("docid");
        if (tmpAtt == null)
            throw new NoSubs();

        tracks = arrel.getChildren();
        tam = tracks.size();
        if (tam == 0)
            return null;
        i = 0; track = null;
        while (i < tam) {
            track = tracks.get(i);
            tmpAtt = track.getAttribute("id");
            if (tmpAtt != null) {
                tmpS = tmpAtt.getValue();
                tmpInt = Integer.valueOf(tmpS);
                if (track != null) {
                    tmpAtt = track.getAttribute("name");
                    sName = tmpAtt.getValue();
                    tmpAtt = track.getAttribute("lang_code");
                    sLang = tmpAtt.getValue();
                    tmpAtt = track.getAttribute("lang_original");
                    sLangOrig = tmpAtt.getValue();

                    tNS = new NetSubtitle(t);
                    tNS.setId(id);
                    tNS.setIdXML(tmpInt);
                    tNS.setName(sName);
                    tNS.setLang(sLang);
                    //tNS.setLangOriginal(new String(sLangOrig.getBytes(Charset.defaultCharset()), "UTF-8"));
                    tNS.setLangOriginal(sLangOrig);

                    llista.add(tNS);
                }
            }
            i++;
        }
            

        return llista;
    }

    public static InputStreamReader readURL(String s) throws MalformedURLException,
            IOException {
        URL url = null;
        InputStreamReader isr;
        String appName, appVersion;
        appName = java.util.ResourceBundle.getBundle("Bundle").getString("app.name");
        appVersion = java.util.ResourceBundle.getBundle("Bundle").getString("app.version");
        url = new URL(s);
        URLConnection urlconn = url.openConnection();
        urlconn.setRequestProperty("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlconn.setRequestProperty("Accept-Charset",
            "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        urlconn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (compatible; " + appName + "/" + appVersion + ")");
        urlconn.connect();

        isr = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
        //isr = new InputStreamReader(url.openStream(), "UTF-8");
        
        return isr;
    }

}
