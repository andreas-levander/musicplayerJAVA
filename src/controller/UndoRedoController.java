package controller;


import controller.undoredoCommands.Command;

import java.util.Stack;

public class UndoRedoController {

    private Stack<Command> undoCommands;
    private Stack<Command> redoCommands;


    public UndoRedoController() {
        undoCommands = new Stack<>();
        redoCommands = new Stack<>();
    }
    /** executes the command and adds it to the undoCommands stack and clears the redo stack*/
    public void onExecute(Command command) {
        undoCommands.push(command);
        command.execute();
        redoCommands.clear();
    }
    /** preforms the undo command and removes it from undo stack and adds it to redo stack*/
    public void onUndo() {
        if (!undoCommands.isEmpty()) {
            Command toUndo = undoCommands.pop();
            toUndo.undo();
            redoCommands.push(toUndo);
        }
    }
    /** preforms the redo command and removes it from redo stack and puts in back into undo stack*/
    public void onRedo() {
        if (!redoCommands.isEmpty()) {
            Command toRedo = redoCommands.pop();
            toRedo.execute();
            undoCommands.push(toRedo);
        }
    }
    public boolean undoIsEmpty() {
        return undoCommands.isEmpty();
    }
    public boolean redoIsEmpty() {
        return redoCommands.isEmpty();
    }
}
