package org.example.command;

import org.example.model.Resource;

import java.awt.*;
import java.io.File;
import java.net.URI;

public class ViewCommand implements Command {
    private Resource resource;

    public ViewCommand(Resource resource) { this.resource = resource; }

    @Override
    public void execute() throws org.example.exception.RepositoryException {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (resource.getLocation().startsWith("http")) {
                desktop.browse(new URI(resource.getLocation()));
            } else {
                desktop.open(new File(resource.getLocation()));
            }
        } catch (Exception e) {
            throw new org.example.exception.RepositoryException(e);
        }
    }
}