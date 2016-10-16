package main;

/*
 * Copyright (c) 2000-2016 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import util.Util;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
 
/**
 * The sample demonstrates how to create Browser instance, embed it,
 * load HTML content from string, and display it.
 */
public class HelloWorld {
    
	/**
	 * minimum zoom
	 */
	public static final int MIN_ZOOM = 0;
    /**
     * maximum zoom
     */
    public static final int MAX_ZOOM = 21;
	/**
	 * zoom
	 */
    private static int zoom;
	/**
	 * minimum latitude
	 */
	public static final double MIN_LAT = -89.0;
	/**
	 * maximum latitude
	 */
	public static final double MAX_LAT = 89.0;
	/**
	 * latitude increase step
	 */
	public static final double LAT_STEP = 1.0;
	/**
	 * latitude
	 */
	private static double lat;
	/**
	 * longitude
	 */
	private static double lng;
	
	/**
	 * Main function.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);
 
        JFrame frame = new JFrame("GoogleMap");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        JButton btnZoomIn = new JButton("Zoom In");
        btnZoomIn.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
                if (zoom < MAX_ZOOM) {
                    browser.executeJavaScript("map.setZoom(" + ++zoom + ")");
                }
            }

        });
        
        JButton btnZoomOut = new JButton("Zoom Out");
        btnZoomOut.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
                if (zoom > MIN_ZOOM) {
                    browser.executeJavaScript("map.setZoom(" + --zoom + ")");
                }
            }

        });
        
        JButton btnLatNorth = new JButton("Lat Move North");
        btnLatNorth.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (lat < MAX_LAT) {
        			lat += LAT_STEP;
        			if (lat > MAX_LAT) lat = MAX_LAT;
        			browser.executeJavaScript("map.setCenter({lat:" + lat + ",lng:" + lng + "})");
        			System.out.println("lat:"+lat);
                }
            }

        });
        
        JButton btnLatSouth = new JButton("Lat Move South");
        btnLatSouth.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (lat > MIN_LAT) {
        			lat -= LAT_STEP;
        			if (lat < MIN_LAT) lat = MIN_LAT;
        			browser.executeJavaScript("map.setCenter({lat:" + lat + ",lng:" + lng + "})");
        			System.out.println("lat:"+lat);
                }
            }

        });
        
        JPanel toolBar = new JPanel();
        toolBar.add(btnZoomIn);
        toolBar.add(btnZoomOut);
        toolBar.add(btnLatNorth);
        toolBar.add(btnLatSouth);
        frame.add(toolBar, BorderLayout.SOUTH);
        
        
        String content = Util.readFile("html/index.html", StandardCharsets.UTF_8);
        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                    // web page is loaded completely including all frames
                	zoom = (int)browser.executeJavaScriptAndReturnValue("map.getZoom()").getNumberValue();
                	lat = browser.executeJavaScriptAndReturnValue("map.getCenter().lat()").getNumberValue();
                	lng = browser.executeJavaScriptAndReturnValue("map.getCenter().lng()").getNumberValue();
                	
                }
            }
        });
        browser.loadHTML(content);
    }
    
} 