/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCEUtils;

import javax.swing.JFileChooser;

/**
 *
 * @author deni
 */
public class FileChooser {

    public static String chooseDirectory() {
        
        String directory = "";
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            // Save file
            directory = fileChooser.getSelectedFile().toPath().toString();
        }
        
        return directory;
    }
    
    

    public static String chooseFile(String... extension) {
        
        // TODO
        return null;
    }

}
