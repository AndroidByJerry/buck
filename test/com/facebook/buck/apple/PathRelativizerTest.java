/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.apple;

import static org.junit.Assert.assertEquals;

import com.facebook.buck.model.BuildTarget;
import com.facebook.buck.rules.BuildRuleResolver;
import com.facebook.buck.rules.SourcePathResolver;
import com.facebook.buck.rules.TestSourcePath;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathRelativizerTest {
  private Path root;
  private Path outputPath;
  private PathRelativizer pathRelativizer;

  @Before
  public void setUp() {
    root = Paths.get("/my/repo/root/");
    outputPath = root.resolve("output0/output1");
    pathRelativizer = new PathRelativizer(
        root,
        outputPath,
        new SourcePathResolver(new BuildRuleResolver()));
  }

  @Test
  public void testOutputPathToBuildTargetPath() {
    assertEquals(
        Paths.get("../../foo/bar"),
        pathRelativizer.outputDirToRootRelative(Paths.get("foo/bar")));
  }

  @Test
  public void testOutputPathToSourcePath() {
    assertEquals(
        Paths.get("../../source/path/foo.h"),
        pathRelativizer.outputPathToSourcePath(new TestSourcePath("source/path/foo.h")));
  }

  @Test
  public void testOutputDirToRootRelative() {
    assertEquals(
        Paths.get("../../foo/bar/file"),
        pathRelativizer.outputPathToBuildTargetPath(
            BuildTarget.builder("//foo/bar", "baz").build(),
            Paths.get("file")));
  }
}
