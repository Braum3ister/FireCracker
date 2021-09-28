package edu.kit.stephan.firecracker.view.command;

import edu.kit.stephan.firecracker.model.resources.Errors;
import edu.kit.stephan.firecracker.model.resources.SemanticsException;
import edu.kit.stephan.firecracker.model.resources.SyntaxException;
import edu.kit.stephan.firecracker.model.firebreaker.CardinalDirection;
import edu.kit.stephan.firecracker.model.firebreaker.GameHandlerFireBreaker;
import edu.kit.stephan.firecracker.model.firebreaker.Position;
import java.util.List;

/**
 * The Enum which holds all the Commands and is used to execute them.
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public enum Command {
    /**
     * The Move Command.
     */
    MOVE(CommandParserFireBreaker.MOVE, CommandParserFireBreaker.REGEX_MOVE) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker.moveCommand(parameters.get(0)
                        , createPositionOutOfInput(parameters.get(1), parameters.get(2)));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }

    },

    /**
     * The Quit Command.
     */
    QUIT(CommandParserFireBreaker.QUIT, CommandParserFireBreaker.REGEX_QUIT) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            return new Result(Result.ResultType.SUCCESS);
        }
    },

    /**
     * The Show board Command.
     */
    SHOW_BOARD(CommandParserFireBreaker.SHOW_BOARD, CommandParserFireBreaker.REGEX_SHOW_BOARD) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            return new Result(Result.ResultType.SUCCESS, gameHandlerFireBreaker.showBoardCommand());
        }
    },
    /**
     * The Show field Command.
     */
    SHOW_FIELD(CommandParserFireBreaker.SHOW_FIELD, CommandParserFireBreaker.REGEX_SHOW_FIELD) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker.showFieldCommand(createPositionOutOfInput(parameters.get(0)
                        , parameters.get(1)));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },
    /**
     * The Extinguish Command.
     */
    EXTINGUISH(CommandParserFireBreaker.EXTINGUISH, CommandParserFireBreaker.REGEX_EXTINGUISH) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker.extinguishCommand(parameters.get(0)
                        , createPositionOutOfInput(parameters.get(1), parameters.get(2)));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },
    /**
     * The Show player Command.
     */
    SHOW_PLAYER(CommandParserFireBreaker.SHOW_PLAYER, CommandParserFireBreaker.REGEX_SHOW_PLAYER) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker.showPlayerCommand();
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },
    /**
     * The Fire to roll Command.
     */
    FIRE_TO_ROLL(CommandParserFireBreaker.FIRE_TO_ROLL, CommandParserFireBreaker.REGEX_FIRE_TO_ROLL) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker
                        .fireToRollCommand(Command.createCardinalDirection(parameters.get(0)));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * The Buy fire engine Command.
     */
    BUY_FIRE_ENGINE(CommandParserFireBreaker.BUY_FIRE_ENGINE, CommandParserFireBreaker.REGEX_BUY_FIRE_ENGINE) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker.buyFireEngineCommand(Command.createPositionOutOfInput(
                        parameters.get(0), parameters.get(1)));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * The Refill Command.
     */
    REFILL(CommandParserFireBreaker.REFILL, CommandParserFireBreaker.REGEX_REFILL) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker.refillFireBrigadeCommand(parameters.get(0));
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * The PlayerManagement Command.
     */
    TURN(CommandParserFireBreaker.TURN, CommandParserFireBreaker.REGEX_TURN) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            String resultMessage;
            try {
                resultMessage = gameHandlerFireBreaker.turnCommand();
            } catch (SemanticsException e) {
                return new Result(Result.ResultType.FAILURE, e.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, resultMessage);
        }
    },

    /**
     * The Reset Command.
     */
    RESET(CommandParserFireBreaker.RESET, CommandParserFireBreaker.REGEX_RESET) {
        @Override
        public Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker) {
            return new Result(Result.ResultType.SUCCESS, gameHandlerFireBreaker.resetGameCommand());
        }
    };

    private final String commandName;
    private final String regexOfTheCommand;

    /**
     * Constructor of a Command
     * @param commandName the name of the Command
     * @param regexOfTheCommand the regex which corresponds to the Command.
     */
    Command(String commandName, String regexOfTheCommand) {
        this.commandName = commandName;
        this.regexOfTheCommand = regexOfTheCommand;
    }

    /**
     * Gets a command through the String representation
     *
     * @param commandName the command name
     * @return the found command
     * @throws SyntaxException if there is no command corresponding to the string
     */
    public static Command getCommand(String commandName) throws SyntaxException {
        for (Command command : Command.values()) {
            if (command.commandName.equals(commandName)) return command;
        }
        throw new SyntaxException(Errors.COMMAND_NOT_IMPLEMENTED);
    }

    /**
     * Gets regex of the command.
     *
     * @return the regex of the command
     */
    public String getRegexOfTheCommand() {
        return regexOfTheCommand;
    }

    /**
     * Executes a command with its parameters
     *
     * @param parameters             the parameters
     * @param gameHandlerFireBreaker the fire-breaker database on which the command is performed.
     * @return a Result which documents if the command succeeded or not and the corresponding message.
     */
    public abstract Result executeCommand(List<String> parameters, GameHandlerFireBreaker gameHandlerFireBreaker);

    private static Position createPositionOutOfInput(String inputNumberOne, String inputNumberTwo)
            throws SemanticsException {
        int xCoordinate;
        int yCoordinate;
        try {
            xCoordinate = Integer.parseInt(inputNumberOne);
            yCoordinate = Integer.parseInt(inputNumberTwo);
        } catch (NumberFormatException e) {
            throw new SemanticsException(Errors.POSITION_INVALID);
        }
        return new Position(xCoordinate, yCoordinate);
    }

    private static CardinalDirection createCardinalDirection(String direction) {
        return CardinalDirection.findDirectionThroughInteger(Integer.parseInt(direction));
    }
}
