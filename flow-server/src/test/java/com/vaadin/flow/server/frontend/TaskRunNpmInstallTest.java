/*
 * Copyright 2000-2020 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.server.frontend;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.vaadin.flow.server.ExecutionFailedException;
import com.vaadin.flow.server.frontend.scanner.ClassFinder;
import com.vaadin.flow.server.frontend.scanner.FrontendDependencies;

public class TaskRunNpmInstallTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private NodeUpdater nodeUpdater;

    private TaskRunNpmInstall task;

    private File npmFolder;

    private Logger logger = Mockito.mock(Logger.class);

    @Before
    public void setUp() throws IOException {
        npmFolder = temporaryFolder.newFolder();
        nodeUpdater = new NodeUpdater(Mockito.mock(ClassFinder.class),
                Mockito.mock(FrontendDependencies.class), npmFolder,
                new File(""), null) {

            @Override
            public void execute() {
            }

            @Override
            Logger log() {
                return logger;
            }

        };
        task = createTask();
    }

    protected TaskRunNpmInstall createTask() {
        return new TaskRunNpmInstall(nodeUpdater, false);
    }

    @Test
    public void runNpmInstall_emptyDir_npmInstallIsExecuted()
            throws ExecutionFailedException {
        File nodeModules = getNodeUpdater().nodeModulesFolder;
        nodeModules.mkdir();
        nodeUpdater.modified = false;
        task.execute();

        Mockito.verify(logger).info(getRunningMsg());
    }

    @Test
    public void runNpmInstall_toolIsChanged_nodeModulesIsRemoved()
            throws ExecutionFailedException, IOException {
        File nodeModules = getNodeUpdater().nodeModulesFolder;
        nodeModules.mkdir();

        nodeUpdater.modified = true;
        File yaml = new File(nodeModules, ".modules.yaml");
        yaml.createNewFile();
        task.execute();

        Assert.assertFalse(yaml.exists());
    }

    @Test
    public void runNpmInstall_toolIsNotChanged_nodeModulesIsNotRemoved()
            throws ExecutionFailedException, IOException {
        File nodeModules = getNodeUpdater().nodeModulesFolder;
        nodeModules.mkdir();

        nodeUpdater.modified = true;
        File fakeFile = new File(nodeModules, ".fake.file");
        fakeFile.createNewFile();
        task.execute();

        Assert.assertTrue(fakeFile.exists());
    }

    @Test
    public void runNpmInstall_nonEmptyDir_npmInstallIsNotExecuted()
            throws IOException, ExecutionFailedException {
        File nodeModules = getNodeUpdater().nodeModulesFolder;
        nodeModules.mkdir();
        new File(nodeModules, "foo").createNewFile();
        nodeUpdater.modified = false;
        task.execute();

        Mockito.verify(logger)
                .info("Skipping `" + getToolName() + " install`.");
    }

    @Test
    public void runNpmInstall_dirContainsOnlyFlowNpmPackage_npmInstallIsNotExecuted()
            throws ExecutionFailedException {
        File nodeModules = getNodeUpdater().nodeModulesFolder;
        nodeModules.mkdir();
        new File(nodeModules, "@vaadin/flow-frontend/").mkdirs();
        nodeUpdater.modified = false;
        task.execute();

        Mockito.verify(logger).info(getRunningMsg());
    }

    @Test
    public void runNpmInstall_modified_npmInstallIsExecuted()
            throws ExecutionFailedException {
        nodeUpdater.modified = true;
        task.execute();

        Mockito.verify(logger).info(getRunningMsg());
    }

    private String getRunningMsg() {
        return "Running `" + getToolName() + " install` to "
                + "resolve and optionally download frontend dependencies. "
                + "This may take a moment, please stand by...";
    }

    protected NodeUpdater getNodeUpdater() {
        return nodeUpdater;
    }

    protected String getToolName() {
        return "npm";
    }
}
