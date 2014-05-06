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
package com.jzy.test;
/**
 *
 * @author kom
 * @version "0.4, 09/06/08"
 */

public class Common {
    public static String getExtension(String nom) {
        String ext;
        int i;
        
        ext = null;
        if (nom == null)
            return null;
        i = nom.lastIndexOf(".");
        if ((i > 0) && (i < nom.length() - 1))
            ext = nom.substring(i+1).toLowerCase();
        
        return ext;
    }

    public static String removeExtension(String nom) {
        String noExt;
        int i;
        
        noExt = nom;
        i = nom.lastIndexOf(".");
        if ((i > 0) && (i < nom.length() - 1))
            noExt = nom.substring(0, i);
        
        return noExt;
    }
}
