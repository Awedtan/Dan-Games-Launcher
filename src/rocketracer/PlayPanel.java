package rocketracer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class PlayPanel extends JPanel {
    // Map of all tracks by trackID, alphabetically
    public static TreeMap<String, String> trackIDToFilepathMap = new TreeMap<>();

    public static ImageIcon bronzeMedal = new ImageIcon("assets/img/medal_bronze.png");
    public static ImageIcon bronzeMedalImageScaled = new ImageIcon(bronzeMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon silverMedal = new ImageIcon("assets/img/medal_silver.png");
    public static ImageIcon silverMedalImageScaled = new ImageIcon(silverMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon goldMedal = new ImageIcon("assets/img/medal_gold.png");
    public static ImageIcon goldMedalImageScaled = new ImageIcon(goldMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon authorMedal = new ImageIcon("assets/img/medal_author.png");
    public static ImageIcon authorMedalImageScaled = new ImageIcon(authorMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon noMedal = new ImageIcon("assets/img/medal_blank.png");
    public static ImageIcon noMedalImageScaled = new ImageIcon(noMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));

    // Necessary JComponents
    JPanel selectionPanel;
    JButton backButton;
    JScrollPane selectionScroll;

    /**
     * Loads all track files into memory (trackIDToFilepathMap)
     */
    public static void loadAllTracks() {
        File trackFolder = new File("tracks");
        File[] trackFolderArray = trackFolder.listFiles();

        for(File file : trackFolderArray) {
            String fileExt = "";
            int dot = file.getName().lastIndexOf(".");
            if (dot > 0) {
                fileExt = file.getName().substring(dot + 1);
            }

            // Only files with extension ".track" are parsed
            if (fileExt.compareToIgnoreCase("track") == 0) {
                try {
                    Track track = new Track(file.getAbsolutePath());
                    if (!(trackIDToFilepathMap.containsKey(track.getTrackID()))) {
                        // Basically if the track exists already it won't be added
                        // This goes based on the track ID
                        trackIDToFilepathMap.put(track.getTrackID(), file.getAbsolutePath());
                    }
                } catch (IllegalArgumentException | IOException e) { 
                    // Do nothing, it means it's an invalid track
                    // Or something went wrong with BufferedReader
                }
            }
        }
    }

    /**
     * Creates a button that allows the player to play the selected level
     * @param trackID ID of the track
     * @return a JButton containing the track ID and the player's highest achieved level on that track
     */
    public static JButton createLevelButton(String trackID) {
        String trackFilepath = trackIDToFilepathMap.get(trackID);
        if (trackFilepath == null) {
            return null;
        }

        ImageIcon medalImage = noMedalImageScaled;

        HashMap<String, String> metadataMap = Track.getMetaData(trackFilepath);
        Time personalBest = RMain.personalBests.get(trackID);
        Time bronzeTime = new Time(metadataMap.get("bronzeTime"));
        Time silverTime = new Time(metadataMap.get("silverTime"));
        Time goldTime = new Time(metadataMap.get("goldTime"));
        Time authorTime = new Time(metadataMap.get("authorTime"));

        if (!(personalBest == null)) {
            if (personalBest.compareTo(authorTime) <= 0) {
                medalImage = authorMedalImageScaled;
            } else if (personalBest.compareTo(goldTime) <= 0) {
                medalImage = goldMedalImageScaled;
            } else if (personalBest.compareTo(silverTime) <= 0) {
                medalImage = silverMedalImageScaled;
            } else if (personalBest.compareTo(bronzeTime) <= 0) {
                medalImage = bronzeMedalImageScaled;
            } else {
                medalImage = noMedalImageScaled;
            }
        }

        JButton newButton = new JButton(trackID);
        newButton.setIcon(medalImage);
        newButton.setForeground(Color.WHITE);
        newButton.setBackground(null);
        newButton.setBorder(RMain.buttonBorder);
        newButton.setFont(RMain.uiTextMedium);
        newButton.setFocusable(false);
        newButton.setAlignmentY(0f);
        newButton.setAlignmentX(0f);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RMain.showGame(trackFilepath);
            }
        });

        return newButton;
    }

    /**
     * Creates a new instance of PlayPanel
     */
    public PlayPanel() {
        super(true);

        // Loading all the tracks first
        loadAllTracks();

        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(0f);
        setAlignmentY(0f);
        setBackground(new Color(50, 50, 50));

        // Initializing the back button
        backButton = new JButton("< Back");
        backButton.setFont(RMain.uiTextBig);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setBorder(RMain.buttonBorder);
        backButton.setFocusable(false);
        backButton.setAlignmentX(0f);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RMain.showMainMenu();
            }
        });

        // Level selection list
        GridLayout selectionPanelGrid = new GridLayout(0, 5);
        selectionPanelGrid.setHgap(10);
        selectionPanelGrid.setVgap(10);
        selectionPanel = new JPanel();
        selectionPanel.setLayout(selectionPanelGrid);
        selectionPanel.setBackground(null);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        selectionPanel.setFocusable(false);
        selectionPanel.setAlignmentX(0f);

        selectionScroll = new JScrollPane();

        // Adding a new button to the grid for every level in the folder
        for (String trackID : trackIDToFilepathMap.keySet()) {
            selectionPanel.add(createLevelButton(trackID));
        }
        
        // Adding the UI components
        add(selectionPanel, BorderLayout.LINE_START);
        add(backButton, BorderLayout.PAGE_END);
    }
}
