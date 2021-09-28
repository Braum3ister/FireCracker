package edu.kit.stephan.firecracker.view;


import edu.kit.stephan.firecracker.view.command.CommandParser;
import edu.kit.stephan.firecracker.view.command.Result;
import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SyntaxException;
import edu.kit.stephan.firecracker.view.command.Command;
import edu.kit.stephan.firecracker.model.firebreaker.GameHandlerFireBreaker;
import edu.kit.stephan.firecracker.model.firebreaker.board.GameBoard;
import edu.kit.stephan.firecracker.core.Input;
import edu.kit.stephan.firecracker.core.Output;
import edu.kit.stephan.firecracker.core.Pair;

import java.util.List;


/**
 * This class describes a session for command execution.
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class Session {
    private boolean isCodeRunning;
    private final GameHandlerFireBreaker gameHandlerFireBreaker;
    private final Output output;
    private final Output errOutput;
    private final Input input;
    private final CommandParser parser;


    /**
     * Instantiates a new Session.
     *
     * @param output    the output consumer
     * @param errOutput the error output consumer
     * @param input     the input supplier
     * @param parser    the parser used to decode the input strings
     * @param gameBoard the game board which was parsed
     */
    public Session(Output output, Output errOutput, Input input, CommandParser parser, GameBoard gameBoard) {
        this.gameHandlerFireBreaker = new GameHandlerFireBreaker(gameBoard);
        this.parser = parser;
        this.input = input;
        this.output = output;
        this.errOutput = errOutput;
    }

    /**
     * Method which starts the FireBreaker - Game
     */
    public void interactive() {
        gameHandlerFireBreaker.initialize(output);
        isCodeRunning = true;
        while (isCodeRunning) {
            processSingleCommand();
        }
    }


    private void processSingleCommand() {
        String inputUser = input.read(); //Scanner.readline()
        Pair<String, List<String>> parsedArguments; //command , Parameter

        try {
            parsedArguments = parser.parseCommand(inputUser);
        } catch (SyntaxException e) {
            errOutput.output(e.getMessage());
            return;
        }
        String command = parsedArguments.getFirstElement();
        List<String> parameters = parsedArguments.getSecondElement();

        executeSingleCommand(command, parameters);
    }

    private void executeSingleCommand(String commandName, List<String> parameters) {
        Result result;

        try {
            result = Command.getCommand(commandName).executeCommand(parameters, gameHandlerFireBreaker);
        } catch (SyntaxException e) {
            result = new Result(Result.ResultType.FAILURE, e.getMessage());
        }
        switch (result.getType()) {
            case SUCCESS:
                if (result.getMessage() != null) {
                    output.output(result.getMessage());
                } else {
                    isCodeRunning = false;
                }
                break;
            case FAILURE:
                if (result.getMessage() != null) {
                    errOutput.output(result.getMessage());
                } else {
                    errOutput.output(Errors.COMMAND_ENDED_ERROR);
                }
                break;
            default:
                throw new IllegalStateException(Errors.NOT_IMPLEMENTED);
        }
    }
}
