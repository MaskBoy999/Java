package org.example.command;

import org.example.exception.RepositoryException;

public interface Command {
    void execute() throws RepositoryException;
}