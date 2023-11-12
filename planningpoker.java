package EffortLogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

public class PlanningPoker extends JFrame {
    private JPanel planningPokerPanel;
    private List<Integer> estimates;
    private HistoricalData historicalData;
    private JLabel instructionLabel;
    private JButton beginPlanningButton;
    private JButton historicalDataButton;
    private JComboBox<String> searchOptionDropdown;
    private JTextField searchField;
    private JButton searchButton;
    private JButton searchBarButton;
    private JLabel historicalDataLabel;

    public PlanningPoker(){
        this.historicalData = new HistoricalData();
        estimates = new ArrayList<>();

        planningPokerPanel = new JPanel(new BorderLayout());

        String[] searchOptions = {"Search by keyword", "Search by date", "Search by programming language", "Search by project type"};
        searchOptionDropdown = new JComboBox<>(searchOptions);

        beginPlanningButton = new JButton("Begin Planning Poker");
        beginPlanningButton.addActionListener(e -> {
            displayPokerCards();
            hideButtonsAfterStart();
        });

        instructionLabel = new JLabel("", SwingConstants.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Select Search Option:"));
        searchPanel.add(searchOptionDropdown);

        JPanel searchInputPanel = new JPanel(new BorderLayout());
        searchInputPanel.add(searchPanel, BorderLayout.NORTH);

        searchField = new JTextField();
        searchField.setEditable(true);
        searchField.setText("    ");

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
			try {
				performSearch();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

        JPanel searchFieldPanel = new JPanel(new FlowLayout());
        searchFieldPanel.add(new JLabel("Search Historical Data:"));
        searchFieldPanel.add(searchField);
        searchFieldPanel.add(searchButton);

        searchInputPanel.add(searchFieldPanel, BorderLayout.SOUTH);

        planningPokerPanel.add(searchInputPanel, BorderLayout.NORTH);

        historicalDataButton = new JButton("View Historical Data");
        historicalDataButton.addActionListener(e -> displayHistoricalData());

        historicalDataLabel = new JLabel("", SwingConstants.CENTER);
        planningPokerPanel.add(historicalDataLabel, BorderLayout.CENTER);

        planningPokerPanel.add(historicalDataButton, BorderLayout.SOUTH);
        add(beginPlanningButton, BorderLayout.WEST);
        add(planningPokerPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> searchField.requestFocusInWindow());
    }

    private void displayPokerCards() {
        planningPokerPanel.removeAll();
        planningPokerPanel.setLayout(new BorderLayout());

        String[] pokerCardValues = {"0", "1/2", "1", "2", "3", "5", "8", "13", "20", "40", "100", "Infinity", "?", "Pause"};
        JPanel cardsPanel = new JPanel(new FlowLayout());
        for (String cardValue : pokerCardValues) {
            JButton pokerCardButton = new JButton(cardValue);
            pokerCardButton.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    if (cardValue.equals("Infinity") || cardValue.equals("?") || cardValue.equals("Pause")) {
                        estimates.add(0);
                    } else {
                        int estimate = Integer.parseInt(cardValue);
                        estimates.add(estimate);
                    }
                    updateHistoricalData("Current Project", estimates.get(estimates.size() - 1));
                    closePlanningPoker();
                    setupInitialScreen();
                });
            });
            cardsPanel.add(pokerCardButton);
        }

        instructionLabel.setText("Select a card based on the importance of User Story");
        cardsPanel.add(instructionLabel);

        planningPokerPanel.add(cardsPanel, BorderLayout.CENTER);

        revalidate();
    }

    private void hideButtonsAfterStart() {
        beginPlanningButton.setVisible(false);
    }

    private List<String> searchHistoricalData(String keyword, String searchOption) {
        return historicalData.getHistoricalData().stream()
                .filter(entry -> {
                    switch (searchOption) {
                        case "Search by date":
                            break;
                        case "Search by programming language":
                            break;
                        case "Search by project type":
                            break;
                        default:
                            return entry.toLowerCase().contains(keyword.toLowerCase());
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    private void displaySearchResults(File[] results) {
        StringBuilder message = new StringBuilder("Search Results:\n");
        for (File file : results) {
            message.append(file.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void performSearch() throws IOException {
        String searchKeyword = searchField.getText().trim();
        // Search the encrypted files for the keyword
        File[] matchingFiles = EncryptDecrypt.searchPlanningPokerFiles(searchKeyword);

        // If matching files are found, display their names
        if (matchingFiles.length > 0) {
            displaySearchResults(matchingFiles);
        } else {
            JOptionPane.showMessageDialog(PlanningPoker.this, "No matching projects found.",
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayHistoricalData() {
        JOptionPane.showMessageDialog(this, historicalDataLabel.getText(), "Historical Data", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateHistoricalData(String projectName, int storyPoints) {
        historicalData.addEntry(projectName, storyPoints);

        List<String> history = historicalData.getHistoricalData();
        StringBuilder message = new StringBuilder("Historical Data:\n");
        for (String entry : history) {
            message.append(entry).append("\n");
        }
        historicalDataLabel.setText(message.toString());
    }

    private void setupInitialScreen() {
        planningPokerPanel.removeAll();
        planningPokerPanel.setLayout(new BorderLayout());

        planningPokerPanel.add(beginPlanningButton, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Select Search Option:"));
        searchPanel.add(searchOptionDropdown);
        searchPanel.add(new JLabel("Search Historical Data:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        planningPokerPanel.add(searchPanel, BorderLayout.NORTH);
        planningPokerPanel.add(historicalDataButton, BorderLayout.SOUTH);

        revalidate();
        setVisible(true);
    }

    public List<Integer> startPlanningPoker() {
        estimates.clear();

        SwingUtilities.invokeLater(() -> setVisible(true));

        while (isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return estimates;
    }

    private void closePlanningPoker() {
        planningPokerPanel.remove(instructionLabel);

        Component[] components = planningPokerPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel cardsPanel = (JPanel) component;
                Component[] cardComponents = cardsPanel.getComponents();
                for (Component cardComponent : cardComponents) {
                    cardsPanel.remove(cardComponent);
                }
            }
        }

        setupInitialScreen();
        revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlanningPoker planningPoker = new PlanningPoker();
            List<Integer> estimates = planningPoker.startPlanningPoker();
            System.out.println("Selected estimates: " + estimates);
        });
    }
}

class HistoricalData {
    private List<String> history;

    public HistoricalData() {
        this.history = new ArrayList<>();
    }

    public void addEntry(String project, int storyPoints) {
        String entry = project + " - Story points: " + storyPoints;
        history.add(entry);
    }

    public List<String> getHistoricalData() {
        return history;
    }
}
