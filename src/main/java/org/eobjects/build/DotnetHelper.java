package org.eobjects.build;

import java.io.File;
import java.io.FileFilter;

import org.apache.maven.plugin.MojoFailureException;

public final class DotnetHelper {

    public static DotnetHelper get() {
        return new DotnetHelper();
    }

    private DotnetHelper() {
    }

    private final FileFilter projectJsonDirectoryFilter = new FileFilter() {
        public boolean accept(File dir) {
            if (dir.isDirectory()) {
                if (new File(dir, "project.json").exists()) {
                    return true;
                }
            }
            return false;
        }
    };

    public File[] getProjectDirectories() throws MojoFailureException {
        final File directory = new File(".");
        if (projectJsonDirectoryFilter.accept(directory)) {
            return new File[] { directory };
        }

        final File[] directories = directory.listFiles(projectJsonDirectoryFilter);
        if (directories == null || directories.length == 0) {
            throw new MojoFailureException("Could not find any directories with a 'project.json' file.");
        }
        return directories;
    }

    public void executeCommand(File subDirectory, String command) throws MojoFailureException {

        final ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.directory(subDirectory);
        processBuilder.inheritIO();

        final int exitCode;
        try {
            final Process process = processBuilder.start();
            exitCode = process.waitFor();
        } catch (Exception e) {
            throw new MojoFailureException("Command [" + command + "] failed", e);
        }

        if (exitCode == 0) {
            // success
        } else {
            throw new MojoFailureException("Command [" + command + "] returned non-zero exit code: " + exitCode);
        }
    }
}
