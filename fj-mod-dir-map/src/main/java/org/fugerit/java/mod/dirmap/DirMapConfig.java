package org.fugerit.java.mod.dirmap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.core.xml.dom.SearchDOM;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DirMapConfig {
	
	public DirMapConfig() {
		this.welcomeFileList = new ArrayList<String>();
	}
	
	public static DirMapConfig configure( Document doc ) {
		org.fugerit.java.core.xml.dom.SearchDOM searchDOM = SearchDOM.newInstance( true, true );
		DirMapConfig dirMapConfig = new DirMapConfig();
		Element root = doc.getDocumentElement();
		Element generalConfig = searchDOM.findTag( root, "general-config" );
		Properties props = DOMUtils.attributesToProperties( generalConfig );
		dirMapConfig.setAllowListing( "1".equalsIgnoreCase( props.getProperty( "allow-listing" ) ) );
		dirMapConfig.setDirectoryMapPath( props.getProperty( "directory-map-path" ) );
		dirMapConfig.setImageBaseUrl( props.getProperty( "image-base-url" ) );
		dirMapConfig.setExcludePath( props.getProperty( "exclude-path" ) );
		Element welcomeFileListTag = searchDOM.findTag( root , "welcome-file-list" );
		if ( welcomeFileListTag != null ) {
			List<Element> welcomeFileTags = searchDOM.findAllTags( welcomeFileListTag , "welcome-file" );
			Iterator<Element> welcomeFileTagsIt = welcomeFileTags.iterator();
			while ( welcomeFileTagsIt.hasNext() ) {
				Element welcomeFile = (Element)welcomeFileTagsIt.next();
				String welcomeFileText = searchDOM.findText( welcomeFile );
				dirMapConfig.welcomeFileList.add( welcomeFileText );
			}
		}
		return dirMapConfig;
	}
	
	public File renderFile( String filePath ) {
		if ( filePath.indexOf( this.getExcludePath() )==0 ) {
			filePath = filePath.substring( this.excludePath.length() );
		}
		File file = new File( this.directoryMapPath, filePath );
		return file;
	}
	
	public boolean isRoot( File file ) throws IOException {
		File rootFile = new File( this.getDirectoryMapPath() );
		File testFile = new File( file.getCanonicalPath()+File.separator );
		return rootFile.equals( file ) || rootFile.equals( testFile );
	}
	
	private String excludePath;
	
	private String directoryMapPath;
	
	private String imageBaseUrl;
	
	private boolean allowListing;
	
	private List<String> welcomeFileList;

	public boolean isAllowListing() {
		return allowListing;
	}

	public boolean isWelcomeFile( String fileName ) {
		return this.welcomeFileList.contains( fileName );
	}
	
	public void setAllowListing(boolean allowListing) {
		this.allowListing = allowListing;
	}

	public String getDirectoryMapPath() {
		return directoryMapPath;
	}

	public void setDirectoryMapPath(String directoryMapPath) {
		this.directoryMapPath = directoryMapPath;
	}

	public String getImageBaseUrl() {
		return imageBaseUrl;
	}

	public void setImageBaseUrl(String imageBaseUrl) {
		this.imageBaseUrl = imageBaseUrl;
	}

	/**
	 * @return return excludePath value.
	 */
	public String getExcludePath() {
		return excludePath;
	}

	/**
	 * @param excludePath value to set
	 */
	public void setExcludePath(String excludePath) {
		this.excludePath = excludePath;
	}
	
}
