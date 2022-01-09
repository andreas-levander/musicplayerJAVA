package controller.undoredoCommands;

public interface Command {
    void execute();
    void undo();
}
