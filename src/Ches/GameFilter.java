package Ches;

import java.io.File;
import javax.swing.filechooser.*;

public class GameFilter extends FileFilter {
 //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = this.getFileExtension(f);
        if (extension != null) {
            if (extension.equals("csw")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Chess Wars (.csw)";
    }
    
    public String getFileExtension(File f) {
        String s = null;
        s = f.getName();
        int period = s.lastIndexOf('.');
        
        if (period > 0 && period < s.length() - 1){
            s = s.substring((period+1), s.length());
        }
        
        return s;
    }
}


