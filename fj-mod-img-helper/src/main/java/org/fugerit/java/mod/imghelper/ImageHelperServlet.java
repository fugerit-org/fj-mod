package org.fugerit.java.mod.imghelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageHelperServlet extends HttpServlet {
	
	private final static Logger logger = LoggerFactory.getLogger( ImageHelperServlet.class );
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6728876009586393088L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		int index = path.lastIndexOf( "/" );
		path = path.substring( index );
		String iconPath = "icon_default"+path;
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( iconPath ) ) {
			logger.debug( "iconPath -> {}", iconPath );
			OutputStream os = response.getOutputStream();
			StreamIO.pipeStream( is , os, StreamIO.MODE_CLOSE_BOTH );
		} catch ( Exception e ) {
			logger.error( "Errore : "+e, e );
			response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
		}
	}

}


