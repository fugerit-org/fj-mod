package org.fugerit.java.mod.dirmap;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.cfg.ConfigurableObject;

public interface DirMapHandler extends ConfigurableObject {

	public void outputPage( HttpServletRequest request, HttpServletResponse response, DirMapConfig dirMapConfig ) throws IOException;
	
}
