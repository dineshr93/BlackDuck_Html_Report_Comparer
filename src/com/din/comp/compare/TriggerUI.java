package com.din.comp.compare;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;



public class TriggerUI extends JPanel implements ActionListener {
    private static final long serialVersionUID = -4487732343062917781L;
    // JFileChooser fc;
    JButton clear,compare;
    JTextArea fc;

    JList dropZone;
    DefaultListModel listModel;
    JSplitPane childSplitPane, parentSplitPane;
    PrintStream ps;
    
    

    public TriggerUI() {
        super(new BorderLayout());

        /* fc = new JFileChooser();;
    fc.setMultiSelectionEnabled(true);
    fc.setDragEnabled(true);
    fc.setControlButtonsAreShown(false);
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);*/
        fc= new JTextArea();
        fc.setText("Rules:\n1. drop old html first.\n2. drop new html.\n3. click compare button.\n4. Wait until popup comes.\n5.Check output in the output.txt file.\nEnd");

        fc.setEditable(false);
        JPanel fcPanel = new JPanel(new BorderLayout());

        fcPanel.add(fc, BorderLayout.CENTER);


        compare = new JButton("Compare");
        compare.addActionListener(this);
        JPanel buttonPanel1 = new JPanel(new BorderLayout());
        buttonPanel1.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        buttonPanel1.add(compare, BorderLayout.LINE_END);


        clear = new JButton("Clear All");
        clear.addActionListener(this);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        buttonPanel.add(clear, BorderLayout.LINE_END);

        JPanel leftUpperPanel = new JPanel(new BorderLayout());
        leftUpperPanel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        leftUpperPanel.add(fcPanel, BorderLayout.CENTER);
        leftUpperPanel.add(buttonPanel1, BorderLayout.LINE_END);
        leftUpperPanel.add(buttonPanel, BorderLayout.PAGE_END);

        JScrollPane leftLowerPanel = new javax.swing.JScrollPane();
        leftLowerPanel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

        listModel = new DefaultListModel();
        dropZone = new JList(listModel);
        dropZone.setCellRenderer(new FileCellRenderer());
        dropZone.setTransferHandler(new ListTransferHandler(dropZone));
        dropZone.setDragEnabled(true);
        dropZone.setDropMode(javax.swing.DropMode.INSERT);
        dropZone.setBorder(new TitledBorder("Drag and drop files here"));
        leftLowerPanel.setViewportView(new JScrollPane(dropZone));

        childSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftLowerPanel,leftUpperPanel);
        childSplitPane.setDividerLocation(200);//400
        childSplitPane.setPreferredSize(new Dimension(300, 400));//480, 650

        /*console = new JTextArea();
    console.setColumns(40);
    console.setLineWrap(true);
    console.setBorder(new TitledBorder("Console"));

    parentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    childSplitPane, console);
    parentSplitPane.setDividerLocation(480);
    parentSplitPane.setPreferredSize(new Dimension(800, 650));*/

        add(childSplitPane, BorderLayout.CENTER);

    }

    public void setDefaultButton() {
        getRootPane().setDefaultButton(clear);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            listModel.clear();
            FileCellRenderer.files=new String[50];
        }else if (e.getSource() == compare) {
            //our function
        	if(FileCellRenderer.files[2]!=null || FileCellRenderer.files[0]==null||FileCellRenderer.files[1]==null)
    		{
    			JOptionPane.showMessageDialog (null, "Drag and drop 2 bom html reports to be compared", "Info", JOptionPane.INFORMATION_MESSAGE);
    			return;
    		}else{
        	try {
				BOMCompareWithShipment.perform(FileCellRenderer.files[0], FileCellRenderer.files[1]);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        }
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            //UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel");
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //Create and set up the window.
        JFrame frame = new JFrame("Bill of materials Comparer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the menu bar and content pane.
        TriggerUI demo = new TriggerUI();
        demo.setOpaque(true); //content panes must be opaque
        frame.setContentPane(demo);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        demo.setDefaultButton();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}


class FileCellRenderer extends DefaultListCellRenderer {
	static String files[] = new String[50];
	//int i=0;
    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);

        if (c instanceof JLabel && value instanceof File) {
            JLabel l = (JLabel)c;
            File f = (File)value;
            l.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
            l.setText(f.getName());
            //l.setText(f.getAbsolutePath());
            l.setToolTipText(f.getAbsolutePath());
            files[index]= f.getAbsolutePath();
        }

        return c;
    }
}

class ListTransferHandler extends TransferHandler {

    private JList list;

    ListTransferHandler(JList list) {
        this.list = list;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        // we only import FileList
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }

        // Check for FileList flavor
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            displayDropLocation("List doesn't accept a drop of this type.");
            return false;
        }

        // Get the fileList that is being dropped.
        Transferable t = info.getTransferable();
        List<File> data;
        try {
            data = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
        }
        catch (Exception e) { return false; }
        DefaultListModel model = (DefaultListModel) list.getModel();
        for (Object file : data) {
            model.addElement((File)file);
        }
        return true;
    }

    private void displayDropLocation(String string) {
        System.out.println(string);
    }
}