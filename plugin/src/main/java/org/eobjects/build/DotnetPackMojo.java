package org.eobjects.build;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "pack", defaultPhase = LifecyclePhase.PACKAGE)
public class DotnetPackMojo extends AbstractDotnetMojo {

    @Parameter(defaultValue = "${project.version}", readonly = true)
    private String version;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final PluginHelper helper = getPluginHelper();
        for (File subDirectory : helper.getProjectDirectories()) {
            helper.executeCommand(subDirectory, "dotnet", "pack", "-c", helper.getBuildConfiguration(),
                    "--version-suffix", version);
        }
    }
}