package org.fugerit.java.mod.dirmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.helpers.XMLConfigurableObject;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.log.LogFacade;
import org.w3c.dom.Element;

public class DefaultDirMapHandler extends XMLConfigurableObject implements DirMapHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4040071691833624135L;

	public DefaultDirMapHandler() {
		
	}
	
	/* (non-Javadoc)
	 * @see org.morozko.java.core.cfg.helpers.AbstractConfigurableObject#configure(org.w3c.dom.Element)
	 */
	public void configure(Element tag) throws ConfigException {

	}

	private void renderPage( File file, HttpServletResponse response ) throws IOException {
		String test = file.getName().toLowerCase();
		String type = "application/octet-stream";
		if ( test.indexOf( ".pdf" ) != -1 ) {
			type = "application/pdf";
		} else if ( test.indexOf( ".xls" ) != -1 ) {
			type = "application/excel";
		} else if ( test.indexOf( ".html" ) != -1 ||  test.indexOf( ".htm" ) != -1 ) {
			type = "text/html";
		} else if ( test.indexOf( ".css" ) != -1 ) {
			type = "text/css";
		}
		response.setContentType( type );
		LogFacade.getLog().debug( "MapOutputPage MapOutputPage : mime-type : "+type );
		FileInputStream fis = new FileInputStream( file );
		OutputStream os = response.getOutputStream();
		StreamIO.pipeStream( fis, os, StreamIO.MODE_CLOSE_IN_ONLY );
	}
	
	public void outputPage( HttpServletRequest request, HttpServletResponse response, DirMapConfig dirMapConfig ) throws IOException {
		String uri = request.getRequestURI();
		LogFacade.getLog().debug( "MapOutputPage URI  : "+uri );
		String filePath = uri.substring( request.getContextPath().length() );
		LogFacade.getLog().debug( "MapOutputPage FILE : "+filePath );
		File file = dirMapConfig.renderFile( filePath );
		String rootUri = ( new File( dirMapConfig.getDirectoryMapPath() ) ).toURI().toString();
		boolean flagRoot = dirMapConfig.isRoot( file );
//		boolean flagDir = ( uri.charAt( uri.length()-1 ) == '/' );
		if ( file.exists() ) {
			if ( file.isDirectory() ) {
				String[] list = file.list();
				String hasIndex = null;
				for ( int k=0; k<list.length; k++ ) {
					if ( dirMapConfig.isWelcomeFile( list[k] ) ) {
						hasIndex = list[k];
					}
				}
				if ( hasIndex != null ) {
					renderPage( new File( file, hasIndex ), response );
				} else {
					if ( dirMapConfig.isAllowListing() ) {
						response.setContentType( "text/html" );
						PrintWriter pw = new PrintWriter( response.getOutputStream(), true );
						String baseLink = request.getContextPath()+dirMapConfig.getExcludePath()+"/";
						File parent = file.getParentFile();
						String parentLink = null;
						if ( parent != null ) {
							String parsentUri = null;
							parsentUri = parent.toURI().toString();
							parentLink = baseLink+parsentUri.substring( rootUri.length() ); 
						}
						pw.println( "<html>" );
						pw.println( "<head><title>Directory listing : "+filePath+"</title></head>" );
						pw.println( "<body>" );
						pw.println( "<table>" );
						if ( !flagRoot && parentLink != null ) {
							pw.println( "<tr><td><img src='"+request.getContextPath()+dirMapConfig.getImageBaseUrl()+"blank.png'/></td><td><a href='"+parentLink+"'>...</a></td></tr>" );	
						}
						for ( int k=0; k<list.length; k++ ) {
							String currentUri = ( new File( file, list[k]) ).toURI().toString();
							String link = baseLink+currentUri.substring( rootUri.length() );
							pw.println( "<tr><td><img src='"+request.getContextPath()+dirMapConfig.getImageBaseUrl()+"file.png'/></td><td><a href='"+link+"'>"+list[k]+"</a></td></tr>" );	
						}
						pw.println( "</table>" );
						pw.println( "</body>" );
						pw.println( "</html>" );				
					} else {
						response.sendError( HttpServletResponse.SC_FORBIDDEN );
					}
				}
			} else {
				renderPage( file , response );
			}			
		} else {
			response.sendError( HttpServletResponse.SC_NOT_FOUND );
		}		
	}	
	
}
