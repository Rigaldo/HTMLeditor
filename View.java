package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private final UndoManager undoManager = new UndoManager();
    private final UndoListener undoListener = new UndoListener(undoManager);

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void exit() {
        controller.exit();
    }
    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, menuBar);
        MenuHelper.initEditMenu(this, menuBar);
        MenuHelper.initStyleMenu(this, menuBar);
        MenuHelper.initAlignMenu(this, menuBar);
        MenuHelper.initColorMenu(this, menuBar);
        MenuHelper.initFontMenu(this, menuBar);
        MenuHelper.initHelpMenu(this, menuBar);
        getContentPane().add(menuBar, BorderLayout.NORTH);
    }
    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        JScrollPane scrollPaneHtml = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", scrollPaneHtml);
        JScrollPane scrollPaneText = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст", scrollPaneText);
        tabbedPane.setPreferredSize(new Dimension(100, 100));
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        getContentPane().add(tabbedPane, "Center");
    }
    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }
    public void init() {
        initGui();
        addWindowListener(new FrameListener(this));
        setVisible(true);
    }
    public boolean canUndo() {
        return undoManager.canUndo();
    }
    public boolean canRedo() {
        return undoManager.canRedo();
    }
    public void undo() {
        try {
            undoManager.undo();
        }
        catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }
    public void redo() {
        try {
            undoManager.redo();
        }
        catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }
    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }
    public void resetUndo() {
        undoManager.discardAllEdits();
    }
    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == 0;
    }
    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }
    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }
    public void showAbout() {
        JOptionPane.showMessageDialog(this,
                "HTML Editor, v. 1.0\n" +
                        "by CBurns, 2022\nAll rights reserved.",
                "О программе", JOptionPane.INFORMATION_MESSAGE);
    }
    public void selectedTabChanged() {
        int index = tabbedPane.getSelectedIndex();
        if (index == 0) {
            controller.setPlainText(plainTextPane.getText());
        }
        else {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Новый":
                controller.createNewDocument();
                break;
            case "Открыть":
                controller.openDocument();
                break;
            case "Сохранить":
                controller.saveDocument();
                break;
            case "Сохранить как...":
                controller.saveDocumentAs();
                break;
            case "Выход":
                controller.exit();
                break;
            case "О программе":
                showAbout();
        }
    }
}
