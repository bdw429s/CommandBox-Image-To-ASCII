/**
 * Display an image in ASCII to the console.  Image will be scaled to fit the console.  Mostly for fun.
 * .
 * {code:bash}
 * imageToASCII myImage.jpg
 * {code}
 *
 **/	
component extends="commandbox.system.BaseCommand" aliases="" excludeFromHelp=false {
	
	// DI
	property name="tempDir" inject="tempDir@constants";
	
	/**
	 * @imagePath.hint Path to the image you want to display
	 **/
	function run( required string imagePath = '' ) {
		// Ensure path passed
		if( !len( arguments.imagePath ) ) {
			return "Please specify an image path."
		}
				
		// This will make each directory canonical and absolute
		arguments.imagePath = fileSystemUtil.resolvePath( arguments.imagePath );
		
		// Ensure path exists
		if( !fileExists( arguments.imagePath ) ) {
			return "#arguments.imagePath# does not exist!"
		}
		
		// Ensure path is an image
		if( !isImageFile( arguments.imagePath ) ) {
			return "#arguments.imagePath# is not an image!"
		}
		
		// Make images as big as we can
		// height * 2 because we skip every other horizontal line to help keep the aspect ratio
		var height = shell.getTermHeight()*2;
		// width - 3 to leave room for the scroll bar
		var width = shell.getTermWidth()-3;
		
		// Create new image object and resize it to fit
		var tempFile = imageNew( arguments.imagePath );
		ImageScaleToFit( tempFile, width, height );
		
		// Write it out to a temp directory
		var tempFilePath = tempDir & '/' & listLast( arguments.imagePath, '\/' );
		imageWrite( tempFile, tempFilePath );
		
		// Convert it to ASCII
		var imageToASCII = createObject( 'java', 'com.bradwood.ASCII.ImageToASCII', 'imageToASCII.jar').init();
		var result = imageToASCII.convertToASCII( tempFilePath );
		
		// print it out to the console
		print
			.onWhite( result )
			.line();
	}
	
}
