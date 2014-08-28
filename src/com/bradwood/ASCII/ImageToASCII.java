package com.bradwood.ASCII;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

// Based on code found here:
// http://www.intosec.org/java/(java-code)image-to-ascii-art-generator/
public class ImageToASCII {


    BufferedImage img;
    double prevpixval;
    StringBuffer prntwrt;


    public ImageToASCII() {
    	prntwrt = new StringBuffer( 15000 );
    }
    
    public String convertToASCII(String imgname) throws IOException {
        img = ImageIO.read(new File(imgname));

        // Skip every other line since ASCII characters are taller than they are wide
        for (int i = 0; i < img.getHeight(); i=i+2)
        {
            for (int j = 0; j < img.getWidth(); j++)
            {
                Color pixcol = new Color(img.getRGB(j, i));
                
                // For all rows but the first, average in the row we skipped
                if( i > 1 ) {
                    Color prevpixcol = new Color(img.getRGB(j, i-1));
                    pixcol = new Color(
                    		( pixcol.getRed() + prevpixcol.getRed() ) / 2,
                    		( pixcol.getGreen() + prevpixcol.getGreen() ) / 2,
                    		( pixcol.getBlue() + prevpixcol.getBlue() ) / 2
                    	);
                }

                prntwrt.append( strChar( pixcol ) );
            }
            try {
                prntwrt.append( '\n' );
            } catch (Exception ex) {
            }
        }
        return prntwrt.toString();
    }

    private String strChar( Color pixcol ) {        
        return buildColor( pixcol ) + buildCharacter( pixcol );        
    }
    
    private String buildColor( Color pixcol ) {
    	
        int red = pixcol.getRed();
        int green = pixcol.getGreen();
        int blue = pixcol.getBlue();
    	int pixAvg = ( pixcol.getRed() + pixcol.getGreen() + pixcol.getBlue() ) / 3;
        
        String colorEscape = Character.toString((char) 27) + "[";
        
        // find the dominant color(s)
        if( isDominant( red, pixAvg ) && !isDominant( green, pixAvg ) && !isDominant( blue, pixAvg ) ) {
       		// red
        	colorEscape += "31";
	    } else if( !isDominant( red, pixAvg ) && isDominant( green, pixAvg ) && !isDominant( blue, pixAvg ) ) {
			// green
	    	colorEscape += "32";
	    } else if( isDominant( red, pixAvg ) && isDominant( green, pixAvg ) && !isDominant( blue, pixAvg ) ) {
			// yellow
	    	colorEscape += "33";
	    } else if( !isDominant( red, pixAvg ) && !isDominant( green, pixAvg ) && isDominant( blue, pixAvg ) ) {
			// blue
	    	colorEscape += "34";
	    } else if( isDominant( red, pixAvg ) && !isDominant( green, pixAvg ) && isDominant( blue, pixAvg ) ) {
			// magenta
	    	colorEscape += "35";
	    } else if( !isDominant( red, pixAvg ) && isDominant( green, pixAvg ) && isDominant( blue, pixAvg ) ) {
			// cyan
	    	colorEscape += "36";
	    } else if( pixAvg < 240 ) {
    		// black
        	colorEscape += "30";
    	} else {
    		// white
	    	colorEscape += "37";
    	}	

	    return colorEscape + "m";
    }

    private boolean isDominant( int num, int avg ) {        
        return  num - avg > 10;
    }
    
    private String buildCharacter( Color pixcol ) {             
   		// overall pixel darkness 
    	//double pixAvg = ( ( pixcol.getRed() * 0.30 ) + ( pixcol.getGreen() * 0.11 ) + ( pixcol.getBlue() * 0.59 ) );
    	double pixAvg = ( pixcol.getRed() + pixcol.getGreen() + pixcol.getBlue() ) / 3;
        
        if (pixAvg >= 240) {
            return " ";
        } else if (pixAvg >= 210) {
        	return ".";
        } else if (pixAvg >= 190) {
        	return "+";
        } else if (pixAvg >= 170) {
        	return "^";
        } else if (pixAvg >= 120) {
        	return "%";
        } else if (pixAvg >= 110) {
        	return "8";
        } else if (pixAvg >= 80) {
        	return "&";
        } else if (pixAvg >= 60) {
        	return "#";
        } else {
        	return "@";
        }
    }
        
    /*   
    public static void main(String[] args) throws IOException {
	    Img2Ascii obj=new ImageToASCII();
	    String foo = obj.convertToASCII("C:\\smiley.png");
	    System.out.print(foo);
    }
   */
}