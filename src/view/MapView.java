package view;

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
 * @author danwu
 * Subclass of JFrame that displays a Google Map web page.
 */
public class MapView extends JFrame{
    
	/**
	 * UID
	 */
	private static final long serialVersionUID = -4359549065769966244L;
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
	 * minimum longitude
	 */
	public static final double MIN_LNG = -180.0;
	/**
	 * maximum longitude
	 */
	public static final double MAX_LNG = 180.0;
	/**
	 * longitude increase step
	 */
	public static final double LNG_STEP = 20.0;
	/**
	 * longitude
	 */
	private static double lng;
	
	/**
	 * JxBrowser
	 */
	private Browser browser;
	
	/**
	 * Zoom in.
	 */
	public void zoomIn() {
		if (zoom < MAX_ZOOM) {
            browser.executeJavaScript("map.setZoom(" + ++zoom + ")");
        }
	}
	
	/**
	 * Zoom out.
	 */
	public void zoomOut() {
		if (zoom > MIN_ZOOM) {
            browser.executeJavaScript("map.setZoom(" + --zoom + ")");
        }
	}
	
	/**
	 * Update latitude and longitude.
	 */
	private void updateLatLng() {
		browser.executeJavaScript("map.setCenter({lat:" + lat + ",lng:" + lng + "})");
	}
	
	/**
	 * Move latitude towards North Pole.
	 */
	public void latNorth() {
		if (lat < MAX_LAT) {
			lat += LAT_STEP;
			if (lat > MAX_LAT) lat = MAX_LAT;
			updateLatLng();
        }
	}
	
	/**
	 * Move latitude towards South Pole.
	 */
	public void latSouth() {
		if (lat > MIN_LAT) {
			lat -= LAT_STEP;
			if (lat < MIN_LAT) lat = MIN_LAT;
			updateLatLng();
		}
	}
	
	/**
	 * Move longitude towards East.
	 */
	public void lngEast() {
		lng += LNG_STEP;
		if (lng > MAX_LNG) lng = MIN_LNG - MAX_LNG + lng;
		updateLatLng();
	}
	
	/**
	 * Move longitude towards West.
	 */
	public void lngWest() {
		lng -= LNG_STEP;
		if (lng < MIN_LNG) lng = -MIN_LNG + MAX_LNG + lng;
		updateLatLng();
	}
	
	/**
	 * Initilize the GUI.
	 * @throws IOException 
	 */
	private void initGUI() throws IOException {
		browser = new Browser();
        BrowserView view = new BrowserView(browser);
 
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(view, BorderLayout.CENTER);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        JButton btnZoomIn = new JButton("Zoom In");
        btnZoomIn.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
                zoomIn();            
            }

        });
        
        JButton btnZoomOut = new JButton("Zoom Out");
        btnZoomOut.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
                zoomOut();            
            }

        });
        
        JButton btnNorth = new JButton("Move North");
        btnNorth.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		latNorth();
            }

        });
        
        JButton btnSouth = new JButton("Move South");
        btnSouth.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		latSouth();
            }

        });
        
        JButton btnEast = new JButton("Move East");
        btnEast.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		lngEast();
            }

        });

        JButton btnWest = new JButton("Move West");
        btnWest.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		lngWest();
            }

        });
        
        JPanel toolBar = new JPanel();
        toolBar.add(btnZoomIn);
        toolBar.add(btnZoomOut);
        toolBar.add(btnNorth);
        toolBar.add(btnSouth);
        toolBar.add(btnEast);
        toolBar.add(btnWest);
        add(toolBar, BorderLayout.SOUTH);
        
        
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
	
	/**
	 * Start the view.
	 */
	public void start() {
		setVisible(true);
	}
	
	/**
	 * Constructor.
	 * @throws IOException 
	 */
	public MapView() throws IOException {
		initGUI();
	}
	
	/**
	 * Main function.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapView view = new MapView();
					view.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}); 
    }
    
} 