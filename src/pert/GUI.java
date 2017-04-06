package pert;
 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
 
public class GUI {
    private static final int width = 640, height = 670;
    private static int rowNumber = 14;
    private static JFrame frame;
    private static JPanel panelTable, panelSettings, panelResults;
    private static JTable table;
    private static JButton buttonRemoveRow, buttonAddRow, buttonSubmit;
    private static JLabel labelTime, labelTime2, label1, label2, label3, label4, label5, label6;
    private static JSpinner spinnerTime;
    private static Activity[] activities;
    private static Pert pert;
   
    public static void start() {
        initComponents();
    }
 
    public static void initComponents(){
        frame = new JFrame();
        frame.setTitle("PERT");
        frame.setSize(width, height);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
       
        panelTable = new JPanel(new BorderLayout());
        panelTable.setBounds(10, 10, 600, 343);
       
        JScrollPane scrollPane = new JScrollPane();
        table = new JTable();
 
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        DefaultTableModel model = new DefaultTableModel(
            new Object [][] {
                {1, "1-2", "3", "4", "5", ""},
                {2, "1-3", "3", "3", "3", ""},
                {3, "2-4", "7", "9", "17", "1"},
                {4, "2-5", "10", "12", "14", "1"},
                {5, "3-6", "1", "5", "9", "2"},
                {6, "3-7", "5", "10", "15", "2"},
                {7, "4-9", "6", "12", "18", "3"},
                {8, "5-8", "4", "6", "14", "4"},
                {9, "5-9", "1", "1", "7", "4"},
                {10, "6-8", "4", "4", "4", "5"},
                {11, "7-8", "10", "15", "20", "6"},
                {12, "7-10", "5", "5", "11", "6"},
                {13, "8-9", "5", "8", "11", "8;10;11"},
                {14, "9-10", "1", "5", "9", "7;9;13"}
            },
            new String [] {
                "", "Nazwa", "tc", "tm", "tp", "Poprzednicy"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };
 
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
 
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
       
        table.setModel(model);
        scrollPane.setViewportView(table);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.setDefaultRenderer(String.class, centerRenderer);
        
        panelTable.add(scrollPane);
        frame.add(panelTable);
       
        panelSettings = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 20));
        panelSettings.setBounds(panelTable.getX(), panelTable.getY() + panelTable.getHeight() , 600, 80);
       
        buttonAddRow = new JButton();
        buttonAddRow.setPreferredSize(new Dimension(70, 30));
        buttonAddRow.setMargin(new Insets(0, 0, 0, 0));
        buttonAddRow.setText("Dodaj rzad");
        buttonAddRow.addActionListener((ActionEvent e) -> {
            if (rowNumber == 0) {
                buttonRemoveRow.setEnabled(true);
            }
            model.addRow(new Object[] {++rowNumber, null, null, null, null, null});
        });
       
        buttonRemoveRow = new JButton();
        buttonRemoveRow.setPreferredSize(new Dimension(70, 30));
        buttonRemoveRow.setMargin(new Insets(0, 0, 0, 0));
        buttonRemoveRow.setText("Usun rzad");
        buttonRemoveRow.addActionListener((ActionEvent e) -> {
            if (rowNumber != 0) {
                model.removeRow(--rowNumber);
            }
            if (rowNumber == 0){
                buttonRemoveRow.setEnabled(false);
            }
        });
       
        labelTime = new JLabel("Dotrzymanie terminu do");
        labelTime.setPreferredSize(new Dimension(135, 30));
       
        labelTime2 = new JLabel("tyg.");
        labelTime2.setPreferredSize(new Dimension(60, 30));
       
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0.0, 99999, 1);
        spinnerTime = new JSpinner(spinnerModel);
        spinnerTime.setPreferredSize(new Dimension(50,30));
       
        buttonSubmit = new JButton("Oblicz");
        buttonSubmit.setMargin(new Insets(0, 0, 0, 0));
        buttonSubmit.setPreferredSize(new Dimension(50, 30));
        buttonSubmit.addActionListener((ActionEvent e) -> {
            pert = new Pert((int) ((double)spinnerTime.getValue()));
            resetResultLabelsText();
            
            if (!panelResults.isVisible()) {
                panelResults.setVisible(true);
            }
            int numActivities = 0;
            for (; numActivities < model.getRowCount(); numActivities++) {
                if(model.getValueAt(numActivities, 1) == null) {
                    break;
                }
            }
            
            activities = new Activity[numActivities];
            for (int row = 0; row < numActivities; row++){
                List<String> list = new ArrayList<>();
                for (int col = 0; col < 6; col++) {
                    if (col>1 && col<5) {
                        list.add(model.getValueAt(row, col).toString().replaceAll(",", "."));
                    }
                    else {
                        list.add(model.getValueAt(row, col).toString());
                    }
                }
                activities[row] = new Activity(list);
                pert.getActivities().add(activities[row]);
            }
            pert.addPreviousActivities();
            pert.doProbability();
        });
       
        panelSettings.add(buttonAddRow);
        panelSettings.add(buttonRemoveRow);
        panelSettings.add(labelTime);
        panelSettings.add(spinnerTime);
        panelSettings.add(labelTime2);
        panelSettings.add(buttonSubmit);
        frame.add(panelSettings);
       
        panelResults = new JPanel();
        panelResults.setLayout(new BoxLayout(panelResults, BoxLayout.Y_AXIS));
        panelResults.setBounds(panelTable.getX(), panelSettings.getY() + panelSettings.getHeight(), 600, 180);
       
        Font labelFont = new Font("Default", Font.BOLD, 15);
       
        label1 = new JLabel();
        label1.setFont(labelFont);
        label1.setPreferredSize(new Dimension(600, 20));
        label2 = new JLabel();
        label2.setFont(labelFont);
        label2.setPreferredSize(new Dimension(600, 20));
        label3 = new JLabel();
        label3.setFont(labelFont);
        label3.setPreferredSize(new Dimension(600, 20));
        label4 = new JLabel();
        label4.setFont(labelFont);
        label4.setPreferredSize(new Dimension(600, 20));
        label5 = new JLabel();
        label5.setFont(labelFont);
        label5.setPreferredSize(new Dimension(600, 20));
        label6 = new JLabel();
        label6.setFont(labelFont);
        label6.setPreferredSize(new Dimension(600, 20));
       
        panelResults.add(label1);
        panelResults.add(label2);
        panelResults.add(label3);
        panelResults.add(label4);
        panelResults.add(label5);
        panelResults.add(label6);
        frame.add(panelResults);
        panelResults.setVisible(false);
        frame.setVisible(true);
    }
    
    public static void resetResultLabelsText(){
        label1.setText("Sciezka krytyczna: ");
        label2.setText("Czas modelowy ukończenia przedsięwzięcia: ");
        label3.setText("Odchylenie standardowe (\u03C3\u00B2): ");
        label4.setText("Przedział czasowy wykonania ukończenia przedsięwzięcia: ");
        label5.setText("Czas realizacji przeskalowany (X): ");
        label6.setText("Prawdopodobieństwo zakończenia przedsięwzięcia w terminie do " + pert.getDeadline() + " dni: ");
    }

    public static JLabel getLabel1() {
        return label1;
    }

    public static void setLabel1(JLabel label1) {
        GUI.label1 = label1;
    }

    public static JLabel getLabel2() {
        return label2;
    }

    public static void setLabel2(JLabel label2) {
        GUI.label2 = label2;
    }

    public static JLabel getLabel3() {
        return label3;
    }

    public static void setLabel3(JLabel label3) {
        GUI.label3 = label3;
    }

    public static JLabel getLabel4() {
        return label4;
    }

    public static void setLabel4(JLabel label4) {
        GUI.label4 = label4;
    }

    public static JLabel getLabel5() {
        return label5;
    }

    public static void setLabel5(JLabel label5) {
        GUI.label5 = label5;
    }

    public static JLabel getLabel6() {
        return label6;
    }

    public static void setLabel6(JLabel label6) {
        GUI.label6 = label6;
    }
   
    public static void main(String[] args) {
        GUI.start();
    }
}